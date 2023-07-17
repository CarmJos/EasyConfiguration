package cc.carm.lib.configuration.core.source.impl;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Objects;

public abstract class FileConfigProvider<W extends ConfigurationWrapper<?>> extends ConfigurationProvider<W> {

    protected final @NotNull File file;

    protected FileConfigProvider(@NotNull File file) {
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
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    public void saveResource(@NotNull String resourcePath, boolean replace)
            throws IOException, IllegalArgumentException {
        Objects.requireNonNull(resourcePath, "ResourcePath cannot be null");
        if (resourcePath.equals("")) throw new IllegalArgumentException("ResourcePath cannot be empty");

        resourcePath = resourcePath.replace('\\', '/');

        URL url = this.getClass().getClassLoader().getResource(resourcePath);
        if (url == null) throw new IllegalArgumentException("The resource '" + resourcePath + "' not exists");

        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(file.getParentFile(), resourcePath.substring(0, Math.max(lastIndex, 0)));

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
