package sample;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.yaml.YAMLConfigFactory;
import org.junit.Test;

public class SampleTest {

    @Test
    public void test() {
        // 1. Make a configuration provider from a file.
        ConfigurationHolder<?> holder = YAMLConfigFactory.from("target/config.yml")
                .resourcePath("configs/sample.yml")
                .indent(4) // Optional: Set the indentation of the configuration file.
                .build();

        // 2. Initialize the configuration classes or instances.
        holder.initialize(SampleConfig.class);
        // 3. Enjoy using the configuration!
        SampleConfig.ENABLED.set(false);
        System.out.println("Your name is " + SampleConfig.INFO.NAME.resolve() + " (age=" + SampleConfig.INFO.AGE.resolve() + ")!");
    }

}
