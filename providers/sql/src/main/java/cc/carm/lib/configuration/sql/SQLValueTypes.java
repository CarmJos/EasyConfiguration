package cc.carm.lib.configuration.sql;

import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

interface SQLValueTypes {

    SQLValueResolver<String> STRING = SQLValueResolver.of(0, String.class, ConfigDataFunction.identity());
    SQLValueResolver<Byte> BYTE = SQLValueResolver.of(1, Byte.class, Byte::parseByte);
    SQLValueResolver<Short> SHORT = SQLValueResolver.of(2, Short.class, Short::parseShort);
    SQLValueResolver<Integer> INTEGER = SQLValueResolver.of(3, Integer.class, Integer::parseInt);
    SQLValueResolver<Long> LONG = SQLValueResolver.of(4, Long.class, Long::parseLong);
    SQLValueResolver<Float> FLOAT = SQLValueResolver.of(5, Float.class, Float::parseFloat);
    SQLValueResolver<Double> DOUBLE = SQLValueResolver.of(6, Double.class, Double::parseDouble);
    SQLValueResolver<Boolean> BOOLEAN = SQLValueResolver.of(7, Boolean.class, Boolean::parseBoolean);
    SQLValueResolver<Character> CHAR = SQLValueResolver.of(8, Character.class, s -> s.charAt(0));

    @SuppressWarnings("rawtypes")
    SQLValueResolver<List> LIST = SQLValueResolver.of(10, List.class,
            array -> SQLConfigProvider.GSON.fromJson(array, List.class),
            list -> SQLConfigProvider.GSON.toJson(list)
    );

    @SuppressWarnings("rawtypes")
    SQLValueResolver<Map> SECTION = SQLValueResolver.of(20, Map.class,
            section -> SQLConfigProvider.GSON.fromJson(section, LinkedHashMap.class),
            section -> SQLConfigProvider.GSON.toJson(section)
    );

    static @NotNull SQLValueResolver<?>[] values() {
        return new SQLValueResolver<?>[]{
                STRING, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, BOOLEAN, CHAR, LIST, SECTION
        };
    }

    static SQLValueResolver<?> valueOf(int index) {
        return values()[index];
    }

    static @Nullable SQLValueResolver<?> get(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(null);
    }

    static @Nullable SQLValueResolver<?> get(Class<?> typeClazz) {
        return Arrays.stream(values()).filter(v -> v.isTypeOf(typeClazz)).findFirst().orElse(null);
    }

}
