package cc.carm.lib.configuration.demo.tests.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;

@HeaderComments("Inner Test")
public class InstanceConfig implements Configuration {

    public final ConfigValue<Double> STATUS = ConfiguredValue.of(1.0D);

}
