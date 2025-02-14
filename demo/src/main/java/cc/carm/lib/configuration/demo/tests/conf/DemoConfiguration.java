package cc.carm.lib.configuration.demo.tests.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.FooterComments;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.configuration.annotation.InlineComment;
import cc.carm.lib.configuration.demo.DatabaseConfiguration;
import cc.carm.lib.configuration.demo.tests.model.UserRecord;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.standard.ConfiguredList;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;

import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@ConfigPath(root = true)
@HeaderComments({"此处内容将显示在配置文件的最上方"})
@FooterComments({"此处内容将显示在配置文件的最下方", "可用于显示版权信息等"})
public interface DemoConfiguration extends Configuration {

    @ConfigPath(root = true)
    ConfigValue<Double> VERSION = ConfiguredValue.of(Double.class, 1.0D);

    @ConfigPath(root = true)
    @FooterComments({"此处内容将显示在配置条目的下方", "可用于补充说明，但一般不建议使用"})
    ConfigValue<Long> TEST_NUMBER = ConfiguredValue.of(1000000L);

    @HeaderComments({"枚举类型测试"})
    @FooterComments({"上述的枚举内容本质上是通过STRING解析的"})
    ConfigValue<ChronoUnit> TEST_ENUM = ConfiguredValue.of(ChronoUnit.class, ChronoUnit.DAYS);

    // 支持通过 Class<?> 变量标注子配置，一并注册。
    // 注意： 若对应类也有注解，则优先使用类的注解。
    Class<?> DATABASE = DatabaseConfiguration.class;

    @ConfigPath("registered_users") // 通过注解规定配置文件中的路径，若不进行注解则以变量名自动生成。
    @HeaderComments({"Section类型数据测试"}) // 通过注解给配置添加注释。
    @InlineComment("默认地注释会加到Section的首行末尾") // 通过注解给配置添加注释。
    @InlineComment(value = "用户名(匹配注释)", regex = "name") // 通过注解给配置添加注释。
    @InlineComment(value = "信息", regex = "info.*") // 通过注解给配置添加注释。
    ConfiguredList<UserRecord> USERS = ConfiguredList.builderOf(UserRecord.class).fromSection()
            .parse(UserRecord::deserialize).serialize(UserRecord::serialize)
            .defaults(UserRecord.CARM).build();

//    @HeaderComment({"[ID - UUID]对照表", "", "用于测试Map类型的解析与序列化保存"})
//    ConfiguredMap<Integer, UUID> USERS = ConfiguredMap.builderOf(Integer.class, UUID.class)
//            .asLinkedMap().fromString()
//            .parseKey(Integer::parseInt)
//            .parseValue(v -> Objects.requireNonNull(UUID.fromString(v)))
//            .build();


    /**
     * 支持内部类的直接注册。
     * 注意，需要启用 {@link  cc.carm.lib.configuration.source.option.StandardOptions#LOAD_SUB_CLASSES}
     */
    class SUB implements Configuration {

        @ConfigPath(value = "uuid-value", root = true)
        public static final ConfigValue<UUID> UUID_CONFIG_VALUE = ConfiguredValue
                .builderOf(UUID.class).fromString()
                .parse((holder, data) -> UUID.fromString(data))
                .build();

        @HeaderComments({"内部类的内部类测试", "通过这种方式，您可以轻易实现多层次的配置文件结构"})
        public interface That extends Configuration {

            ConfiguredList<UUID> OPERATORS = ConfiguredList
                    .builderOf(UUID.class).fromString()
                    .parse(s -> Objects.requireNonNull(UUID.fromString(s)))
                    .build();

        }
    }


}
