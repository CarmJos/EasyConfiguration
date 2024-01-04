package cc.carm.lib.configuration.core.function;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

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
                if (Long.class.isAssignableFrom(valueClass) || long.class.isAssignableFrom(valueClass)) {
                    input = longValue().parse(input, (Long) defaultValue);
                } else if (Integer.class.isAssignableFrom(valueClass) || int.class.isAssignableFrom(valueClass)) {
                    input = intValue().parse(input, (Integer) defaultValue);
                } else if (Float.class.isAssignableFrom(valueClass) || float.class.isAssignableFrom(valueClass)) {
                    input = floatValue().parse(input, (Float) defaultValue);
                } else if (Double.class.isAssignableFrom(valueClass) || double.class.isAssignableFrom(valueClass)) {
                    input = doubleValue().parse(input, (Double) defaultValue);
                } else if (Byte.class.isAssignableFrom(valueClass) || byte.class.isAssignableFrom(valueClass)) {
                    input = byteValue().parse(input, (Byte) defaultValue);
                } else if (Short.class.isAssignableFrom(valueClass) || short.class.isAssignableFrom(valueClass)) {
                    input = shortValue().parse(input, (Short) defaultValue);
                }
            } else if (Boolean.class.isAssignableFrom(valueClass) || boolean.class.isAssignableFrom(valueClass)) {
                input = booleanValue().parse(input, (Boolean) defaultValue);
            } else if (Enum.class.isAssignableFrom(valueClass) && input instanceof String) {
                String enumName = (String) input;
                input = valueClass.getDeclaredMethod("valueOf", String.class).invoke(null, enumName);
            } else if (UUID.class.isAssignableFrom(valueClass) && input instanceof String) {
                input = UUID.fromString((String) input);
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
    static @NotNull ConfigValueParser<Object, Integer> intValue() {
        return (input, defaultValue) -> ConfigDataFunction.intValue().parse(input);
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Short> shortValue() {
        return (input, defaultValue) -> ConfigDataFunction.shortValue().parse(input);
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Double> doubleValue() {
        return (input, defaultValue) -> ConfigDataFunction.doubleValue().parse(input);
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Byte> byteValue() {
        return (input, defaultValue) -> ConfigDataFunction.byteValue().parse(input);
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Float> floatValue() {
        return (input, defaultValue) -> ConfigDataFunction.floatValue().parse(input);
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Long> longValue() {
        return (input, defaultValue) -> ConfigDataFunction.longValue().parse(input);
    }

    @Contract(pure = true)
    static @NotNull ConfigValueParser<Object, Boolean> booleanValue() {
        return (input, defaultValue) -> ConfigDataFunction.booleanValue().parse(input);
    }

    @Contract(pure = true)
    static @NotNull <E extends Enum<E>> ConfigValueParser<Object, E> enumValue(Class<E> enumClass) {
        return (input, defaultValue) -> {
            if (input instanceof Enum) {
                return enumClass.cast(input);
            } else if (input instanceof String) {
                return Enum.valueOf(enumClass, (String) input);
            } else if (input instanceof Number) {
                return enumClass.getEnumConstants()[((Number) input).intValue()];
            } else {
                throw new IllegalArgumentException("Cannot cast value to " + enumClass.getName());
            }
        };
    }

}


