package cc.carm.lib.configuration.adapter;

import cc.carm.lib.configuration.function.ConfigDataFunction;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ValueAdapterRegistry {

    protected final Set<ValueAdapter<?>> adapters = new HashSet<>();

    public <FROM, TO> void register(@NotNull Class<FROM> from, @NotNull Class<TO> to,
                                    @Nullable ConfigDataFunction<FROM, TO> parser,
                                    @Nullable ConfigDataFunction<TO, FROM> serializer) {
        register(ValueType.of(from), ValueType.of(to), parser, serializer);
    }

    public <FROM, TO> void register(@NotNull ValueType<FROM> from, @NotNull ValueType<TO> to,
                                    @Nullable ConfigDataFunction<FROM, TO> parser,
                                    @Nullable ConfigDataFunction<TO, FROM> serializer) {
        ValueAdapter<FROM> fromAdapter = adapterOf(from);
        if (fromAdapter == null) throw new IllegalArgumentException("No adapter for type " + from);
        register(to,
                serializer == null ? null : (provider, type, value) -> fromAdapter.serialize(provider, from, serializer.handle(value)),
                parser == null ? null : (provider, type, data) -> parser.handle(fromAdapter.deserialize(provider, from, data))
        );
    }

    public void register(@NotNull ValueAdapter<?>... adapter) {
        adapters.addAll(Arrays.asList(adapter));
    }

    public <T> void register(@NotNull Class<T> type, @NotNull ValueSerializer<T> serializer) {
        register(ValueType.of(type), serializer);
    }

    public <T> void register(@NotNull ValueType<T> type, @NotNull ValueSerializer<T> serializer) {
        ValueAdapter<T> existing = adapterOf(type);
        if (existing != null) {
            existing.serializer(serializer);
        } else {
            register(new ValueAdapter<>(type, serializer, null));
        }
    }

    public <T> void register(@NotNull Class<T> type, @NotNull ValueParser<T> deserializer) {
        register(ValueType.of(type), deserializer);
    }

    public <T> void register(@NotNull ValueType<T> type, @NotNull ValueParser<T> deserializer) {
        ValueAdapter<T> existing = adapterOf(type);
        if (existing != null) {
            existing.deserializer(deserializer);
        } else {
            register(new ValueAdapter<>(type, null, deserializer));
        }
    }

    public <T> void register(@NotNull ValueType<T> type, @Nullable ValueSerializer<T> serializer, @Nullable ValueParser<T> deserializer) {
        if (serializer == null && deserializer == null) return;
        ValueAdapter<T> existing = adapterOf(type);
        if (existing != null) {
            if (serializer != null) existing.serializer(serializer);
            if (deserializer != null) existing.deserializer(deserializer);
        } else {
            register(new ValueAdapter<>(type, serializer, deserializer));
        }
    }

    public void unregister(@NotNull Class<?> type) {
        unregister(ValueType.of(type));
    }

    public void unregister(@NotNull ValueType<?> type) {
        adapters.removeIf(adapter -> adapter.type().equals(type));
    }

    @SuppressWarnings("unchecked")
    public <T> ValueAdapter<T> adapterOf(@NotNull ValueType<T> type) {
        ValueAdapter<?> matched = adapters.stream().filter(adapter -> adapter.type().equals(type)).findFirst().orElse(null);
        if (matched != null) return (ValueAdapter<T>) matched;

        // If no adapter found, try to find the adapter for the super type
        return (ValueAdapter<T>) adapters.stream()
                .filter(adapter -> adapter.type().isSubtypeOf(type))
                .findFirst().orElse(null);
    }

    public <T> ValueAdapter<T> adapterOf(@NotNull T value) {
        return adapterOf(ValueType.of(value));
    }

    public <T> ValueAdapter<T> adapterOf(@NotNull Class<T> type) {
        return adapterOf(ValueType.of(type));
    }

    @Contract("_,_,null -> null")
    public <T> T deserialize(@NotNull ConfigurationProvider<?> provider, @NotNull Class<T> type, @Nullable Object source) throws Exception {
        return deserialize(provider, ValueType.of(type), source);
    }

    @Contract("_,_,null -> null")
    public <T> T deserialize(@NotNull ConfigurationProvider<?> provider, @NotNull ValueType<T> type, @Nullable Object source) throws Exception {
        if (source == null) return null; // Null check
        if (type.isInstance(source)) return type.cast(source); // Not required to deserialize
        ValueAdapter<T> adapter = adapterOf(type);
        if (adapter == null) throw new RuntimeException("No adapter for type " + type);
        return adapter.deserialize(provider, type, source);
    }

    @Contract("_,null -> null")
    public <T> Object serialize(@NotNull ConfigurationProvider<?> provider, @Nullable T value) throws Exception {
        if (value == null) return null; // Null check
        ValueType<T> type = ValueType.of(value);
        ValueAdapter<T> adapter = adapterOf(type);
        if (adapter == null) return value; // No adapters, try to return the original value
        return adapter.serialize(provider, type, value);
    }


}
