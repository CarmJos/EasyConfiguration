package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.function.DataFunction;
import cc.carm.lib.configuration.option.FileConfigOptions;
import cc.carm.lib.configuration.source.section.ConfigurationSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class FileConfigSource<SELF extends FileConfigSource<SELF, ORIGINAL>, ORIGINAL>
        extends ConfigurationSource<SELF, ORIGINAL> {

    protected final @NotNull File file;
    protected final @Nullable String resourcePath;

    protected FileConfigSource(@NotNull ConfigurationHolder<? extends SELF> holder, long lastUpdateMillis,
                               @NotNull File file, @Nullable String resourcePath) {
        super(holder, lastUpdateMillis);
        this.file = file;
        this.resourcePath = resourcePath;
    }

    public Charset charset() {
        return holder().options().get(FileConfigOptions.CHARSET);
    }

    public boolean copyDefaults() {
        return holder().options().get(FileConfigOptions.COPY_DEFAULTS);
    }


    public void initializeFile() throws IOException {
        if (this.file.exists()) return;

        File parent = this.file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory " + file.getParentFile().getAbsolutePath());
        }

        if (!this.file.createNewFile()) {
            throw new IOException("Failed to create file " + file.getAbsolutePath());
        }

        if (resourcePath != null && copyDefaults()) {
            try {
                saveResource(resourcePath, false);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    protected <R> R fileInputStream(@NotNull DataFunction<InputStream, R> loader) {
        try (FileInputStream is = new FileInputStream(file)) {
            return loader.handle(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected <R> R fileReader(@NotNull DataFunction<Reader, R> loader) {
        return fileInputStream(is -> loader.handle(new InputStreamReader(is, charset())));
    }

    protected void fileOutputStream(@NotNull Consumer<FileOutputStream> stream) {
        try (FileOutputStream os = new FileOutputStream(file)) {
            stream.accept(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void fileWriter(@NotNull Consumer<Writer> writer) {
        fileOutputStream(os -> writer.accept(new OutputStreamWriter(os, charset())));
    }

    protected void saveResource(@NotNull String resourcePath, boolean replace)
            throws IOException, IllegalArgumentException {
        Objects.requireNonNull(resourcePath, "ResourcePath cannot be null");
        if (resourcePath.isEmpty()) throw new IllegalArgumentException("ResourcePath cannot be empty");

        resourcePath = resourcePath.replace('\\', '/');

        URL url = this.getClass().getClassLoader().getResource(resourcePath);
        if (url == null) throw new IllegalArgumentException("The resource '" + resourcePath + "' not exists");

        File outDir = file.getParentFile();

        if (!outDir.exists() && !outDir.mkdirs()) throw new IOException("Failed to create directory " + outDir);
        if (!file.exists() || replace) {
            try (OutputStream out = Files.newOutputStream(file.toPath())) {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                try (InputStream in = connection.getInputStream()) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
            }
        }
    }

    @Nullable
    protected InputStream getResource(@NotNull String filename) {
        try {
            URL url = this.getClass().getClassLoader().getResource(filename);
            if (url == null) return null;
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

}
