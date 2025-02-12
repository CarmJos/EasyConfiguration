package config;

import cc.carm.lib.configuration.demo.tests.ConfigurationTest;
import cc.carm.lib.configuration.json.JSONConfigFactory;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.loader.PathGenerator;
import cc.carm.lib.configuration.source.option.StandardOptions;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import org.junit.Test;

import java.io.File;

public class JSONConfigTest {

    protected final ConfigurationHolder<?> holder = JSONConfigFactory
            .from(new File("target"), "config.json")
            .resourcePath("example.json")
            .build();

    @Test
    public void onTest() {

        ConfigValue<Boolean> EXAMPLE = ConfiguredValue.of(false);
        EXAMPLE.initialize(this.holder, "example");

        System.out.println("Example: " + EXAMPLE.get());

        ConfigurationTest.testDemo(this.holder);
        ConfigurationTest.testInner(this.holder);

        ConfigurationTest.save(this.holder);
    }


}
