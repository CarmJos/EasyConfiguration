package cc.carm.lib.configuration.demo.tests.conf;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.annotation.InlineComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredMap;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.configuration.demo.tests.model.TestModel;

import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;


@HeaderComment({"此处内容将显示在配置文件的最上方"})
public class DemoConfiguration extends ConfigurationRoot {

    @ConfigPath(root = true)
    protected static final ConfigValue<Double> VERSION = ConfiguredValue.of(Double.class, 1.0D);

    @ConfigPath(root = true)
    public static final ConfigValue<Long> TEST_NUMBER = ConfiguredValue.of(Long.class, 1000000L);

    public static final ConfigValue<ChronoUnit> TEST_ENUM = ConfiguredValue.of(ChronoUnit.class, ChronoUnit.DAYS);

    // 支持通过 Class<?> 变量标注子配置，一并注册。
    // 注意： 若对应类也有注解，则优先使用类的注解。
    @ConfigPath("other-class-config") //支持通过注解修改子配置的主路径，若不修改则以变量名自动生成。
    @HeaderComment({"", "Something..."}) // 支持给子路径直接打注释
    @InlineComment("InlineComments for class path")
    public static final Class<?> OTHER = OtherConfiguration.class;

    @ConfigPath("user") // 通过注解规定配置文件中的路径，若不进行注解则以变量名自动生成。
    @HeaderComment({"Section类型数据测试"}) // 通过注解给配置添加注释。
    @InlineComment("Section数据也支持InlineComment注释")
    public static final ConfigValue<TestModel> MODEL_TEST = ConfiguredSection
            .builderOf(TestModel.class)
            .defaults(new TestModel("Carm", UUID.randomUUID()))
            .parseValue((section, defaultValue) -> TestModel.deserialize(section))
            .serializeValue(TestModel::serialize).build();

    @HeaderComment({"[ID - UUID]对照表", "", "用于测试Map类型的解析与序列化保存"})
    public static final ConfiguredMap<Integer, UUID> USERS = ConfiguredMap
            .builderOf(Integer.class, UUID.class)
            .asLinkedMap().fromString()
            .parseKey(Integer::parseInt)
            .parseValue(v -> Objects.requireNonNull(UUID.fromString(v)))
            .build();


    /**
     * 支持内部类的直接注册。
     * 注意，需要使用 {@link ConfigInitializer#initialize(Class, boolean, boolean)} 方法，并设定第三个参数为 true。
     */
    public static class Sub extends ConfigurationRoot {

        @ConfigPath(value = "uuid-value", root = true)
        @InlineComment("This is an inline comment")
        public static final ConfigValue<UUID> UUID_CONFIG_VALUE = ConfiguredValue
                .builderOf(UUID.class).fromString()
                .parseValue((data, defaultValue) -> UUID.fromString(data))
                .build();

        public static class That extends ConfigurationRoot {

            public static final ConfiguredList<UUID> OPERATORS = ConfiguredList
                    .builderOf(UUID.class).fromString()
                    .parseValue(s -> Objects.requireNonNull(UUID.fromString(s)))
                    .build();

        }
    }


}
