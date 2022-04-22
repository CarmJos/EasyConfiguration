package config.source;

import cc.carm.lib.configuration.core.ConfigurationRoot;
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

public class DemoConfiguration extends ConfigurationRoot {

    @ConfigPath(root = true)
    protected static final ConfigValue<Double> VERSION = ConfiguredValue.of(Double.class, 1.0D);

    // 可以直接写静态内部类，并通过 Class<?> 声明。
    public static final Class<?> SUB_TEST = Sub.class;

    @ConfigPath("user") // 通过注解规定配置文件中的路径，若不进行注解则以变量名自动生成。
    public static final ConfigValue<TestModel> MODEL_TEST = ConfiguredSection
            .builder(TestModel.class)
            .defaults(new TestModel("Carm", UUID.randomUUID()))
            .parseValue((section, defaultValue) -> TestModel.deserialize(section))
            .serializeValue(TestModel::serialize).build();

    // 子配置文件
    @ConfigPath("database")
    public static final Class<?> DB_CONFIG = DatabaseConfiguration.class;

    public static final ConfigValue<Map<Integer, UUID>> USERS = ConfiguredMap
            .builder(Integer.class, UUID.class).fromString()
            .parseKey(Integer::parseInt)
            .parseValue(v -> Objects.requireNonNull(UUID.fromString(v)))
            .build();


    public static class Sub extends ConfigurationRoot {

        @ConfigPath(value = "uuid-value", root = true)
        public static final ConfigValue<UUID> UUID_CONFIG_VALUE = ConfiguredValue
                .builder(UUID.class).fromString()
                .parseValue((data, defaultValue) -> UUID.fromString(data))
                .build();

        public static final Class<?> NOTHING = That.class;

        public static class That extends ConfigurationRoot {

            public static final ConfigValue<List<UUID>> OPERATORS = ConfiguredList
                    .builder(UUID.class).fromString()
                    .parseValue(s -> Objects.requireNonNull(UUID.fromString(s)))
                    .build();

        }

    }


}
