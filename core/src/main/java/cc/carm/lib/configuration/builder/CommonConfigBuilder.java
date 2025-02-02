package cc.carm.lib.configuration.builder;

import cc.carm.lib.configuration.source.ConfigurationProvider;

public abstract class CommonConfigBuilder<T, B extends CommonConfigBuilder<T, B>>
        extends AbstractConfigBuilder<T, B, ConfigurationProvider<?>> {

    protected CommonConfigBuilder() {
        super(ConfigurationProvider.class);
    }

}
