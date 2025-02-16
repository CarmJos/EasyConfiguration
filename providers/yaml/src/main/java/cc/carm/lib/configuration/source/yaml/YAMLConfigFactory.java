package cc.carm.lib.configuration.source.yaml;

import cc.carm.lib.configuration.commentable.Commentable;
import cc.carm.lib.configuration.commentable.CommentableMeta;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.file.FileConfigFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.File;
import java.util.Arrays;
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

    public YAMLConfigFactory dumper(@NotNull DumperOptions dumperOptions) {
        return option(YAMLOptions.DUMPER, dumperOptions);
    }

    public YAMLConfigFactory dumper(@NotNull Consumer<DumperOptions> modifier) {
        return option(YAMLOptions.DUMPER, modifier);
    }

    /**
     * Set the indent(in spaces) for the yaml file.
     * <p>e.g. for 2 spaces indent, the yaml file will look like:
     * <blockquote><pre>
     *     key:
     *       value: 1
     * </pre></blockquote>
     *
     * @param indent The indent (space) count, from 2 to 8.
     * @return Current {@link YAMLConfigFactory}
     * @see DumperOptions#setIndent(int)
     */
    public YAMLConfigFactory indent(@Range(from = 2, to = 8) int indent) {
        return dumper(d -> d.setIndent(indent));
    }

    /**
     * Set the indicator indent for the yaml file.
     * <p>Indicator indent is the indent for the indicators like '-'.
     * <p>e.g. for 2 spaces indicator indent, the yaml file will look like:
     * <blockquote><pre>
     *     key:
     *       - value1
     *       - value2
     * </pre></blockquote>
     *
     * @param indent The indicator indent, from 2 to 8.
     * @return Current {@link YAMLConfigFactory}
     * @see DumperOptions#setIndicatorIndent(int)
     */
    public YAMLConfigFactory indicatorIndent(@Range(from = 0, to = 9) int indent) {
        return dumper(d -> d.setIndicatorIndent(indent));
    }

    /**
     * Set the line width for a yaml file.
     * <p>When the line width is reached, the yaml file will be split into multiple lines.
     *
     * @param width The line width, from 8 to 1000.
     * @return Current {@link YAMLConfigFactory}
     * @see DumperOptions#setWidth(int)
     */
    public YAMLConfigFactory width(@Range(from = 8, to = 1000) int width) {
        return dumper(d -> d.setWidth(width));
    }

    /**
     * Set the header comments for the configuration file.
     * <p> This will override {@link cc.carm.lib.configuration.annotation.HeaderComments} in root classes.
     *
     * @param header The header comments to set
     * @return The current factory instance
     */
    public YAMLConfigFactory header(@NotNull String... header) {
        return metadata(null, holder -> holder.set(CommentableMeta.HEADER, Arrays.asList(header)));
    }

    /**
     * Set the footer comments for the configuration file.
     * <p> This will override {@link cc.carm.lib.configuration.annotation.FooterComments} in root classes.
     *
     * @param footer The footer comments to set
     * @return The current factory instance
     */
    public YAMLConfigFactory footer(@NotNull String... footer) {
        return metadata(null, holder -> holder.set(CommentableMeta.FOOTER, Arrays.asList(footer)));
    }

    @Override
    public @NotNull ConfigurationHolder<YAMLSource> build() {

        File configFile = this.file;
        String sourcePath = this.resourcePath;

        Commentable.registerMeta(this.initializer); // Register commentable meta types

        return new ConfigurationHolder<YAMLSource>(this.adapters, this.options, this.metadata, this.initializer) {
            final @NotNull YAMLSource source = new YAMLSource(this, configFile, sourcePath);

            @Override
            public @NotNull YAMLSource config() {
                return this.source;
            }
        };
    }


}
