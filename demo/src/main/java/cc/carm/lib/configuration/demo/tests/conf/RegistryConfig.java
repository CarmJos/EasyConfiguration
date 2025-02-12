package cc.carm.lib.configuration.demo.tests.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComment;
import cc.carm.lib.configuration.annotation.InlineComment;
import cc.carm.lib.configuration.demo.tests.model.UserRecord;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;

import java.util.UUID;

public class RegistryConfig implements Configuration {

    @HeaderComment("Support for configurations as instances")
    public final InstanceConfig INSTANCE = new InstanceConfig();

    @ConfigPath("test.user") // 通过注解规定配置文件中的路径，若不进行注解则以变量名自动生成。
    @HeaderComment({"Section类型数据测试"}) // 通过注解给配置添加注释。
    @InlineComment("Section数据也支持InlineComment注释")
    public final ConfigValue<UserRecord> TEST_MODEL = ConfiguredValue.builderOf(UserRecord.class).fromSection()
            .defaults(new UserRecord("Carm", UUID.randomUUID()))
            .parse((holder, section) -> UserRecord.deserialize(section))
            .serialize((holder, data) -> data.serialize()).build();


}
