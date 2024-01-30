package cc.carm.lib.configuration.demo;

import cc.carm.lib.configuration.source.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComment;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;

@HeaderComment({"", "数据库配置", "  用于提供数据库连接，进行数据库操作。"})
public class DatabaseConfiguration implements Configuration {

    @ConfigPath("driver")
    @HeaderComment({
            "数据库驱动配置，请根据数据库类型设置。",
            "- MySQL(旧): com.mysql.jdbc.Driver",
            "- MySQL(新): com.mysql.cj.jdbc.Driver",
            "- MariaDB(推荐): org.mariadb.jdbc.Driver",
    })
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
