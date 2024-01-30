package cc.carm.lib.configuration.yaml.builder;

import cc.carm.lib.configuration.core.builder.AbstractConfigBuilder;
import cc.carm.lib.configuration.yaml.YAMLConfigProvider;

public abstract class AbstractYAMLBuilder<T, B extends AbstractYAMLBuilder<T, B>>
        extends AbstractConfigBuilder<T, B, YAMLConfigProvider> {

    public AbstractYAMLBuilder() {
        super(YAMLConfigProvider.class);
    }

}
