package cc.carm.lib.configuration.adapter.primitive;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

public abstract class PrimitiveAdapter<P extends ConfigurationProvider, T> extends ValueAdapter<P, Object, T> {

    protected PrimitiveAdapter(Class<T> valueType) {
        super(Object.class, valueType);
    }

    @Override
    public Object serialize(@NotNull P provider, @NotNull T value) throws Exception {
        return value;
    }

}
