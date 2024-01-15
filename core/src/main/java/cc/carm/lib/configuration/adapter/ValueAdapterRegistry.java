package cc.carm.lib.configuration.adapter;

import cc.carm.lib.configuration.adapter.primitive.PrimitiveAdapter;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ValueAdapterRegistry<P extends ConfigurationProvider> {

    protected final @NotNull P provider;
    protected final Map<Class<?>, ValueAdapter<P, ?, ?>> adapters = new HashMap<>();

    public ValueAdapterRegistry(@NotNull P provider) {
        this.provider = provider;
    }

    public void register(@NotNull ValueAdapter<P, ?, ?> adapter) {
        adapters.put(adapter.valueType, adapter);
    }

    public <B, V> void register(Class<B> baseClass, Class<V> valueClass,
                                ConfigDataFunction<B, V> parser,
                                ConfigDataFunction<V, B> serializer) {
        register(new ValueAdapter<P, B, V>(baseClass, valueClass) {
            @Override
            public B serialize(@NotNull P provider, @NotNull V value) throws Exception {
                return serializer.parse(value);
            }

            @Override
            public V deserialize(@NotNull P provider, @NotNull B data) throws Exception {
                return parser.parse(data);
            }
        });
    }

    public void unregister(@NotNull Class<?> typeClass) {
        adapters.remove(typeClass);
    }

    public <T> T deserialize(Class<T> type, Object value) throws Exception {
        if (value == null) return null;
        if (type == Object.class) return type.cast(value);

        ValueAdapter<P, ?, ?> adapter = adapters.get(type);
        if (adapter == null) throw new RuntimeException("No adapter for type " + type.getName());

        // CHECK IF VALUE IS ADAPTED FROM GIVEN VALUE'S TYPE
        if (adapter.isAdaptedFrom(value)) return type.cast(adapter.deserializeObject(provider, value));

        // OTHERWISE, WE NEED TO DESERIALIZE ONE BY ONE
        Object baseValue = deserialize(adapter.getBaseType(), value);
        if (baseValue == null) return null; // Null check

        return type.cast(adapter.deserializeObject(provider, baseValue));
    }

    public <T> Object serialize(T value) throws Exception {
        if (value == null) return null;

        Class<?> valueClass = value.getClass();
        ValueAdapter<P, ?, ?> adapter = adapters.get(valueClass);
        if (adapter == null) return value; // No adapters, try to return the original value

        if (adapter instanceof PrimitiveAdapter) {
            // If the value is adapted from a primitive type,
            // we should serialize it into object, then return.
            return adapter.serializeObject(provider, value);
        }

        // Otherwise, we need to serialize one by one.
        return serialize(adapter.serializeObject(provider, value));
    }


}
