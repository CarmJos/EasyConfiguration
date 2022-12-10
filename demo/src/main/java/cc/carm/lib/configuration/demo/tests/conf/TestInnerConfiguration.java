package cc.carm.lib.configuration.demo.tests.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

@HeaderComment("Inner Test")
public class TestInnerConfiguration extends ConfigurationRoot {

    public final ConfigValue<Double> INNER_VALUE = ConfiguredValue.of(Double.class, 1.0D);

}
