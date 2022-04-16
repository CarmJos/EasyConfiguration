package cc.carm.lib.configuration.bungee;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class BungeeConfigProvider extends FileConfigProvider {

    Configuration configuration;

    public BungeeConfigProvider(@NotNull File file) {
        super(file);
    }

    public void initialize() throws IOException {
        this.configuration = getLoader().load(file);
    }

    @Override
    public @NotNull ConfigurationWrapper getConfiguration() {
        return BungeeSectionWrapper.of(configuration);
    }

    @Override
    public void reload() throws Exception {
        this.configuration = getLoader().load(file);
    }

    @Override
    public void save() throws Exception {
        getLoader().save(configuration, file);
    }

    @Override
    public void setComments(@NotNull String path, @NotNull String... comments) {
    }

    @Override
    public @Nullable String[] getComments(@NotNull String path) {
        return null;
    }

    public static ConfigurationProvider getLoader() {
        return ConfigurationProvider.getProvider(YamlConfiguration.class);
    }

}
