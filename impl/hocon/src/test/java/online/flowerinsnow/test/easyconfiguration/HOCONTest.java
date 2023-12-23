package online.flowerinsnow.test.easyconfiguration;

import cc.carm.lib.configuration.EasyConfiguration;
import cc.carm.lib.configuration.hocon.HOCONFileConfigProvider;
import online.flowerinsnow.test.easyconfiguration.config.SimpleConfig;
import org.junit.Test;

import java.io.File;

public class HOCONTest {
    @Test
    public void onTest() {
        HOCONFileConfigProvider provider = EasyConfiguration.from(new File("target/hocon.conf"));
        provider.initialize(SimpleConfig.class);
//        provider.initialize(DatabaseConfiguration.class);

        System.out.println(SimpleConfig.TEST_INT.getNotNull());
        SimpleConfig.TEST_INT.set((int) (Math.random() * 100 * 5));
        System.out.println(SimpleConfig.TEST_INT.getNotNull());

        try {
            provider.save();
            provider.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
