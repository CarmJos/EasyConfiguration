package cc.carm.lib.configuration.builder;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.value.ConfigValue;

public abstract class CommonConfigBuilder<TYPE, RESULT extends ConfigValue<TYPE>, SELF extends CommonConfigBuilder<TYPE, RESULT, SELF>>
        extends AbstractConfigBuilder<TYPE, RESULT, ConfigurationHolder<?>, SELF> {

    protected CommonConfigBuilder(ValueType<TYPE> type) {
        super(ConfigurationHolder.class, type);
    }

}
