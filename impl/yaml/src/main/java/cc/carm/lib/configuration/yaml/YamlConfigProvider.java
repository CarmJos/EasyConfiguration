package cc.carm.lib.configuration.yaml;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import org.bspfsystems.yamlconfiguration.commented.CommentedYamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class YamlConfigProvider extends FileConfigProvider<YamlSectionWrapper> {

    protected final @NotNull YamlComments comments = new YamlComments();
    protected CommentedYamlConfiguration configuration;
    protected ConfigInitializer<YamlConfigProvider> initializer;

    public YamlConfigProvider(@NotNull File file) {
        super(file);
    }

    public void initializeConfig() {
        this.configuration = CommentedYamlConfiguration.loadConfiguration(comments, file);
        this.initializer = new ConfigInitializer<>(this);
    }

    @Override
    public @NotNull YamlSectionWrapper getConfiguration() {
        return YamlSectionWrapper.of(configuration);
    }

    @Override
    public void reload() throws Exception {
        configuration.load(getFile());
    }

    @Override
    public void save() throws Exception {
        configuration.save(getFile());
    }

    @Override
    public void setComments(@NotNull String path, @NotNull String... comments) {
        this.comments.set(path, comments);
    }

    @Override
    public @Nullable String[] getComments(@NotNull String path) {
        return this.comments.get(path);
    }

    @Override
    public @NotNull ConfigInitializer<YamlConfigProvider> getInitializer() {
        return this.initializer;
    }

}
