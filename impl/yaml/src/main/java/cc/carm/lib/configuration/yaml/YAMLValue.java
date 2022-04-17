package cc.carm.lib.configuration.yaml;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import cc.carm.lib.configuration.yaml.YamlConfigProvider;
import cc.carm.lib.configuration.yaml.YamlSectionWrapper;
import cc.carm.lib.configuration.yaml.builder.YamlConfigBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class YAMLValue<T> extends CachedConfigValue<T> {

    public static @NotNull YamlConfigBuilder builder() {
        return new YamlConfigBuilder();
    }

    public YAMLValue(@Nullable YamlConfigProvider provider,
                     @Nullable String configPath, @NotNull String[] comments, @Nullable T defaultValue) {
        super(provider, configPath, comments, defaultValue);
    }

    public YamlConfigProvider getYAMLProvider() {
        ConfigurationProvider<?> provider = getProvider();
        if (provider instanceof YamlConfigProvider) return (YamlConfigProvider) getProvider();
        else throw new IllegalStateException("Provider is not a SpigotConfigProvider");
    }

    public YamlSectionWrapper getYAMLConfig() {
        return getYAMLProvider().getConfiguration();
    }

}
