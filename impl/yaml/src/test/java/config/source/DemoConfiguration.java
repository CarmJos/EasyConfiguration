package config.source;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigComment;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

@ConfigPath("database")
@ConfigComment({"数据库配置", "  用于提供数据库连接，进行数据库操作。"})
public class DemoConfiguration extends ConfigurationRoot {

    @ConfigPath(root = true)
    @ConfigComment({
            "有时候，需要在配置文件最上面显示点东西，",
            "此时就推荐添加一个可以用到但并不重要的参数到最上面",
            "并给他添加对应的注释。"
    })
    protected static final ConfigValue<Double> VERSION = ConfiguredValue.of(Double.class, 1.0D);


    @ConfigPath("driver")
    @ConfigComment({
            "数据库驱动配置，请根据数据库类型设置。",
            "- MySQL: com.mysql.cj.jdbc.Driver",
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
        return String.format("jdbc:mysql://%s:%s/%s%s",
                HOST.get(), PORT.get(), DATABASE.get(), EXTRA.get()
        );
    }


}
