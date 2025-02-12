package cc.carm.lib.configuration.demo.tests;

import cc.carm.lib.configuration.demo.tests.conf.DemoConfiguration;
import cc.carm.lib.configuration.demo.tests.conf.RegistryConfig;
import cc.carm.lib.configuration.demo.tests.model.UserRecord;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import org.jetbrains.annotations.TestOnly;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConfigurationTest {

    @TestOnly
    public static void testDemo(ConfigurationHolder<?> holder) {
        try {
            holder.initialize(DemoConfiguration.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("----------------------------------------------------");

        System.out.println("Test Number: ");

        System.out.println("before: " + DemoConfiguration.TEST_NUMBER.get());
        DemoConfiguration.TEST_NUMBER.set((long) (Long.MAX_VALUE * Math.random()));
        System.out.println("after: " + DemoConfiguration.TEST_NUMBER.get());

        System.out.println("> Test Value:");
        System.out.println("before: " + DemoConfiguration.SUB.UUID_CONFIG_VALUE.get());
        DemoConfiguration.SUB.UUID_CONFIG_VALUE.set(UUID.randomUUID());
        System.out.println("after: " + DemoConfiguration.SUB.UUID_CONFIG_VALUE.get());

        System.out.println("> Test List:");

        System.out.println(" Before:");
        DemoConfiguration.SUB.That.OPERATORS.forEach(System.out::println);
        List<UUID> operators = IntStream.range(0, 5).mapToObj(i -> UUID.randomUUID()).collect(Collectors.toList());
        DemoConfiguration.SUB.That.OPERATORS.set(operators);
        System.out.println(" After:");
        DemoConfiguration.SUB.That.OPERATORS.forEach(System.out::println);

        System.out.println("> Clear List:");
        System.out.println(" Before: size :" + DemoConfiguration.SUB.That.OPERATORS.size());
        DemoConfiguration.SUB.That.OPERATORS.modify(List::clear);
        System.out.println(" After size :" + DemoConfiguration.SUB.That.OPERATORS.size());

        System.out.println("> Test Section:");
        System.out.println(DemoConfiguration.USERS.get());
        DemoConfiguration.USERS.add(UserRecord.random());

//        System.out.println("> Test Maps:");
//        DemoConfiguration.USERS.forEach((k, v) -> System.out.println(k + ": " + v));
//        LinkedHashMap<Integer, UUID> data = new LinkedHashMap<>();
//        for (int i = 1; i <= 5; i++) {
//            data.put(i, UUID.randomUUID());
//        }
//        DemoConfiguration.USERS.set(data);
        System.out.println("----------------------------------------------------");
    }

    public static void testInner(ConfigurationHolder<?> provider) {

        RegistryConfig TEST = new RegistryConfig();

        provider.initialize(TEST);

        System.out.println("> Test Inner value before:");
        System.out.println(TEST.INSTANCE.INNER_VALUE.resolve());

        double after = Math.random() * 200D;
        System.out.println("> Test Inner value -> " + after);
        TEST.INSTANCE.INNER_VALUE.set(after);

        System.out.println("> Test Inner value after:");
        System.out.println(TEST.INSTANCE.INNER_VALUE.resolve());

    }

    public static void save(ConfigurationHolder<?> provider) {
        try {
            provider.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
