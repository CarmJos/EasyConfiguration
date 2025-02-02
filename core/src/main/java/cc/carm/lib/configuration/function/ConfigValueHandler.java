package cc.carm.lib.configuration.function;


import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@FunctionalInterface
public interface ConfigValueHandler<T, R> {

    @Nullable R handle(@NotNull ConfigurationProvider<?> provider, @NotNull T data) throws Exception;

    default <V> ConfigValueHandler<T, V> andThen(@NotNull ConfigValueHandler<R, V> after) {
        Objects.requireNonNull(after);
        return ((provider, data) -> {
            R result = handle(provider, data);
            if (result == null) return null;
            else return after.handle(provider, result);
        });
    }

    default <V> ConfigValueHandler<V, R> compose(@NotNull ConfigValueHandler<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return ((provider, data) -> {
            T result = before.handle(provider, data);
            if (result == null) return null;
            return handle(provider, result);
        });
    }

    default <V> ConfigValueHandler<V, R> compose(@NotNull ConfigDataFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return ((provider, data) -> {
            T result = before.handle(data);
            return handle(provider, result);
        });
    }

    @Contract(pure = true)
    static <T> @NotNull ConfigValueHandler<T, T> identity() {
        return (provider, input) -> input;
    }

    @Contract(pure = true)
    static <T> @NotNull ConfigValueHandler<T, Object> toObject() {
        return ConfigurationProvider::serialize;
    }

    @Contract(pure = true)
    static <T> @NotNull ConfigValueHandler<Object, T> fromObject(ValueType<T> type) {
        return (provider, input) -> provider.deserialize(type, input);
    }


    @Contract(pure = true)
    static <T, V> @NotNull ConfigValueHandler<T, V> required() {
        return (provider, input) -> {
            throw new IllegalArgumentException("Please specify the value parser.");
        };
    }
}


