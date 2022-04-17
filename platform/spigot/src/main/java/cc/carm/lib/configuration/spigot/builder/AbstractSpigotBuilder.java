package cc.carm.lib.configuration.spigot.builder;

import cc.carm.lib.configuration.core.builder.AbstractConfigBuilder;
import cc.carm.lib.configuration.spigot.SpigotConfigProvider;

public abstract class AbstractSpigotBuilder<T, B extends AbstractSpigotBuilder<T, B>>
        extends AbstractConfigBuilder<T, B, SpigotConfigProvider> {

    public AbstractSpigotBuilder() {
        super(SpigotConfigProvider.class);
    }

}
