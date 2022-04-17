package cc.carm.lib.configuration.spigot;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import cc.carm.lib.configuration.spigot.builder.SpigotConfigBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SpigotValue<T> extends CachedConfigValue<T> {

    public static @NotNull SpigotConfigBuilder builder() {
        return new SpigotConfigBuilder();
    }


    public SpigotValue(@Nullable SpigotConfigProvider provider,
                       @Nullable String configPath, @NotNull String[] comments, @Nullable T defaultValue) {
        super(provider, configPath, comments, defaultValue);
    }

    public SpigotConfigProvider getSpigotProvider() {
        ConfigurationProvider<?> provider = getProvider();
        if (provider instanceof SpigotConfigProvider) return (SpigotConfigProvider) getProvider();
        else throw new IllegalStateException("Provider is not a SpigotConfigProvider");
    }

    public SpigotSectionWrapper getSpigotConfig() {
        return getSpigotProvider().getConfiguration();
    }


}
