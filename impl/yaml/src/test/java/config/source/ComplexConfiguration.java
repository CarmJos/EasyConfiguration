package config.source;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigComment;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredMap;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import config.model.TestModel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ComplexConfiguration extends ConfigurationRoot {

    @ConfigComment({"User测试"})
    public static final ConfigValue<TestModel> USER = ConfiguredSection
            .builder(TestModel.class)
            .defaults(new TestModel("Carm", UUID.randomUUID()))
            .parseValue((section, defaultValue) -> TestModel.deserialize(section))
            .serializeValue(TestModel::serialize).build();

    @ConfigComment({"[ID-UUID] 对照表", "", "用于测试Map类型的解析与序列化保存"})
    public static final ConfigValue<Map<Integer, UUID>> USERS = ConfiguredMap
            .builder(Integer.class, UUID.class).fromString()
            .parseKey(Integer::parseInt)
            .parseValue(v -> Objects.requireNonNull(UUID.fromString(v)))
            .build();

    public static class Sub {

        @ConfigPath(value = "uuid", root = true)
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
