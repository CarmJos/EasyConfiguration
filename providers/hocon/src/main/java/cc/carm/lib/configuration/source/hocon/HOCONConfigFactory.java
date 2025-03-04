package cc.carm.lib.configuration.source.hocon;

import cc.carm.lib.configuration.commentable.Commentable;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.file.FileConfigFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class HOCONConfigFactory extends FileConfigFactory<HOCONSource, ConfigurationHolder<HOCONSource>, HOCONConfigFactory> {
    public static HOCONConfigFactory from(@NotNull String path) {
        return new HOCONConfigFactory(new File(path));
    }

    public static HOCONConfigFactory from(@NotNull File file) {
        return new HOCONConfigFactory(file);
    }

    public static HOCONConfigFactory from(@NotNull File parent, @NotNull String configName) {
        return new HOCONConfigFactory(new File(parent, configName));
    }

    public HOCONConfigFactory(@NotNull File file) {
        super(file);
    }

    @Override
    protected HOCONConfigFactory self() {
        return this;
    }

    @Override
    public @NotNull ConfigurationHolder<HOCONSource> build() {
        File configFile = this.file;
        String sourcePath = this.resourcePath;

        Commentable.registerMeta(this.initializer); // Register commentable meta types

        return new ConfigurationHolder<HOCONSource>(this.adapters, this.options, this.metadata, this.initializer) {
            final @NotNull HOCONSource source = new HOCONSource(this, configFile, sourcePath);

            @Override
            public @NotNull HOCONSource config() {
                return this.source;
            }
        };
    }
}
