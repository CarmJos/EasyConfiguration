package config;

import cc.carm.lib.configuration.EasyConfiguration;
import cc.carm.lib.configuration.yaml.YamlConfigProvider;
import config.model.AbstractModel;
import config.model.SomeModel;
import config.model.TestModel;
import config.source.ComplexConfiguration;
import config.source.DemoConfiguration;
import config.source.ImplConfiguration;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerialization;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConfigTester {


    @Test
    public void onTest() {
        ConfigurationSerialization.registerClass(TestModel.class);
        ConfigurationSerialization.registerClass(SomeModel.class);

        YamlConfigProvider provider = EasyConfiguration.from("target/config.yml", "config.yml");

        testDemo(provider);
        testComplex(provider);
        testSerialization(provider);

        try {
            provider.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void testSerialization(YamlConfigProvider provider) {
        System.out.println("----------------------------------------------------");
        provider.initialize(ImplConfiguration.class);
        System.out.println(ImplConfiguration.TEST.get());
        ImplConfiguration.TEST.set(TestModel.random());

        AbstractModel model = provider.getConfiguration().getSerializable("ImplConfiguration.test", TestModel.class);

        provider.getConfiguration().set("ImplConfiguration.some", new SomeModel("lover", 123));
        AbstractModel model2 = provider.getConfiguration().getSerializable("ImplConfiguration.some", SomeModel.class);

        System.out.println("model1: " + Optional.ofNullable(model).map(AbstractModel::getName).orElse(null));
        System.out.println("model1: " + Optional.ofNullable(model2).map(AbstractModel::getName).orElse(null));


        System.out.println("----------------------------------------------------");
    }

    public static void testDemo(YamlConfigProvider provider) {
        provider.initialize(DemoConfiguration.class);
    }

    public static void testComplex(YamlConfigProvider provider) {
        System.out.println("----------------------------------------------------");
        provider.initialize(ComplexConfiguration.class);

        System.out.println("> Test Value:");
        System.out.println("before: " + ComplexConfiguration.Sub.UUID_CONFIG_VALUE.get());
        ComplexConfiguration.Sub.UUID_CONFIG_VALUE.set(UUID.randomUUID());
        System.out.println("after: " + ComplexConfiguration.Sub.UUID_CONFIG_VALUE.get());

        System.out.println("> Test List:");
        ComplexConfiguration.Sub.That.Operators.getNotNull().forEach(System.out::println);
        List<UUID> operators = IntStream.range(0, 5).mapToObj(i -> UUID.randomUUID()).collect(Collectors.toList());
        ComplexConfiguration.Sub.That.Operators.set(operators);

        System.out.println("> Test Section:");
        System.out.println(ComplexConfiguration.USER.get());
        ComplexConfiguration.USER.set(TestModel.random());

        System.out.println("> Test Maps:");
        ComplexConfiguration.USERS.getNotNull().forEach((k, v) -> System.out.println(k + ": " + v));
        LinkedHashMap<Integer, UUID> data = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            data.put((int) (1000 * Math.random()), UUID.randomUUID());
        }
        ComplexConfiguration.USERS.set(data);
        System.out.println("----------------------------------------------------");
    }


}
