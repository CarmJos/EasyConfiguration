package cc.carm.lib.configuration.adapter;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Value adapter, used to convert the value of the configuration file into the objects.
 *
 * @param <P> The type of the configuration provider.
 * @param <B> The type of the base data
 * @param <V> The type of the target value
 */
public abstract class ValueAdapter<P extends ConfigurationProvider, B, V> {

    protected final Class<? super B> baseType;
    protected final Class<? super V> valueType;

    protected ValueAdapter(Class<? super B> baseType, Class<? super V> valueType) {
        this.baseType = baseType;
        this.valueType = valueType;
    }

    public Class<? super B> getBaseClass() {
        return baseType;
    }

    public Class<? super V> getValueClass() {
        return valueType;
    }

    public abstract B serialize(@NotNull P provider, @NotNull V value) throws Exception;

    public abstract V deserialize(@NotNull P provider, @NotNull Class<? extends V> clazz, @NotNull B data) throws Exception;

    public boolean isAdaptedFrom(Class<?> clazz) {
        return clazz.isAssignableFrom(valueType);
    }

    public boolean isAdaptedFrom(Object object) {
        return isAdaptedFrom(object.getClass());
    }

    public boolean isAdapterOf(Class<?> clazz) {
        return valueType.isAssignableFrom(clazz);
    }

    @SuppressWarnings("unchecked")
    protected final V deserializeObject(P provider, Class<?> valueClass, Object data) throws Exception {
        return deserialize(provider, (Class<? extends V>) valueClass, (B) data);
    }

    @SuppressWarnings("unchecked")
    protected final B serializeObject(P provider, Object value) throws Exception {
        return serialize(provider, (V) value);
    }

}

