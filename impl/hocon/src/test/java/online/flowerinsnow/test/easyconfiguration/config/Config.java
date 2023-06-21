package online.flowerinsnow.test.easyconfiguration.config;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public class Config extends ConfigurationRoot {
    @HeaderComment("测试字段 int")
    public static final ConfiguredValue<Integer> TEST_INT = ConfiguredValue.of(Integer.class, 15);

    @HeaderComment("测试字段 List<String>")
    public static final ConfiguredList<String> TEST_LIST_STRING = ConfiguredList.of(String.class, "li", "li", "li1");

    @HeaderComment("测试对象")
    public static class TestObject extends ConfigurationRoot {
        @HeaderComment("测试字段 Boolean")
        public static final ConfiguredValue<Boolean> TEST_BOOLEAN = ConfiguredValue.of(Boolean.class, true);
    }
}
