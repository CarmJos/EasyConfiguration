package online.flowerinsnow.test.easyconfiguration;

import cc.carm.lib.configuration.EasyConfiguration;
import cc.carm.lib.configuration.demo.DatabaseConfiguration;
import cc.carm.lib.configuration.demo.tests.conf.DemoConfiguration;
import cc.carm.lib.configuration.hocon.HOCONFileConfigProvider;
import online.flowerinsnow.test.easyconfiguration.config.Config;
import org.junit.Test;

import java.io.File;

public class HOCONTest {
    @Test
    public void onTest() {
        HOCONFileConfigProvider provider = EasyConfiguration.from(new File("target/hocon.conf"));
        provider.initialize(Config.class);
        provider.initialize(DatabaseConfiguration.class);
        try {
            provider.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Config.TestObject.TEST_BOOLEAN.getNotNull() = " + Config.TestObject.TEST_BOOLEAN.getNotNull());
    }
}
