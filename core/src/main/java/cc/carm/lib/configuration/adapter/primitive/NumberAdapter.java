package cc.carm.lib.configuration.adapter.primitive;

import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

public abstract class NumberAdapter<P extends ConfigurationProvider, T extends Number> extends PrimitiveAdapter<P, T> {

    public static <P extends ConfigurationProvider, T extends Number> NumberAdapter<P, T> of(Class<T> numberClass,
                                                                                             ConfigDataFunction<Object, T> function) {
        return new NumberAdapter<P, T>(numberClass) {
            @Override
            public T deserialize(@NotNull P provider, @NotNull Object data) throws Exception {
                return function.parse(data);
            }
        };
    }

    protected NumberAdapter(Class<T> valueType) {
        super(valueType);
    }


}
