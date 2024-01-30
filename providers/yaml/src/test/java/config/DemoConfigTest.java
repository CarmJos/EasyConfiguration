package config;

import cc.carm.lib.configuration.EasyConfiguration;
import cc.carm.lib.configuration.demo.tests.ConfigurationTest;
import cc.carm.lib.configuration.demo.tests.model.AbstractModel;
import cc.carm.lib.configuration.yaml.YAMLConfigProvider;
import config.model.AnyModel;
import config.model.SomeModel;
import config.source.ModelConfiguration;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerialization;
import org.junit.Test;

import java.io.IOException;

public class DemoConfigTest {

    static {
        ConfigurationSerialization.registerClass(SomeModel.class);
        ConfigurationSerialization.registerClass(AnyModel.class);
    }

    protected final YAMLConfigProvider provider = EasyConfiguration.from("target/config.yml", "test/test2/config.yml");

    @Test
    public void onTest() {

        ConfigurationTest.testDemo(this.provider);
        ConfigurationTest.testInner(this.provider);

        testSerialization(this.provider);

        ConfigurationTest.save(this.provider);
    }


    public static void testSerialization(YAMLConfigProvider provider) {
        provider.initialize(ModelConfiguration.class);
        System.out.println("----------------------------------------------------");

        AbstractModel someModel = ModelConfiguration.SOME_MODEL.get();
        if (someModel != null) System.out.println(someModel.getName());

        AbstractModel anyModel = ModelConfiguration.ANY_MODEL.get();
        if (anyModel != null) System.out.println(anyModel.getName());

        ModelConfiguration.MODELS.forEach(System.out::println);
        ModelConfiguration.MODEL_MAP.forEach((v, anyModel1) -> System.out.println(v + " -> " + anyModel1.toString()));


        System.out.println("----------------------------------------------------");
    }


}
