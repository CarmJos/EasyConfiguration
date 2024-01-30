package config;

import cc.carm.lib.configuration.EasyConfiguration;
import cc.carm.lib.configuration.demo.tests.ConfigurationTest;
import cc.carm.lib.configuration.json.JSONConfigProvider;
import org.junit.Test;

public class JSONConfigTest {

    protected final JSONConfigProvider provider = EasyConfiguration.from("target/config.json", "config.json");


    @Test
    public void onTest() {

        ConfigurationTest.testDemo(this.provider);
        ConfigurationTest.testInner(this.provider);

        System.out.println("----------------------------------------------------");
        provider.getConfiguration().getValues(true).forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("----------------------------------------------------");
        provider.getConfiguration().getValues(false).forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("----------------------------------------------------");
        
        ConfigurationTest.save(this.provider);
    }


}
