package online.flowerinsnow.test.easyconfiguration;

import cc.carm.lib.configuration.EasyConfiguration;
import cc.carm.lib.configuration.demo.tests.ConfigurationTest;
import cc.carm.lib.configuration.hocon.HOCONFileConfigProvider;
import org.junit.Test;

import java.io.File;

public class HOCONTest {
    @Test
    public void onTest() {
        HOCONFileConfigProvider provider = EasyConfiguration.from(new File("target/hocon.conf"));

        ConfigurationTest.testDemo(provider);
        ConfigurationTest.testInner(provider);

        try {
            provider.save();
            provider.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
