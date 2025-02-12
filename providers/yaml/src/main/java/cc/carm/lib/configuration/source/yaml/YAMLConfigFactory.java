package cc.carm.lib.configuration.source.yaml;

import cc.carm.lib.configuration.commentable.CommentableMetaTypes;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.file.FileConfigFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class YAMLConfigFactory extends FileConfigFactory<YAMLSource, ConfigurationHolder<YAMLSource>, YAMLConfigFactory> {

    public static YAMLConfigFactory from(@NotNull String path) {
        return new YAMLConfigFactory(new File(path));
    }

    public static YAMLConfigFactory from(@NotNull File file) {
        return new YAMLConfigFactory(file);
    }

    public static YAMLConfigFactory from(@NotNull File parent, @NotNull String configName) {
        return new YAMLConfigFactory(new File(parent, configName));
    }

    public YAMLConfigFactory(@NotNull File file) {
        super(file);
    }

    @Override
    protected YAMLConfigFactory self() {
        return this;
    }

    public YAMLConfigFactory loader(@NotNull LoaderOptions loaderOptions) {
        return option(YAMLOptions.LOADER, loaderOptions);
    }

    public YAMLConfigFactory loader(@NotNull Consumer<LoaderOptions> modifier) {
        return option(YAMLOptions.LOADER, modifier);
    }

    public YAMLConfigFactory indent(@Range(from = 2, to = 8) int indent) {
        return dumper(d -> d.setIndent(indent));
    }

    public YAMLConfigFactory width(@Range(from = 8, to = 1000) int width) {
        return dumper(d -> d.setWidth(width));
    }

    public YAMLConfigFactory dumper(@NotNull DumperOptions dumperOptions) {
        return option(YAMLOptions.DUMPER, dumperOptions);
    }

    public YAMLConfigFactory dumper(@NotNull Consumer<DumperOptions> modifier) {
        return option(YAMLOptions.DUMPER, modifier);
    }

    @Override
    public @NotNull ConfigurationHolder<YAMLSource> build() {

        File configFile = this.file;
        String sourcePath = this.resourcePath;

        CommentableMetaTypes.register(this.initializer); // Register commentable meta types

        return new ConfigurationHolder<YAMLSource>(
                this.adapters, this.options, new HashMap<>(), this.initializer
        ) {
            final @NotNull YAMLSource source = new YAMLSource(this, configFile, sourcePath);

            @Override
            public @NotNull YAMLSource config() {
                return this.source;
            }
        };
    }


}
