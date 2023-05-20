package cc.carm.lib.configuration.demo.tests.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.annotation.InlineComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.configuration.demo.tests.model.TestModel;

import java.util.UUID;

public class TestConfiguration extends ConfigurationRoot {

    public final TestInnerConfiguration INNER = new TestInnerConfiguration();

    public final ConfigValue<Double> CLASS_VALUE = ConfiguredValue.of(Double.class, 1.0D);

    @ConfigPath("test.user") // 通过注解规定配置文件中的路径，若不进行注解则以变量名自动生成。
    @HeaderComment({"Section类型数据测试"}) // 通过注解给配置添加注释。
    @InlineComment("Section数据也支持InlineComment注释")
    public final ConfigValue<TestModel> TEST_MODEL = ConfiguredSection
            .builderOf(TestModel.class)
            .defaults(new TestModel("Carm", UUID.randomUUID()))
            .parseValue((section, defaultValue) -> TestModel.deserialize(section))
            .serializeValue(TestModel::serialize).build();


}
