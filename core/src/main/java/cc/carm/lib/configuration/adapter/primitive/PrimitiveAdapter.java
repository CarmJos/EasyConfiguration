package cc.carm.lib.configuration.adapter.primitive;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

public abstract class PrimitiveAdapter<P extends ConfigurationProvider, T> extends ValueAdapter<P, Object, T> {

    public static <P extends ConfigurationProvider, T> PrimitiveAdapter<P, T> of(@NotNull Class<T> clazz,
                                                                                 @NotNull ConfigDataFunction<Object, T> function) {
        return new PrimitiveAdapter<P, T>(clazz) {
            @Override
            public T deserialize(@NotNull P provider, @NotNull Object data) throws Exception {
                return function.parse(data);
            }
        };
    }


    protected PrimitiveAdapter(Class<T> valueType) {
        super(Object.class, valueType);
    }

    @Override
    public Object serialize(@NotNull P provider, @NotNull T value) throws Exception {
        return value;
    }

}
