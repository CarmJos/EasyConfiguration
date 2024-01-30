package cc.carm.lib.configuration.adapter.strandard;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.function.ConfigDataFunction;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

public abstract class PrimitiveAdapters<T> extends ValueAdapter<Object, T> {

    public static PrimitiveAdapters<String> ofString() {
        return of(String.class, o -> o instanceof String ? (String) o : o.toString());
    }

    public static PrimitiveAdapters<Boolean> ofBoolean() {
        return of(Boolean.class, o -> o instanceof Boolean ? (Boolean) o : Boolean.parseBoolean(o.toString()));
    }

    public static PrimitiveAdapters<Character> ofCharacter() {
        return of(Character.class, o -> o instanceof Character ? (Character) o : o.toString().charAt(0));
    }

    public static PrimitiveAdapters<Integer> ofInteger() {
        return ofNumber(Integer.class, Number::intValue, Integer::parseInt);
    }

    public static PrimitiveAdapters<Long> ofLong() {
        return ofNumber(Long.class, Number::longValue, Long::parseLong);
    }

    public static PrimitiveAdapters<Double> ofDouble() {
        return ofNumber(Double.class, Number::doubleValue, Double::parseDouble);
    }

    public static PrimitiveAdapters<Float> ofFloat() {
        return ofNumber(Float.class, Number::floatValue, Float::parseFloat);
    }

    public static PrimitiveAdapters<Short> ofShort() {
        return ofNumber(Short.class, Number::shortValue, Short::parseShort);
    }

    public static PrimitiveAdapters<Byte> ofByte() {
        return ofNumber(Byte.class, Number::byteValue, Byte::parseByte);
    }

    protected PrimitiveAdapters(Class<T> valueType) {
        super(Object.class, valueType);
    }

    @Override
    public Object serialize(@NotNull ConfigurationProvider<?> provider, @NotNull T value) throws Exception {
        return value;
    }

    public static <T> PrimitiveAdapters<T> of(@NotNull Class<T> clazz,
                                              @NotNull ConfigDataFunction<Object, T> function) {
        return new PrimitiveAdapters<T>(clazz) {
            @Override
            public T deserialize(@NotNull ConfigurationProvider<?> provider, @NotNull Class<? extends T> clazz, @NotNull Object data) throws Exception {
                return function.parse(data);
            }
        };
    }

    public static <T extends Number> PrimitiveAdapters<T> ofNumber(@NotNull Class<T> numberClass,
                                                                   @NotNull ConfigDataFunction<Number, T> castFunction,
                                                                   @NotNull ConfigDataFunction<String, T> parseFunction) {
        return of(numberClass, o -> o instanceof Number ? castFunction.parse((Number) o) : parseFunction.parse(o.toString()));
    }
}
