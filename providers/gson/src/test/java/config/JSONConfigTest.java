package config;

import cc.carm.lib.configuration.demo.tests.ConfigurationTest;
import cc.carm.lib.configuration.json.JSONConfigFactory;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.option.StandardOptions;
import org.junit.Test;

import java.io.File;

public class JSONConfigTest {

    protected final ConfigurationHolder<?> holder = JSONConfigFactory
            .from(new File("target"), "config.json")
            .option(StandardOptions.PATH_SEPARATOR, '-')
            .build();

    @Test
    public void onTest() {
        ConfigurationTest.testDemo(this.holder);
        ConfigurationTest.testInner(this.holder);

        ConfigurationTest.save(this.holder);
    }


}
