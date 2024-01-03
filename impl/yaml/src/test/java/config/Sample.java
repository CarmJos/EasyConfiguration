package config;

import cc.carm.lib.configuration.EasyConfiguration;
import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.annotation.InlineComment;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class Sample {

    @HeaderComment("Configurations for sample")
    interface SampleConfig extends Configuration {
        @HeaderComment("Configure your name!") // Header comment
        ConfiguredValue<String> NAME = ConfiguredValue.of("Joker");
        @InlineComment("Enabled?") // Inline comment
        ConfiguredValue<Boolean> ENABLED = ConfiguredValue.of(true);
    }

    public static void main(String[] args) {
        // 1. Make a configuration provider from a file.
        ConfigurationProvider<?> provider = EasyConfiguration.from("config.yml");
        // 2. Initialize the configuration classes or instances.
        provider.initialize(SampleConfig.class);
        // 3. Enjoy using the configuration!
        SampleConfig.ENABLED.set(false);
        System.out.println("Your name is " + SampleConfig.NAME.getNotNull() + " !");
    }

}
