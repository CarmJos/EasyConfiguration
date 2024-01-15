package cc.carm.lib.configuration.yaml;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.impl.CachedConfigValue;
import cc.carm.lib.configuration.yaml.builder.YAMLConfigBuilder;
import org.jetbrains.annotations.NotNull;

public abstract class YAMLValue<T> extends CachedConfigValue<T> {

    public static @NotNull YAMLConfigBuilder builder() {
        return new YAMLConfigBuilder();
    }

    public YAMLValue(@NotNull ValueManifest<T> manifest) {
        super(manifest);
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
