package cc.carm.lib.configuration.spigot;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import cc.carm.lib.configuration.commented.CommentedYamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class SpigotConfigProvider extends FileConfigProvider<SpigotSectionWrapper> {

    protected final ConfigComments comments = new ConfigComments();
    protected ConfigInitializer<SpigotConfigProvider> initializer;
    protected CommentedYamlConfiguration configuration;

    public SpigotConfigProvider(@NotNull File file) {
        super(file);
    }

    public void initializeConfig() {
        this.configuration = CommentedYamlConfiguration.loadConfiguration(comments, file);
        this.initializer = new ConfigInitializer<>(this);
    }

    public void say() {
        System.out.println("Hello");
    }

    @Override
    public @NotNull SpigotSectionWrapper getConfiguration() {
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

    @Override
    public @NotNull ConfigInitializer<SpigotConfigProvider> getInitializer() {
        return this.initializer;
    }

}
