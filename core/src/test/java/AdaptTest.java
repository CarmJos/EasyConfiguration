import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.adapter.strandard.EnumAdapter;
import cc.carm.lib.configuration.adapter.strandard.PrimitiveAdapters;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;

public class AdaptTest {

    @Test
    public void test() throws Exception {

        ValueAdapterRegistry<ConfigurationProvider> registry = new ValueAdapterRegistry<>();
        registry.register(Long.class, PrimitiveAdapters.ofLong());
        registry.register(long.class, PrimitiveAdapters.ofLong());
        registry.register(Integer.class, PrimitiveAdapters.ofInteger());
        registry.register(int.class, PrimitiveAdapters.ofInteger());
        registry.register(Double.class, PrimitiveAdapters.ofDouble());
        registry.register(double.class, PrimitiveAdapters.ofDouble());
        registry.register(Float.class, PrimitiveAdapters.ofFloat());
        registry.register(float.class, PrimitiveAdapters.ofFloat());
        registry.register(Short.class, PrimitiveAdapters.ofShort());
        registry.register(short.class, PrimitiveAdapters.ofShort());
        registry.register(Byte.class, PrimitiveAdapters.ofByte());
        registry.register(byte.class, PrimitiveAdapters.ofByte());
        registry.register(Character.class, PrimitiveAdapters.ofCharacter());
        registry.register(char.class, PrimitiveAdapters.ofCharacter());
        registry.register(Boolean.class, PrimitiveAdapters.ofBoolean());
        registry.register(boolean.class, PrimitiveAdapters.ofBoolean());
        registry.register(String.class, PrimitiveAdapters.ofString());
        registry.register(new EnumAdapter<>());

        registry.register(Long.class, Duration.class, Duration::ofSeconds, Duration::getSeconds);
        registry.register(
                Duration.class, LocalTime.class,
                duration -> LocalTime.now().plus(duration),
                data -> Duration.between(LocalTime.now(), data)
        );

        ConfigurationProvider provider = new ConfigurationProvider();

        LocalTime v = registry.deserialize(provider, LocalTime.class, "600");
        Object d = registry.serialize(provider, v);

        System.out.println(v);
        System.out.println(d);
        System.out.println(registry.deserialize(provider, TestEnum.class, "b"));
        System.out.println(registry.serialize(provider, TestEnum.C).getClass());

    }

    enum TestEnum {
        A, b, C
    }

}
