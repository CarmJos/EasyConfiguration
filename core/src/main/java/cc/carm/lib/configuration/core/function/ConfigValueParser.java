package cc.carm.lib.configuration.core.function;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@FunctionalInterface
public interface ConfigValueParser<T, R> {

    @Nullable R parse(@NotNull T data, @Nullable R defaultValue) throws Exception;

    default <V> ConfigValueParser<T, V> andThen(@NotNull ConfigValueParser<R, V> after) {
        Objects.requireNonNull(after);
        return ((data, defaultValue) -> {
            R result = parse(data, null);
            if (result == null) return defaultValue;
            else return after.parse(result, defaultValue);
        });
    }

    default <V> ConfigValueParser<V, R> compose(@NotNull ConfigDataFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return ((data, defaultValue) -> {
            T result = before.parse(data);
            return parse(result, defaultValue);
        });
    }


    @Contract(pure = true)
    static <T> @NotNull ConfigValueParser<T, T> identity() {
        return (input, defaultValue) -> input;
    }

    @Contract(pure = true)
    static <T> @NotNull ConfigValueParser<T, Object> toObject() {
        return (input, defaultValue) -> input;
    }

    @Contract(pure = true)
    static <T, V> @NotNull ConfigValueParser<T, V> required() {
        return (input, defaultValue) -> {
            throw new IllegalArgumentException("Please specify the value parser.");
        };
    }

    @Contract(pure = true)
    static <V> @NotNull ConfigValueParser<Object, V> castObject(Class<V> valueClass) {
        return (input, defaultValue) -> {

            if (Number.class.isAssignableFrom(valueClass)) {
                if (Long.class.isAssignableFrom(valueClass)) {
                    input = longValue().parse(input, null);
                } else if (Integer.class.isAssignableFrom(valueClass)) {
                    input = intValue().parse(input, null);
                } else if (Float.class.isAssignableFrom(valueClass)) {
                    input = floatValue().parse(input, null);
                } else if (Double.class.isAssignableFrom(valueClass)) {
                    input = doubleValue().parse(input, null);
                } else if (Byte.class.isAssignableFrom(valueClass)) {
                    input = byteValue().parse(input, null);
                } else if (Short.class.isAssignableFrom(valueClass)) {
                    input = shortValue().parse(input, null);
                }
            }

            if (valueClass.isInstance(input)) return valueClass.cast(input);
            else throw new IllegalArgumentException("Cannot cast value to " + valueClass.getName());
        };
    }

    @Contract(pure = true)
    static <V> @NotNull ConfigValueParser<String, V> parseString(Class<V> valueClass) {
        return (input, defaultValue) -> {
            if (valueClass.isInstance(input)) return valueClass.cast(input);
            else throw new IllegalArgumentException("Cannot cast string to " + valueClass.getName());
        };
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, String> castToString() {
        return (input, defaultValue) -> {
            if (input instanceof String) return (String) input;
            else return input.toString();
        };
    }

    @Contract(pure = true)
    static @NotNull <T> ConfigValueParser<T, String> castToString(Class<T> clazz) {
        return (input, defaultValue) -> {
            if (input instanceof String) return (String) input;
            else return input.toString();
        };
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Integer> intValue() {
        return (input, defaultValue) -> {
            if (input instanceof Integer) {
                return (Integer) input;
            } else if (input instanceof Number) {
                return ((Number) input).intValue();
            } else throw new IllegalArgumentException("Cannot cast value to " + Integer.class.getName());
        };
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Short> shortValue() {
        return (input, defaultValue) -> {
            if (input instanceof Short) {
                return (Short) input;
            } else if (input instanceof Number) {
                return ((Number) input).shortValue();
            } else throw new IllegalArgumentException("Cannot cast value to " + Short.class.getName());
        };
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Double> doubleValue() {
        return (input, defaultValue) -> {
            if (input instanceof Double) {
                return (Double) input;
            } else if (input instanceof Number) {
                return ((Number) input).doubleValue();
            } else throw new IllegalArgumentException("Cannot cast value to " + Double.class.getName());
        };
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Byte> byteValue() {
        return (input, defaultValue) -> {
            if (input instanceof Byte) {
                return (Byte) input;
            } else if (input instanceof Number) {
                return ((Number) input).byteValue();
            } else throw new IllegalArgumentException("Cannot cast value to " + Byte.class.getName());
        };
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Float> floatValue() {
        return (input, defaultValue) -> {
            if (input instanceof Float) {
                return (Float) input;
            } else if (input instanceof Number) {
                return ((Number) input).floatValue();
            } else throw new IllegalArgumentException("Cannot cast value to " + Float.class.getName());
        };
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Long> longValue() {
        return (input, defaultValue) -> {
            if (input instanceof Long) {
                return (Long) input;
            } else if (input instanceof Number) {
                return ((Number) input).longValue();
            } else throw new IllegalArgumentException("Cannot cast value to " + Long.class.getName());
        };
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Boolean> booleanValue() {
        return (input, defaultValue) -> {
            if (input instanceof Boolean) {
                return (Boolean) input;
            } else if (input instanceof String) {
                String s = (String) input;
                return Boolean.parseBoolean(s) || "yes".equalsIgnoreCase(s);
            } else if (input instanceof Integer) {
                return ((Integer) input) == 1;
            } else throw new IllegalArgumentException("Cannot cast value to " + Boolean.class.getName());
        };
    }

}


