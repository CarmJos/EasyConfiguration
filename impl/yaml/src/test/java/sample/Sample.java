package sample;

import cc.carm.lib.configuration.EasyConfiguration;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;

public class Sample {

    public static void main(String[] args) {
        // 1. Make a configuration provider from a file.
        ConfigurationProvider<?> provider = EasyConfiguration.from("config.yml");
        // 2. Initialize the configuration classes or instances.
        provider.initialize(SampleConfig.class);
        // 3. Enjoy using the configuration!
        SampleConfig.ENABLED.set(false);
        System.out.println("Your name is " + SampleConfig.INFO.NAME.getNotNull() + " !");
    }

}
