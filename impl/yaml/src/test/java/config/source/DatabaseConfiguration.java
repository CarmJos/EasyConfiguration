package config.source;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigComment;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

@ConfigComment({"数据库配置", "  用于提供数据库连接，进行数据库操作。"})
public class DatabaseConfiguration extends ConfigurationRoot {

    @ConfigPath("driver")
    @ConfigComment(value = {
            "数据库驱动配置，请根据数据库类型设置。",
            "- MySQL: com.mysql.cj.jdbc.Driver",
            "- MariaDB(推荐): org.mariadb.jdbc.Driver",
    }, startWrap = false)
    protected static final ConfigValue<String> DRIVER_NAME = ConfiguredValue.of(
            String.class, "com.mysql.cj.jdbc.Driver"
    );

    @ConfigComment(startWrap = false)
    protected static final ConfigValue<String> HOST = ConfiguredValue.of(String.class, "127.0.0.1");
    @ConfigComment(startWrap = false)
    protected static final ConfigValue<Integer> PORT = ConfiguredValue.of(Integer.class, 3306);
    @ConfigComment(startWrap = false)
    protected static final ConfigValue<String> DATABASE = ConfiguredValue.of(String.class, "minecraft");
    @ConfigComment(startWrap = false)
    protected static final ConfigValue<String> USERNAME = ConfiguredValue.of(String.class, "root");
    @ConfigComment(startWrap = false)
    protected static final ConfigValue<String> PASSWORD = ConfiguredValue.of(String.class, "password");
    @ConfigComment(startWrap = false)
    protected static final ConfigValue<String> EXTRA = ConfiguredValue.of(String.class, "?useSSL=false");

    protected static String buildJDBC() {
        return String.format("jdbc:mysql://%s:%s/%s%s", HOST.get(), PORT.get(), DATABASE.get(), EXTRA.get());
    }

}
