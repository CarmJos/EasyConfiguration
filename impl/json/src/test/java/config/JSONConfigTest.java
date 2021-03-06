package config;

import cc.carm.lib.configuration.EasyConfiguration;
import cc.carm.lib.configuration.json.JSONConfigProvider;
import config.model.TestModel;
import config.source.DemoConfiguration;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JSONConfigTest {

    protected final JSONConfigProvider provider = EasyConfiguration.from("target/config.yml", "config.yml");


    @Test
    public void onTest() {

        provider.initialize(DemoConfiguration.class);

        testDemo();
        System.out.println("----------------------------------------------------");
        provider.getConfiguration().getValues(true).forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("----------------------------------------------------");
        provider.getConfiguration().getValues(false).forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("----------------------------------------------------");

        try {
            provider.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void testDemo() {
        System.out.println("----------------------------------------------------");

        System.out.println("> Test Value:");
        System.out.println("before: " + DemoConfiguration.Sub.UUID_CONFIG_VALUE.get());
        DemoConfiguration.Sub.UUID_CONFIG_VALUE.set(UUID.randomUUID());
        System.out.println("after: " + DemoConfiguration.Sub.UUID_CONFIG_VALUE.get());

        System.out.println("> Test List:");
        DemoConfiguration.Sub.That.OPERATORS.getNotNull().forEach(System.out::println);
        List<UUID> operators = IntStream.range(0, 5).mapToObj(i -> UUID.randomUUID()).collect(Collectors.toList());
        DemoConfiguration.Sub.That.OPERATORS.set(operators);

        System.out.println("> Test Section:");
        System.out.println(DemoConfiguration.MODEL_TEST.get());
        DemoConfiguration.MODEL_TEST.set(TestModel.random());

        System.out.println("> Test Maps:");
        DemoConfiguration.USERS.getNotNull().forEach((k, v) -> System.out.println(k + ": " + v));
        LinkedHashMap<Integer, UUID> data = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) {
            data.put(i, UUID.randomUUID());
        }
        DemoConfiguration.USERS.set(data);
        System.out.println("----------------------------------------------------");
    }


}
