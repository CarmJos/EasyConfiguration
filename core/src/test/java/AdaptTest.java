import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.adapter.primitive.PrimitiveAdapter;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;

public class AdaptTest {

    @Test
    public void test() throws Exception {

        ValueAdapterRegistry<?> registry = new ValueAdapterRegistry<>(new ConfigurationProvider());
        registry.register(PrimitiveAdapter.of(Long.class, data -> Long.parseLong(data.toString())));
        registry.register(PrimitiveAdapter.of(Integer.class, data -> Integer.parseInt(data.toString())));
        registry.register(PrimitiveAdapter.of(long.class, data -> Long.parseLong(data.toString())));
        registry.register(Long.class, Duration.class, Duration::ofSeconds, Duration::getSeconds);
        registry.register(
                Duration.class, LocalTime.class,
                duration -> LocalTime.now().plus(duration),
                data -> Duration.between(data, LocalTime.now())
        );

        LocalTime v = registry.deserialize(LocalTime.class, "600");
        Object d = registry.serialize(v);

        System.out.println(v);
        System.out.println(d);
    }

}
