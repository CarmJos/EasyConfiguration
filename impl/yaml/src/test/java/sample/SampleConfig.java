package sample;

import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComment;
import cc.carm.lib.configuration.annotation.InlineComment;
import cc.carm.lib.configuration.value.standard.ConfiguredList;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;

import java.util.UUID;

@HeaderComment("Configurations for sample")
public interface SampleConfig extends Configuration {

    @InlineComment("Enabled?") // Inline comment
    ConfiguredValue<Boolean> ENABLED = ConfiguredValue.of(true);

    ConfiguredList<UUID> UUIDS = ConfiguredList.builderOf(UUID.class).fromString()
            .parseValue(UUID::fromString).serializeValue(UUID::toString)
            .defaults(
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    UUID.fromString("00000000-0000-0000-0000-000000000001")
            ).build();

    interface INFO extends Configuration {

        @HeaderComment("Configure your name!") // Header comment
        ConfiguredValue<String> NAME = ConfiguredValue.of("Joker");

        @ConfigPath("year") // Custom path
        ConfiguredValue<Integer> AGE = ConfiguredValue.of(24);

    }
}
