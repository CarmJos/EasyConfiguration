package cc.carm.lib.configuration.adapter.strandard;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

public abstract class PrimitiveAdapters<P extends ConfigurationProvider, T> extends ValueAdapter<P, Object, T> {

    public static <P extends ConfigurationProvider> PrimitiveAdapters<P, String> ofString() {
        return of(String.class, o -> o instanceof String ? (String) o : o.toString());
    }

    public static <P extends ConfigurationProvider> PrimitiveAdapters<P, Boolean> ofBoolean() {
        return of(Boolean.class, o -> o instanceof Boolean ? (Boolean) o : Boolean.parseBoolean(o.toString()));
    }

    public static <P extends ConfigurationProvider> PrimitiveAdapters<P, Character> ofCharacter() {
        return of(Character.class, o -> o instanceof Character ? (Character) o : o.toString().charAt(0));
    }

    public static <P extends ConfigurationProvider> PrimitiveAdapters<P, Integer> ofInteger() {
        return ofNumber(Integer.class, Number::intValue, Integer::parseInt);
    }

    public static <P extends ConfigurationProvider> PrimitiveAdapters<P, Long> ofLong() {
        return ofNumber(Long.class, Number::longValue, Long::parseLong);
    }

    public static <P extends ConfigurationProvider> PrimitiveAdapters<P, Double> ofDouble() {
        return ofNumber(Double.class, Number::doubleValue, Double::parseDouble);
    }

    public static <P extends ConfigurationProvider> PrimitiveAdapters<P, Float> ofFloat() {
        return ofNumber(Float.class, Number::floatValue, Float::parseFloat);
    }

    public static <P extends ConfigurationProvider> PrimitiveAdapters<P, Short> ofShort() {
        return ofNumber(Short.class, Number::shortValue, Short::parseShort);
    }

    public static <P extends ConfigurationProvider> PrimitiveAdapters<P, Byte> ofByte() {
        return ofNumber(Byte.class, Number::byteValue, Byte::parseByte);
    }

    protected PrimitiveAdapters(Class<T> valueType) {
        super(Object.class, valueType);
    }

    @Override
    public Object serialize(@NotNull P provider, @NotNull T value) throws Exception {
        return value;
    }

    public static <P extends ConfigurationProvider, T> PrimitiveAdapters<P, T> of(@NotNull Class<T> clazz,
                                                                                  @NotNull ConfigDataFunction<Object, T> function) {
        return new PrimitiveAdapters<P, T>(clazz) {
            @Override
            public T deserialize(@NotNull P provider, @NotNull Class<? extends T> clazz, @NotNull Object data) throws Exception {
                return function.parse(data);
            }
        };
    }

    public static <P extends ConfigurationProvider, T extends Number> PrimitiveAdapters<P, T> ofNumber(@NotNull Class<T> numberClass,
                                                                                                       @NotNull ConfigDataFunction<Number, T> castFunction,
                                                                                                       @NotNull ConfigDataFunction<String, T> parseFunction) {
        return of(numberClass, o -> o instanceof Number ? castFunction.parse((Number) o) : parseFunction.parse(o.toString()));
    }
}
