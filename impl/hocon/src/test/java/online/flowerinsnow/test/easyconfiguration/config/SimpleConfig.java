package online.flowerinsnow.test.easyconfiguration.config;

import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

public interface SimpleConfig extends Configuration {
    @HeaderComment("测试字段 int")
    ConfiguredValue<Integer> TEST_INT = ConfiguredValue.of(Integer.class, 15);

    @HeaderComment("测试字段 List<String>")
    ConfiguredList<String> TEST_LIST_STRING = ConfiguredList.of(String.class, "li", "li", "li1");

    @HeaderComment("测试对象")
    interface TestObject extends Configuration {

        @HeaderComment("测试字段 Boolean")
        ConfiguredValue<Boolean> TEST_BOOLEAN = ConfiguredValue.of(Boolean.class, true);

        @HeaderComment("inner")
        interface InnerObject extends Configuration {
            @HeaderComment("测试字段")
            public static final ConfiguredValue<Boolean> TEST_BOOLEAN_1 = ConfiguredValue.of(Boolean.class, true);
        }

    }
}
