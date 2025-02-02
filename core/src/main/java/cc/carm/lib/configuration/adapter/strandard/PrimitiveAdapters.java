package cc.carm.lib.configuration.adapter.strandard;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueParser;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.function.ConfigDataFunction;
import org.jetbrains.annotations.NotNull;

public class PrimitiveAdapters<T> extends ValueAdapter<T> {

    public static final PrimitiveAdapters<?>[] ADAPTERS = new PrimitiveAdapters[]{
            ofString(), ofBoolean(), ofBooleanType(), ofCharacter(), ofCharacterType(),
            ofInteger(), ofIntegerType(), ofLong(), ofLongType(), ofDouble(), ofDoubleType(),
            ofFloat(), ofFloatType(), ofShort(), ofShortType(), ofByte(), ofByteType()
    };

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static ValueAdapter<Enum<?>> ofEnum() {
        ValueAdapter<Enum<?>> adapter = new ValueAdapter<>(new ValueType<Enum<?>>() {
        });
        adapter.deserializer((provider, type, data) -> Enum.valueOf((Class<Enum>) type.getRawType(), data.toString()));
        adapter.serializer((provider, type, value) -> value.name());
        return adapter;
    }

    public static PrimitiveAdapters<String> ofString() {
        return of(String.class, o -> o instanceof String ? (String) o : o.toString());
    }

    public static PrimitiveAdapters<Boolean> ofBoolean() {
        return of(Boolean.class, o -> o instanceof Boolean ? (Boolean) o : Boolean.parseBoolean(o.toString()));
    }

    public static PrimitiveAdapters<Boolean> ofBooleanType() {
        return of(boolean.class, o -> o instanceof Boolean ? (Boolean) o : Boolean.parseBoolean(o.toString()));
    }

    public static PrimitiveAdapters<Character> ofCharacter() {
        return of(Character.class, o -> o instanceof Character ? (Character) o : o.toString().charAt(0));
    }

    public static PrimitiveAdapters<Character> ofCharacterType() {
        return of(char.class, o -> o instanceof Character ? (Character) o : o.toString().charAt(0));
    }

    public static PrimitiveAdapters<Integer> ofInteger() {
        return ofNumber(Integer.class, Number::intValue, Integer::parseInt);
    }

    public static PrimitiveAdapters<Integer> ofIntegerType() {
        return ofNumber(int.class, Number::intValue, Integer::parseInt);
    }

    public static PrimitiveAdapters<Long> ofLong() {
        return ofNumber(Long.class, Number::longValue, Long::parseLong);
    }

    public static PrimitiveAdapters<Long> ofLongType() {
        return ofNumber(long.class, Number::longValue, Long::parseLong);
    }

    public static PrimitiveAdapters<Double> ofDouble() {
        return ofNumber(Double.class, Number::doubleValue, Double::parseDouble);
    }

    public static PrimitiveAdapters<Double> ofDoubleType() {
        return ofNumber(double.class, Number::doubleValue, Double::parseDouble);
    }

    public static PrimitiveAdapters<Float> ofFloat() {
        return ofNumber(Float.class, Number::floatValue, Float::parseFloat);
    }

    public static PrimitiveAdapters<Float> ofFloatType() {
        return ofNumber(float.class, Number::floatValue, Float::parseFloat);
    }

    public static PrimitiveAdapters<Short> ofShort() {
        return ofNumber(Short.class, Number::shortValue, Short::parseShort);
    }

    public static PrimitiveAdapters<Short> ofShortType() {
        return ofNumber(short.class, Number::shortValue, Short::parseShort);
    }

    public static PrimitiveAdapters<Byte> ofByte() {
        return ofNumber(Byte.class, Number::byteValue, Byte::parseByte);
    }

    public static PrimitiveAdapters<Byte> ofByteType() {
        return ofNumber(byte.class, Number::byteValue, Byte::parseByte);
    }

    public static <T> PrimitiveAdapters<T> of(@NotNull Class<T> clazz,
                                              @NotNull ConfigDataFunction<Object, T> function) {
        return new PrimitiveAdapters<>(clazz, (p, type, data) -> function.handle(data));
    }

    public static <T extends Number> PrimitiveAdapters<T> ofNumber(@NotNull Class<T> numberClass,
                                                                   @NotNull ConfigDataFunction<Number, T> castFunction,
                                                                   @NotNull ConfigDataFunction<String, T> parseFunction) {
        return of(numberClass, o -> o instanceof Number ? castFunction.handle((Number) o) : parseFunction.handle(o.toString()));
    }

    protected PrimitiveAdapters(@NotNull Class<T> valueType, @NotNull ValueParser<T> deserializer) {
        super(ValueType.of(valueType), (provider, type, value) -> value, deserializer);
    }

}
