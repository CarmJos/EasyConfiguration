package cc.carm.lib.configuration.demo.tests.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.HeaderComment;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;

@HeaderComment("Inner Test")
public class InstanceConfig implements Configuration {

    public final ConfigValue<Double> INNER_VALUE = ConfiguredValue.of(1.0D);

}
