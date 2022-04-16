package cc.carm.lib.configuration.spigot;

import cc.carm.lib.configuration.commented.CommentedYamlConfiguration;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class SpigotConfigProvider extends FileConfigProvider {

    ConfigComments comments = new ConfigComments();
    CommentedYamlConfiguration configuration;

    public SpigotConfigProvider(@NotNull File file) {
        super(file);
    }

    public void initialize() {
        this.configuration = CommentedYamlConfiguration.loadConfiguration(comments, file);
    }

    @Override
    public @NotNull ConfigurationWrapper getConfiguration() {
        return SpigotSectionWrapper.of(configuration);
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

}
