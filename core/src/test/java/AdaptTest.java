import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.adapter.strandard.PrimitiveAdapters;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.source.loader.ConfigurationLoader;
import cc.carm.lib.configuration.source.option.ConfigurationOptionHolder;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import cc.carm.test.config.TestSource;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdaptTest {

    @Test
    public void test() throws Exception {

        ValueAdapterRegistry registry = new ValueAdapterRegistry();
        registry.register(PrimitiveAdapters.ADAPTERS);
        registry.register(PrimitiveAdapters.ofEnum());


        registry.register(ValueType.of(Long.class), ValueType.of(Duration.class), Duration::ofMillis, Duration::toMillis);
        registry.register(
                ValueType.of(Duration.class), ValueType.of(LocalTime.class),
                duration -> LocalTime.now().plus(duration),
                data -> Duration.between(LocalTime.now(), data)
        );

        ConfigurationProvider<TestSource> provider = new ConfigurationProvider<>(
                new TestSource(), new ConfigurationLoader(),
                registry, new ConfigurationOptionHolder(), new ConcurrentHashMap<>()
        );

        LocalTime v = registry.deserialize(provider, LocalTime.class, 600000L);
        Object d = registry.serialize(provider, v);

        System.out.println(v);
        System.out.println(d);
        System.out.println(registry.deserialize(provider, TestEnum.class, "C"));
        System.out.println(registry.serialize(provider, TestEnum.C).getClass());
    }

    enum TestEnum {
        A, b, C
    }

}
