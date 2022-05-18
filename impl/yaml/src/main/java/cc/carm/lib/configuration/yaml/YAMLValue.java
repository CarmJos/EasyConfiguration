package cc.carm.lib.configuration.yaml;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import cc.carm.lib.configuration.yaml.builder.YAMLConfigBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class YAMLValue<T> extends CachedConfigValue<T> {

    public static @NotNull YAMLConfigBuilder builder() {
        return new YAMLConfigBuilder();
    }

    public YAMLValue(@Nullable YAMLConfigProvider provider, @Nullable String configPath,
                     @Nullable List<String> headerComments, @Nullable String inlineComments,
                     @Nullable T defaultValue) {
        super(provider, configPath, headerComments, inlineComments, defaultValue);
    }

    public YAMLConfigProvider getYAMLProvider() {
        ConfigurationProvider<?> provider = getProvider();
        if (provider instanceof YAMLConfigProvider) return (YAMLConfigProvider) getProvider();
        else throw new IllegalStateException("Provider is not a YamlConfigProvider");
    }

    public YAMLSectionWrapper getYAMLConfig() {
        return getYAMLProvider().getConfiguration();
    }

}
