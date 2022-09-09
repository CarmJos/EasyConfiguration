package cc.carm.lib.configuration.core.source.impl;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public abstract class FileConfigProvider<W extends ConfigurationWrapper<?>> extends ConfigurationProvider<W> {

    protected final @NotNull File file;

    public FileConfigProvider(@NotNull File file) {
        this.file = file;
    }

    public @NotNull File getFile() {
        return file;
    }

    public void initializeFile(@Nullable String sourcePath) throws IOException {
        if (this.file.exists()) return;

        File parent = this.file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory " + file.getParentFile().getAbsolutePath());
        }

        if (!this.file.createNewFile()) {
            throw new IOException("Failed to create file " + file.getAbsolutePath());
        }

        if (sourcePath != null) {
            try {
                saveResource(sourcePath, true);
            } catch (Exception ignored) {
            }
        }
    }

    public void saveResource(@NotNull String resourcePath, boolean replace)
            throws NullPointerException, IOException, IllegalArgumentException {
        Objects.requireNonNull(resourcePath, "ResourcePath cannot be null");
        if (resourcePath.equals("")) throw new IllegalArgumentException("ResourcePath cannot be empty");

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null) throw new IllegalArgumentException("The resource '" + resourcePath + "' not exists");


        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(file, resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists() && !outDir.mkdirs()) throw new IOException("Failed to create directory " + outDir);
        if (!file.exists() || replace) {
            try {

                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Nullable
    public InputStream getResource(@NotNull String filename) {
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
