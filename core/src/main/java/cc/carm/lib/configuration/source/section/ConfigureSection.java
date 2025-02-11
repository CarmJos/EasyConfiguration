package cc.carm.lib.configuration.source.section;

import cc.carm.lib.configuration.function.DataFunction;
import cc.carm.lib.configuration.source.option.StandardOptions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public interface ConfigureSection {

    @NotNull ConfigureSource<?, ?, ?> source();

    default char separator() {
        return source().holder().options().get(StandardOptions.PATH_SEPARATOR);
    }

    @NotNull
    default Set<String> getKeys(boolean deep) {
        return getValues(deep).keySet();
    }

    @NotNull
    Map<String, Object> getValues(boolean deep);

    void set(@NotNull String path, @Nullable Object value);

    boolean contains(@NotNull String path);

    default <T> boolean isType(@NotNull String path, @NotNull Class<T> typeClass) {
        return typeClass.isInstance(get(path));
    }

    boolean isList(@NotNull String path);

    @Nullable List<?> getList(@NotNull String path);

    boolean isSection(@NotNull String path);

    @Nullable
    ConfigureSection getSection(@NotNull String path);

    @Nullable Object get(@NotNull String path);

    default @Nullable <T> T get(@NotNull String path, @NotNull Class<T> clazz) {
        return get(path, null, clazz);
    }

    default @Nullable <T> T get(@NotNull String path, @NotNull DataFunction<Object, T> parser) {
        return get(path, null, parser);
    }

    @Contract("_,!null,_->!null")
    default @Nullable <T> T get(@NotNull String path, @Nullable T defaultValue, @NotNull Class<T> clazz) {
        return get(path, defaultValue, DataFunction.castObject(clazz));
    }

    @Contract("_,!null,_->!null")
    default @Nullable <T> T get(@NotNull String path, @Nullable T defaultValue,
                                @NotNull DataFunction<Object, T> parser) {
        Object value = get(path);
        if (value != null) {
            try {
                return parser.handle(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }


    default boolean isBoolean(@NotNull String path) {
        return isType(path, Boolean.class);
    }

    default boolean getBoolean(@NotNull String path) {
        return getBoolean(path, false);
    }

    @Contract("_, !null -> !null")
    default @Nullable Boolean getBoolean(@NotNull String path, @Nullable Boolean def) {
        return get(path, def, DataFunction.booleanValue());
    }

    default @Nullable Boolean isByte(@NotNull String path) {
        return isType(path, Byte.class);
    }

    default @Nullable Byte getByte(@NotNull String path) {
        return getByte(path, (byte) 0);
    }

    @Contract("_, !null -> !null")
    default @Nullable Byte getByte(@NotNull String path, @Nullable Byte def) {
        return get(path, def, DataFunction.byteValue());
    }

    default boolean isShort(@NotNull String path) {
        return isType(path, Short.class);
    }

    default @Nullable Short getShort(@NotNull String path) {
        return getShort(path, (short) 0);
    }

    @Contract("_, !null -> !null")
    default @Nullable Short getShort(@NotNull String path, @Nullable Short def) {
        return get(path, def, DataFunction.shortValue());
    }


    default boolean isInt(@NotNull String path) {
        return isType(path, Integer.class);
    }

    default @Nullable Integer getInt(@NotNull String path) {
        return getInt(path, 0);
    }

    @Contract("_, !null -> !null")
    default @Nullable Integer getInt(@NotNull String path, @Nullable Integer def) {
        return get(path, def, DataFunction.intValue());
    }


    default boolean isLong(@NotNull String path) {
        return isType(path, Long.class);
    }

    default @Nullable Long getLong(@NotNull String path) {
        return getLong(path, 0L);
    }

    @Contract("_, !null -> !null")
    default @Nullable Long getLong(@NotNull String path, @Nullable Long def) {
        return get(path, def, DataFunction.longValue());
    }


    default boolean isFloat(@NotNull String path) {
        return isType(path, Float.class);
    }

    default @Nullable Float getFloat(@NotNull String path) {
        return getFloat(path, 0.0F);
    }

    @Contract("_, !null -> !null")
    default @Nullable Float getFloat(@NotNull String path, @Nullable Float def) {
        return get(path, def, DataFunction.floatValue());
    }


    default boolean isDouble(@NotNull String path) {
        return isType(path, Double.class);
    }

    default @Nullable Double getDouble(@NotNull String path) {
        return getDouble(path, 0.0D);
    }

    @Contract("_, !null -> !null")
    default @Nullable Double getDouble(@NotNull String path, @Nullable Double def) {
        return get(path, def, DataFunction.doubleValue());
    }


    default boolean isChar(@NotNull String path) {
        return isType(path, Boolean.class);
    }

    default @Nullable Character getChar(@NotNull String path) {
        return getChar(path, null);
    }

    @Contract("_, !null -> !null")
    default @Nullable Character getChar(@NotNull String path, @Nullable Character def) {
        return get(path, def, Character.class);
    }


    default boolean isString(@NotNull String path) {
        return isType(path, String.class);
    }

    default @Nullable String getString(@NotNull String path) {
        return getString(path, null);
    }

    @Contract("_, !null -> !null")
    default @Nullable String getString(@NotNull String path, @Nullable String def) {
        return get(path, def, String.class);
    }

    default <V> @NotNull List<V> getList(@NotNull String path, @NotNull DataFunction<Object, V> parser) {
        return parseList(getList(path), parser);
    }

    @Unmodifiable
    default @NotNull List<String> getStringList(@NotNull String path) {
        return getList(path, DataFunction.castToString());
    }

    @Unmodifiable
    default @NotNull List<Integer> getIntegerList(@NotNull String path) {
        return getList(path, DataFunction.intValue());
    }

    @Unmodifiable
    default @NotNull List<Long> getLongList(@NotNull String path) {
        return getList(path, DataFunction.longValue());
    }

    @Unmodifiable
    default @NotNull List<Double> getDoubleList(@NotNull String path) {
        return getList(path, DataFunction.doubleValue());
    }

    @Unmodifiable
    default @NotNull List<Float> getFloatList(@NotNull String path) {
        return getList(path, DataFunction.floatValue());
    }

    @Unmodifiable
    default @NotNull List<Byte> getByteList(@NotNull String path) {
        return getList(path, DataFunction.byteValue());
    }

    @Unmodifiable
    default @NotNull List<Character> getCharList(@NotNull String path) {
        return getList(path, DataFunction.castObject(Character.class));
    }

    @Unmodifiable
    static <T> @NotNull List<T> parseList(@Nullable List<?> list, DataFunction<Object, T> parser) {
        if (list == null) return Collections.emptyList();
        List<T> values = new ArrayList<>();
        for (Object o : list) {
            try {
                values.add(parser.handle(o));
            } catch (Exception ignored) {
            }
        }
        return values;
    }

}
