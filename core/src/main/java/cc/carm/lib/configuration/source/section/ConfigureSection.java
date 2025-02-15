package cc.carm.lib.configuration.source.section;

import cc.carm.lib.configuration.function.DataFunction;
import cc.carm.lib.configuration.source.option.StandardOptions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface ConfigureSection {

    @NotNull ConfigureSource<?, ?, ?> source();

    @Nullable ConfigureSection parent();

    default char separator() {
        return source().holder().options().get(StandardOptions.PATH_SEPARATOR);
    }

    @NotNull
    @UnmodifiableView
    default Set<String> getKeys(boolean deep) {
        return getValues(deep).keySet();
    }

    @NotNull
    @UnmodifiableView
    Map<String, Object> getValues(boolean deep);

    void set(@NotNull String path, @Nullable Object value);

    boolean contains(@NotNull String path);

    default <T> boolean isType(@NotNull String path, @NotNull Class<T> typeClass) {
        return typeClass.isInstance(get(path));
    }

    default boolean isList(@NotNull String path) {
        return isType(path, List.class);
    }

    @Nullable List<?> getList(@NotNull String path);

    default boolean isSection(@NotNull String path) {
        return isType(path, ConfigureSection.class);
    }

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

    /**
     * Get a list of values from the section
     * <p>
     * If the path does not exist, an empty list will be returned
     * <br>Any changes please use {@link #set(String, Object)} after changes
     *
     * @param path   The path to get the list from
     * @param parser The function to parse the values
     * @param <V>    The type of the values
     * @return The list of values
     */
    default <V> @NotNull List<V> getList(@NotNull String path, @NotNull DataFunction<Object, V> parser) {
        return getCollection(path, ArrayList::new, parser);
    }

    /**
     * Get a list of strings from the section
     * <p> Limitations see {@link #getList(String, DataFunction)}
     *
     * @param path The path to get the list from
     * @return The list of strings
     */
    default @NotNull List<String> getStringList(@NotNull String path) {
        return getList(path, DataFunction.castToString());
    }

    /**
     * Get a list of integer from the section
     * <p> Limitations see {@link #getList(String, DataFunction)}
     *
     * @param path The path to get the list from
     * @return The list of int values
     */
    default @NotNull List<Integer> getIntegerList(@NotNull String path) {
        return getList(path, DataFunction.intValue());
    }

    /**
     * Get a list of long from the section
     * <p> Limitations see {@link #getList(String, DataFunction)}
     *
     * @param path The path to get the list from
     * @return The list of long values
     */
    default @NotNull List<Long> getLongList(@NotNull String path) {
        return getList(path, DataFunction.longValue());
    }

    /**
     * Get a list of double from the section
     * <p> Limitations see {@link #getList(String, DataFunction)}
     *
     * @param path The path to get the list from
     * @return The list of doubles
     */
    default @NotNull List<Double> getDoubleList(@NotNull String path) {
        return getList(path, DataFunction.doubleValue());
    }

    /**
     * Get a list of floats from the section
     * <p> Limitations see {@link #getList(String, DataFunction)}
     *
     * @param path The path to get the list from
     * @return The list of floats
     */
    default @NotNull List<Float> getFloatList(@NotNull String path) {
        return getList(path, DataFunction.floatValue());
    }

    /**
     * Get a list of bytes from the section
     * <p> Limitations see {@link #getList(String, DataFunction)}
     *
     * @param path The path to get the list from
     * @return The list of bytes
     */
    default @NotNull List<Byte> getByteList(@NotNull String path) {
        return getList(path, DataFunction.byteValue());
    }

    /**
     * Get a list of char from the section
     * <p> Limitations see {@link #getList(String, DataFunction)}
     *
     * @param path The path to get the list from
     * @return The list of char
     */
    default @NotNull List<Character> getCharList(@NotNull String path) {
        return getList(path, DataFunction.castObject(Character.class));
    }

    default <T, C extends Collection<T>> @NotNull C getCollection(@NotNull String path,
                                                                  @NotNull Supplier<C> constructor,
                                                                  @NotNull DataFunction<Object, T> parser) {
        return parseCollection(getList(path), constructor, parser);
    }

    default @NotNull Stream<?> stream(@NotNull String path) {
        List<?> values = getList(path);
        return values == null ? Stream.empty() : values.stream();
    }

    default <T> @NotNull Stream<T> stream(@NotNull String path, @NotNull Function<Object, T> parser) {
        return stream(path).map(parser);
    }

    static <T, C extends Collection<T>> @NotNull C parseCollection(
            @Nullable List<?> data, @NotNull Supplier<C> constructor,
            @NotNull DataFunction<Object, T> parser
    ) {
        C values = constructor.get();
        if (data == null) return values;
        for (Object obj : data) {
            try {
                values.add(parser.handle(obj));
            } catch (Exception ignored) {
            }
        }
        return values;
    }
}
