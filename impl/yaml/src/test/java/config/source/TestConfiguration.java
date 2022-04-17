package config.source;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigComment;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.util.MapFactory;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredMap;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import config.misc.TestUser;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TestConfiguration extends ConfigurationRoot {

    @ConfigComment({"User测试"})
    public static final ConfigValue<TestUser> USER = ConfiguredSection
            .builder(TestUser.class)
            .defaults(new TestUser("Carm", UUID.randomUUID()))
            .parseValue((section, defaultValue) -> new TestUser(
                    section.getString("name"),
                    UUID.fromString(section.getString("user.uuid", UUID.randomUUID().toString()))
            )).serializeValue(user -> MapFactory.<String, Object>linkedMap()
                    .put("name", user.getName())
                    .put("user.uuid", user.getUuid().toString())
                    .build()
            ).build();

    @ConfigComment({"[ID-UUID] 对照表", "", "用于测试Map类型的解析与序列化保存"})
    public static final ConfigValue<Map<Integer, UUID>> USERS = ConfiguredMap
            .builder(Integer.class, UUID.class).fromString()
            .parseKey(Integer::parseInt)
            .parseValue(v -> Objects.requireNonNull(UUID.fromString(v)))
            .build();

    public static class Sub {

        @ConfigPath("uuid")
        public static final ConfigValue<UUID> UUID_CONFIG_VALUE = ConfiguredValue
                .builder(UUID.class).fromString()
                .parseValue((data, defaultValue) -> UUID.fromString(data))
                .build();

        @ConfigPath("nothing")
        public static class That {

            public static final ConfigValue<List<UUID>> Operators = ConfiguredList
                    .builder(UUID.class).fromString()
                    .parseValue(s -> Objects.requireNonNull(UUID.fromString(s)))
                    .build();

        }


    }


}
