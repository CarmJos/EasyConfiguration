package cc.carm.lib.configuration.function;


import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@FunctionalInterface
public interface ConfigValueParser<T, R> {

    @Nullable R parse(@NotNull ConfigurationProvider<?> provider,
                      @NotNull T data, @Nullable R defaultValue) throws Exception;

    default <V> ConfigValueParser<T, V> andThen(@NotNull ConfigValueParser<R, V> after) {
        Objects.requireNonNull(after);
        return ((provider, data, defaultValue) -> {
            R result = parse(provider, data, null);
            if (result == null) return defaultValue;
            else return after.parse(provider, result, defaultValue);
        });
    }

    default <V> ConfigValueParser<V, R> compose(@NotNull ConfigValueParser<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return ((provider, data, defaultValue) -> {
            T result = before.parse(provider, data, null);
            if (result == null) return null;
            return parse(provider, result, defaultValue);
        });
    }

    default <V> ConfigValueParser<V, R> compose(@NotNull ConfigDataFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return ((provider, data, defaultValue) -> {
            T result = before.parse(data);
            return parse(provider, result, defaultValue);
        });
    }

    @Contract(pure = true)
    static <T> @NotNull ConfigValueParser<T, T> identity() {
        return (provider, input, defaultValue) -> input;
    }

    @Contract(pure = true)
    static <T> @NotNull ConfigValueParser<T, Object> toObject() {
        return (provider, input, defaultValue) -> input;
    }

    @Contract(pure = true)
    static <T, V> @NotNull ConfigValueParser<T, V> required() {
        return (provider, input, defaultValue) -> {
            throw new IllegalArgumentException("Please specify the value parser.");
        };
    }

}


