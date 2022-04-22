package config.source;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigComment;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

@ConfigComment({"数据库配置", "  用于提供数据库连接，进行数据库操作。"})
public class DatabaseConfiguration extends ConfigurationRoot {

    @ConfigPath("driver")
    protected static final ConfigValue<String> DRIVER_NAME = ConfiguredValue.of(
            String.class, "com.mysql.cj.jdbc.Driver"
    );

    protected static final ConfigValue<String> HOST = ConfiguredValue.of(String.class, "127.0.0.1");
    protected static final ConfigValue<Integer> PORT = ConfiguredValue.of(Integer.class, 3306);

    protected static final ConfigValue<String> DATABASE = ConfiguredValue.of(String.class, "minecraft");
    protected static final ConfigValue<String> USERNAME = ConfiguredValue.of(String.class, "root");
    protected static final ConfigValue<String> PASSWORD = ConfiguredValue.of(String.class, "password");

    protected static final ConfigValue<String> EXTRA = ConfiguredValue.of(String.class, "?useSSL=false");

    protected static String buildJDBC() {
        return String.format("jdbc:mysql://%s:%s/%s%s", HOST.get(), PORT.get(), DATABASE.get(), EXTRA.get());
    }

}
