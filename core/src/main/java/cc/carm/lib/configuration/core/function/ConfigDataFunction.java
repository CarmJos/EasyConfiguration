package cc.carm.lib.configuration.core.function;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@FunctionalInterface
public interface ConfigDataFunction<T, R> {

    @NotNull R parse(@NotNull T data) throws Exception;

    default <V> @NotNull ConfigDataFunction<T, V> andThen(@NotNull ConfigDataFunction<? super R, V> after) {
        Objects.requireNonNull(after);
        return ((data) -> after.parse(parse(data)));
    }

    @Contract(pure = true)
    static <T> @NotNull ConfigDataFunction<T, T> identity() {
        return (input) -> input;
    }

    @Contract(pure = true)
    static <T> @NotNull ConfigDataFunction<T, T> identity(Class<T> type) {
        return (input) -> input;
    }

    @Contract(pure = true)
    static <T, V> @NotNull ConfigDataFunction<T, V> required() {
        return (input) -> {
            throw new IllegalArgumentException("Please specify the value parser.");
        };
    }

    @Contract(pure = true)
    static <T> @NotNull ConfigDataFunction<T, Object> toObject() {
        return (input) -> input;
    }

    @Contract(pure = true)
    static <V> @NotNull ConfigDataFunction<Object, V> castObject(Class<V> valueClass) {
        return (input) -> {
            if (valueClass.isInstance(input)) return valueClass.cast(input);
            else throw new IllegalArgumentException("Cannot cast value to " + valueClass.getName());
        };
    }

    @Contract(pure = true)
    static <V> @NotNull ConfigDataFunction<String, V> castFromString(Class<V> valueClass) {
        return (input) -> {
            if (valueClass.isInstance(input)) return valueClass.cast(input);
            else throw new IllegalArgumentException("Cannot cast string to " + valueClass.getName());
        };
    }

    @Contract(pure = true)
    static <T> @NotNull ConfigDataFunction<T, String> castToString() {
        return (input) -> {
            if (input instanceof String) return (String) input;
            else return input.toString();
        };
    }


}


