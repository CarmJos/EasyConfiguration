package config;

import cc.carm.lib.configuration.EasyConfiguration;
import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.yaml.YamlConfigProvider;
import config.misc.TestUser;
import config.source.TestConfiguration;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConfigTester {

    @Test
    public void onTest() {

        YamlConfigProvider provider = EasyConfiguration.from("target/config.yml", "config.yml");
        ConfigInitializer.initialize(provider, TestConfiguration.class, true);

        System.out.println("before: " + TestConfiguration.Sub.UUID_CONFIG_VALUE.get());
        TestConfiguration.Sub.UUID_CONFIG_VALUE.set(UUID.randomUUID());
        System.out.println("after: " + TestConfiguration.Sub.UUID_CONFIG_VALUE.get());


        TestConfiguration.Sub.That.Operators.getNotNull().forEach(System.out::println);
        List<UUID> operators = IntStream.range(0, 5).mapToObj(i -> UUID.randomUUID()).collect(Collectors.toList());
        TestConfiguration.Sub.That.Operators.set(operators);

        System.out.println(TestConfiguration.USER.get());
        TestUser b = new TestUser(UUID.randomUUID().toString().substring(0, 3), UUID.randomUUID());
        TestConfiguration.USER.set(b);

        TestConfiguration.USERS.getNotNull().forEach((k, v) -> System.out.println(k + ": " + v));
        LinkedHashMap<Integer, UUID> data = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            data.put((int) (1000 * Math.random()), UUID.randomUUID());
        }
        TestConfiguration.USERS.set(data);

        try {
            provider.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
