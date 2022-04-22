package config.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
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

public class DemoConfiguration extends ConfigurationRoot {

    @ConfigPath(root = true)
    @ConfigComment({
            "有时候，需要在配置文件最上面显示点东西，",
            "此时就推荐添加一个可以用到但并不重要的参数到最上面",
            "并给他添加对应的注释。"
    })
    protected static final ConfigValue<Double> VERSION = ConfiguredValue.of(Double.class, 1.0D);


    // 支持通过 Class<?> 变量标注子配置，一并注册。
    // 注意： 若对应类也有注解，则优先使用类的注解。
    @ConfigPath("impl-test") //支持通过注解修改子配置的主路径，若不修改则以变量名自动生成。
    @ConfigComment("Something...") // 支持给子路径直接打注释
    public static final Class<?> IMPL = ImplConfiguration.class;

    // 子配置文件
    @ConfigPath("database")
    public static final Class<?> DB_CONFIG = DatabaseConfiguration.class;

    @ConfigPath("user") // 通过注解规定配置文件中的路径，若不进行注解则以变量名自动生成。
    @ConfigComment({"Section类型数据测试"}) // 通过注解给配置添加注释。
    public static final ConfigValue<TestModel> MODEL_TEST = ConfiguredSection
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


    /**
     * 支持内部类的直接注册。
     * 注意，需要使用 {@link ConfigInitializer#initialize(Class, boolean, boolean)} 方法，并设定第三个参数为 true。
     */
    public static class Sub extends ConfigurationRoot {

        @ConfigPath(value = "uuid-value", root = true)
        public static final ConfigValue<UUID> UUID_CONFIG_VALUE = ConfiguredValue
                .builder(UUID.class).fromString()
                .parseValue((data, defaultValue) -> UUID.fromString(data))
                .build();

        public static class That extends ConfigurationRoot {

            public static final ConfigValue<List<UUID>> OPERATORS = ConfiguredList
                    .builder(UUID.class).fromString()
                    .parseValue(s -> Objects.requireNonNull(UUID.fromString(s)))
                    .build();

        }

    }


}
