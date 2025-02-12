package cc.carm.lib.configuration.demo.tests.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComment;
import cc.carm.lib.configuration.annotation.InlineComment;
import cc.carm.lib.configuration.demo.DatabaseConfiguration;
import cc.carm.lib.configuration.demo.tests.model.UserRecord;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.standard.ConfiguredList;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;

import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;


@HeaderComment({"此处内容将显示在配置文件的最上方"})
public interface DemoConfiguration extends Configuration {

    @ConfigPath(root = true)
    ConfigValue<Double> VERSION = ConfiguredValue.of(Double.class, 1.0D);

    @ConfigPath(root = true)
    ConfigValue<Long> TEST_NUMBER = ConfiguredValue.of(1000000L);

    ConfigValue<ChronoUnit> TEST_ENUM = ConfiguredValue.of(ChronoUnit.class, ChronoUnit.DAYS);

    // 支持通过 Class<?> 变量标注子配置，一并注册。
    // 注意： 若对应类也有注解，则优先使用类的注解。
    Class<?> DATABASE = DatabaseConfiguration.class;

    @ConfigPath("registered_users") // 通过注解规定配置文件中的路径，若不进行注解则以变量名自动生成。
    @HeaderComment({"Section类型数据测试"}) // 通过注解给配置添加注释。
    @InlineComment("Section数据也支持InlineComment注释")
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
        @InlineComment("This is an inline comment")
        public static final ConfigValue<UUID> UUID_CONFIG_VALUE = ConfiguredValue
                .builderOf(UUID.class).fromString()
                .parse((holder, data) -> UUID.fromString(data))
                .build();

        public static class That implements Configuration {

            public static final ConfiguredList<UUID> OPERATORS = ConfiguredList
                    .builderOf(UUID.class).fromString()
                    .parse(s -> Objects.requireNonNull(UUID.fromString(s)))
                    .build();

        }
    }


}
