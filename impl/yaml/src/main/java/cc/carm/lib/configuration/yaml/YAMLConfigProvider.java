package cc.carm.lib.configuration.yaml;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import org.bspfsystems.yamlconfiguration.commented.CommentedYamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class YAMLConfigProvider extends FileConfigProvider<YAMLSectionWrapper> {

    protected final @NotNull YAMLComments comments = new YAMLComments();
    protected CommentedYamlConfiguration configuration;
    protected ConfigInitializer<YAMLConfigProvider> initializer;

    public YAMLConfigProvider(@NotNull File file) {
        super(file);
    }

    public void initializeConfig() {
        this.configuration = CommentedYamlConfiguration.loadConfiguration(comments, file);
        this.initializer = new ConfigInitializer<>(this);
    }

    @Override
    public @NotNull YAMLSectionWrapper getConfiguration() {
        return YAMLSectionWrapper.of(configuration);
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
    public @NotNull ConfigInitializer<YAMLConfigProvider> getInitializer() {
        return this.initializer;
    }

}
