package cc.carm.lib.configuration.adapter;

import cc.carm.lib.configuration.adapter.strandard.PrimitiveAdapters;
import cc.carm.lib.configuration.function.ConfigDataFunction;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ValueAdapterRegistry {

    protected final Map<Class<?>, ValueAdapter<?, ?>> adapters = new HashMap<>();

    public void register(@NotNull ValueAdapter<?, ?> adapter) {
        adapters.put(adapter.getValueClass(), adapter);
    }

    public <T> void register(Class<T> clazz, @NotNull ValueAdapter<?, T> adapter) {
        adapters.put(clazz, adapter);
    }

    public <B, V> void register(Class<B> baseClass, Class<V> valueClass,
                                ConfigDataFunction<B, V> parser,
                                ConfigDataFunction<V, B> serializer) {
        register(new ValueAdapter<B, V>(baseClass, valueClass) {
            @Override
            public B serialize(@NotNull ConfigurationProvider<?> provider, @NotNull V value) throws Exception {
                return serializer.parse(value);
            }

            @Override
            public V deserialize(@NotNull ConfigurationProvider<?> provider, @NotNull Class<? extends V> clazz, @NotNull B data) throws Exception {
                return parser.parse(data);
            }
        });
    }

    public void unregister(@NotNull Class<?> typeClass) {
        adapters.remove(typeClass);
    }

    @SuppressWarnings("unchecked")
    @Contract("_,_,null -> null")
    public <T> T deserialize(@NotNull ConfigurationProvider<?> provider, @NotNull Class<T> type, @Nullable Object source) throws Exception {
        if (source == null) return null; // Null check
        if (type.isInstance(source)) return type.cast(source); // Not required to deserialize

        ValueAdapter<?, ?> adapter = getAdapter(type);
        if (adapter == null) throw new RuntimeException("No adapter for type " + type.getName());

        // Check if value is adapted from given value's type
        if (adapter.isAdaptedFrom(source)) {
            return (T) adapter.deserializeObject(provider, type, source);
        }

        // Otherwise, we need to deserialize one by one.
        Object baseValue = deserialize(provider, adapter.getBaseClass(), source);
        if (baseValue == null) return null; // Null check

        return (T) adapter.deserializeObject(provider, type, baseValue);
    }

    @Contract("_,null -> null")
    public <T> Object serialize(@NotNull ConfigurationProvider<?> provider, @Nullable T value) throws Exception {
        if (value == null) return null; // Null check

        ValueAdapter<?, ?> adapter = getAdapter(value.getClass());
        if (adapter == null) return value; // No adapters, try to return the original value

        if (adapter instanceof PrimitiveAdapters) {
            // If the value is adapted from a primitive type,
            // we should serialize it into object, then return.
            return adapter.serializeObject(provider, value);
        }

        // Otherwise, we need to serialize one by one.
        return serialize(provider, adapter.serializeObject(provider, value));
    }

    public ValueAdapter<?, ?> getAdapter(Class<?> clazz) {
        return adapters.getOrDefault(clazz, findAdapter(clazz));
    }

    public ValueAdapter<?, ?> findAdapter(Class<?> clazz) {
        return adapters.values().stream().filter(adapter -> adapter.isAdaptedTo(clazz)).findFirst().orElse(null);
    }


}
