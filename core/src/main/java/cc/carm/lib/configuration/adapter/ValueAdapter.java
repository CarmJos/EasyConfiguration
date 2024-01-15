package cc.carm.lib.configuration.adapter;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

public abstract class ValueAdapter<P extends ConfigurationProvider, B, V> {

    protected final Class<B> baseType;
    protected final Class<V> valueType;

    protected ValueAdapter(Class<B> baseType, Class<V> valueType) {
        this.baseType = baseType;
        this.valueType = valueType;
    }

    public Class<B> getBaseType() {
        return baseType;
    }

    public Class<V> getValueType() {
        return valueType;
    }

    public abstract B serialize(@NotNull P provider, @NotNull V value) throws Exception;

    public abstract V deserialize(@NotNull P provider, @NotNull B data) throws Exception;

    public boolean isAdaptedFrom(Class<?> clazz) {
        return clazz.isAssignableFrom(valueType);
    }

    public boolean isAdaptedFrom(Object object) {
        return isAdaptedFrom(object.getClass());
    }

    protected final V deserializeObject(P provider, Object data) throws Exception {
        return deserialize(provider, this.baseType.cast(data));
    }

    protected final B serializeObject(P provider, Object value) throws Exception {
        return serialize(provider, this.valueType.cast(value));
    }

}

