package cc.carm.lib.configuration.demo.tests;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.demo.tests.conf.DemoConfiguration;
import cc.carm.lib.configuration.demo.tests.conf.TestConfiguration;
import cc.carm.lib.configuration.demo.tests.model.TestModel;
import org.jetbrains.annotations.TestOnly;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConfigurationTest {

    @TestOnly
    public static void testDemo(ConfigurationProvider<?> provider) {
        provider.initialize(DemoConfiguration.class);

        System.out.println("----------------------------------------------------");

        System.out.println("Test Number: ");

        System.out.println("before: " + DemoConfiguration.TEST_NUMBER.get());
        DemoConfiguration.TEST_NUMBER.set((long) (Long.MAX_VALUE * Math.random()));
        System.out.println("after: " + DemoConfiguration.TEST_NUMBER.get());

        System.out.println("> Test Value:");
        System.out.println("before: " + DemoConfiguration.Sub.UUID_CONFIG_VALUE.get());
        DemoConfiguration.Sub.UUID_CONFIG_VALUE.set(UUID.randomUUID());
        System.out.println("after: " + DemoConfiguration.Sub.UUID_CONFIG_VALUE.get());

        System.out.println("> Test List:");

        System.out.println(" Before:");
        DemoConfiguration.Sub.That.OPERATORS.forEach(System.out::println);
        List<UUID> operators = IntStream.range(0, 5).mapToObj(i -> UUID.randomUUID()).collect(Collectors.toList());
        DemoConfiguration.Sub.That.OPERATORS.set(operators);
        System.out.println(" After:");
        DemoConfiguration.Sub.That.OPERATORS.forEach(System.out::println);

        System.out.println("> Clear List:");
        System.out.println(" Before: size :" + DemoConfiguration.Sub.That.OPERATORS.size());
        DemoConfiguration.Sub.That.OPERATORS.modifyList(List::clear);
        System.out.println(" After size :" + DemoConfiguration.Sub.That.OPERATORS.size());

        System.out.println("> Test Section:");
        System.out.println(DemoConfiguration.MODEL_TEST.get());
        DemoConfiguration.MODEL_TEST.set(TestModel.random());

        System.out.println("> Test Maps:");
        DemoConfiguration.USERS.forEach((k, v) -> System.out.println(k + ": " + v));
        LinkedHashMap<Integer, UUID> data = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) {
            data.put(i, UUID.randomUUID());
        }
        DemoConfiguration.USERS.set(data);
        System.out.println("----------------------------------------------------");
    }

    public static void testInner(ConfigurationProvider<?> provider) {

        TestConfiguration TEST = new TestConfiguration();

        provider.initialize(TEST, true);

        System.out.println("> Test Inner value before:");
        System.out.println(TEST.INNER.INNER_VALUE.getNotNull());

        double after = Math.random() * 200D;
        System.out.println("> Test Inner value -> " + after);
        TEST.INNER.INNER_VALUE.set(after);

        System.out.println("> Test Inner value after:");
        System.out.println(TEST.INNER.INNER_VALUE.getNotNull());

    }

    public static void save(ConfigurationProvider<?> provider) {
        try {
            provider.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
