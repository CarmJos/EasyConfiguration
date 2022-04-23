package cc.carm.lib.configuration.core.builder;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;

public abstract class CommonConfigBuilder<T, B extends CommonConfigBuilder<T, B>>
        extends AbstractConfigBuilder<T, B, ConfigurationProvider<?>> {

    public CommonConfigBuilder() {
        super(ConfigurationProvider.class);
    }

}
