package cc.carm.lib.configuration.yaml;

import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import cc.carm.lib.configuration.yaml.builder.YAMLConfigBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class YAMLValue<T> extends CachedConfigValue<T> {

    public static @NotNull YAMLConfigBuilder builder() {
        return new YAMLConfigBuilder();
    }

    public YAMLValue(@Nullable YAMLConfigProvider provider,
                     @Nullable String configPath, @Nullable ConfigCommentInfo comments,
                     @Nullable T defaultValue) {
        super(provider, configPath, comments, defaultValue);
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
