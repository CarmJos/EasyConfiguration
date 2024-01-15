package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import org.jetbrains.annotations.NotNull;

public abstract class ConfigurationBuilder<P extends ConfigurationProvider> {

    protected ConfigurationLoader loader;
    protected ValueAdapterRegistry<P> processors;



    public abstract @NotNull ConfigurationProvider build();

}
