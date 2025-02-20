package cc.carm.lib.configuration.source.section;

import cc.carm.lib.configuration.function.DataFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents a section of a configuration.
 *
 * @author Carm
 * @since 4.0.0
 */
public interface ConfigureSection extends Cloneable {
    
    /**
     * Gets the parent section of this section.
     * <p>
     * For root sections, this will return null.
     *
     * @return Parent section, or null if this is a root section.
     */
    @Contract(pure = true)
    @Nullable ConfigureSection parent();

    /**
     * Gets a set containing all keys in this section.
     * <p>
     * If deep is set to true, then this will contain all the keys within any
     * child {@link ConfigureSection}s (and their children paths).
     * <p>
     * If deep is set to false, then this will contain only the keys of any
     * direct children, and not their own children.
     *
     * @param deep Whether to get a deep list.
     * @return Set of keys contained within this Section.
     */
    @NotNull
    @UnmodifiableView
    default Set<String> getKeys(boolean deep) {
        return getValues(deep).keySet();
    }

    /**
     * Gets a set containing all primary keys in this section.
     *
     * @return Set of keys contained within this Section.
     */
    @NotNull
    @UnmodifiableView
    default Set<String> keys() {
        return getKeys(false);
    }

    /**
     * Gets a set containing all values in this section.
     * <p>
     * If deep is set to true, then this will contain all the keys within any
     * child {@link ConfigureSection}s (and their children paths).
     * <p>
     * If deep is set to false, then this will contain only the keys of any
     * direct children, and not their own children.
     *
     * @param deep Whether to get a deep list.
     * @return Map of data values contained within this Section.
     */
    @NotNull
    @UnmodifiableView
    Map<String, Object> getValues(boolean deep);

    /**
     * Gets a set containing all key-values in this section.
     *
     * @return Map of data values contained within this Section.
     * @see #getValues(boolean)
     */
    @NotNull
    @UnmodifiableView
    default Map<String, Object> values() {
        return getValues(false);
    }

    /**
     * Create a stream of all values in this section.
     *
     * @return Stream of all values in this section.
     */
    default Stream<Map.Entry<String, Object>> stream() {
        return values().entrySet().stream();
    }

    /**
     * Iterates over all keys in this section.
     *
     * @param action The action to apply to each key.
     */
    default void forEach(@NotNull BiConsumer<String, Object> action) {
        values().forEach(action);
    }

    /**
     * Sets the value at the given path.
     * <p>
     * Null values will be kept, if you want to remove a value use {@link #remove(String)}
     * Path separator depends on holder's
     * {@link cc.carm.lib.configuration.source.option.StandardOptions#PATH_SEPARATOR}
     *
     * @param path  The path to set the value at.
     * @param value The value to set.
     */
    void set(@NotNull String path, @Nullable Object value);

    /**
     * Removes the value at the given path.
     * <p>
     * Path separator depends on holder's
     * {@link cc.carm.lib.configuration.source.option.StandardOptions#PATH_SEPARATOR}
     *
     * @param path The path to remove the value at.
     */
    void remove(@NotNull String path);

    /**
     * Check if the value of given path is present.
     * <p>
     * Path separator depends on holder's
     * {@link cc.carm.lib.configuration.source.option.StandardOptions#PATH_SEPARATOR}
     *
     * @param path The path to check.
     * @return True if the value is present, false otherwise.
     */
    boolean contains(@NotNull String path);

    /**
     * Predicate the value of given path is specific type.
     *
     * @param path      The path to check.
     * @param typeClass The type's class
     * @param <T>       The type to check.
     * @return True if the value is present and is the correct type, false otherwise.
     */
    default <T> boolean isType(@NotNull String path, @NotNull Class<T> typeClass) {
        return typeClass.isInstance(get(path));
    }

    /**
     * Predicate the value of given path is a  {@link List}.
     *
     * @param path The path to check.
     * @return True if the value is present and is a list, false otherwise.
     */
    default boolean isList(@NotNull String path) {
        return isType(path, List.class);
    }

    /**
     * Get the value as a {@link List} from the specified path.
     *
     * @param path The path to get the  {@link List}.
     * @return The list if the path exists and is a list, otherwise null.
     */
    @Nullable List<?> getList(@NotNull String path);

    /**
     * Predicate the value of given path is a {@link ConfigureSection}.
     *
     * @param path The path to check.
     * @return True if the value is present and is a section, false otherwise.
     */
    default boolean isSection(@NotNull String path) {
        return isType(path, ConfigureSection.class);
    }

    /**
     * Get the value as a {@link ConfigureSection} from the specified path.
     *
     * @param path The path to get the section.
     * @return The section if the path exists and is a section, otherwise null.
     */
    @Nullable
    ConfigureSection getSection(@NotNull String path);

    /**
     * Get the origin value of the path.
     *
     * @param path The path to get the value from.
     * @return The value at the path, or null if not found.
     */
    @Nullable Object get(@NotNull String path);

    /**
     * Get the value of the path for specific type,
     * if the path does not exist, return null.
     *
     * @param path The path to get the value from.
     * @param type The type class of the value
     * @param <T>  The type of the value
     * @return The value at the path, or the default value if not found.
     */
    default @Nullable <T> T get(@NotNull String path, @NotNull Class<T> type) {
        return get(path, null, type);
    }

    /**
     * Get the value of the path using a parser function,
     * if the path does not exist, return NULL.
     *
     * @param path   The path to get the value from.
     * @param parser The function to parse the value
     * @param <T>    The type of the value
     * @return The value at the path, or null if not found.
     */
    default @Nullable <T> T get(@NotNull String path, @NotNull DataFunction<@Nullable Object, T> parser) {
        return get(path, null, parser);
    }

    /**
     * Get the value of the path for specific type,
     * if the path does not exist, return the default value.
     *
     * @param path     The path to get the value from.
     * @param defaults The default value to return if the path does not exist.
     * @param clazz    The type class of the value
     * @param <T>      The type of the value
     * @return The value at the path, or the default value if not found.
     */
    @Contract("_,!null,_->!null")
    default @Nullable <T> T get(@NotNull String path, @Nullable T defaults, @NotNull Class<T> clazz) {
        return get(path, defaults, DataFunction.castObject(clazz));
    }

    /**
     * Get the value of the path using a parser function,
     * if the path does not exist, return the default value.
     *
     * @param path         The path to get the value from.
     * @param defaultValue The default value to return if the path does not exist.
     * @param parser       The function to parse the value
     * @param <T>          The type of the value
     * @return The value at the path, or the default value if not found.
     */
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

    /**
     * Predicate the value of given path is a {@link Boolean}.
     *
     * @param path The path to check.
     * @return True if the value is present and is a boolean, false otherwise.
     */
    default boolean isBoolean(@NotNull String path) {
        return isType(path, Boolean.class);
    }

    /**
     * Get the value as a {@link Boolean} from the specified path.
     *
     * @param path The path to get the boolean.
     * @return The boolean if the path exists and is a boolean, otherwise false.
     */
    default boolean getBoolean(@NotNull String path) {
        return getBoolean(path, false);
    }

    /**
     * Get the value as a {@link Boolean} from the specified path.
     *
     * @param path The path to get the boolean.
     * @param def  The default value to return if the path does not exist.
     * @return The boolean if the path exists and is a boolean, otherwise the default value.
     */
    @Contract("_, !null -> !null")
    default @Nullable Boolean getBoolean(@NotNull String path, @Nullable Boolean def) {
        return get(path, def, DataFunction.booleanValue());
    }

    /**
     * Predicate the value of given path is a {@link Byte}.
     *
     * @param path The path to check.
     * @return True if the value is present and is a byte, false otherwise.
     */
    default @Nullable Boolean isByte(@NotNull String path) {
        return isType(path, Byte.class);
    }

    /**
     * Get the value as a {@link Byte} from the specified path.
     *
     * @param path The path to get the byte.
     * @return The byte if the path exists and is a byte, otherwise 0.
     */
    default @Nullable Byte getByte(@NotNull String path) {
        return getByte(path, (byte) 0);
    }

    /**
     * Get the value as a {@link Byte} from the specified path.
     *
     * @param path The path to get the byte.
     * @param def  The default value to return if the path does not exist.
     * @return The byte if the path exists and is a byte, otherwise the default value.
     */
    @Contract("_, !null -> !null")
    default @Nullable Byte getByte(@NotNull String path, @Nullable Byte def) {
        return get(path, def, DataFunction.byteValue());
    }

    /**
     * Predicate the value of given path is a {@link Short}.
     *
     * @param path The path to check.
     * @return True if the value is present and is a short, false otherwise.
     */
    default boolean isShort(@NotNull String path) {
        return isType(path, Short.class);
    }

    /**
     * Get the value as a {@link Short} from the specified path.
     *
     * @param path The path to get the short.
     * @return The short if the path exists and is a short, otherwise 0.
     */
    default @Nullable Short getShort(@NotNull String path) {
        return getShort(path, (short) 0);
    }

    /**
     * Get the value as a {@link Short} from the specified path.
     *
     * @param path The path to get the short.
     * @param def  The default value to return if the path does not exist.
     * @return The short if the path exists and is a short, otherwise the default value.
     */
    @Contract("_, !null -> !null")
    default @Nullable Short getShort(@NotNull String path, @Nullable Short def) {
        return get(path, def, DataFunction.shortValue());
    }

    /**
     * Predicate the value of given path is a {@link Integer}.
     *
     * @param path The path to check.
     * @return True if the value is present and is an int, false otherwise.
     */
    default boolean isInt(@NotNull String path) {
        return isType(path, Integer.class);
    }

    /**
     * Get the value as a {@link Integer} from the specified path.
     *
     * @param path The path to get the int.
     * @return The int if the path exists and is an int, otherwise 0.
     */
    default @Nullable Integer getInt(@NotNull String path) {
        return getInt(path, 0);
    }

    /**
     * Get the value as a {@link Integer} from the specified path.
     *
     * @param path The path to get the int.
     * @param def  The default value to return if the path does not exist.
     * @return The int if the path exists and is an int, otherwise the default value.
     */
    @Contract("_, !null -> !null")
    default @Nullable Integer getInt(@NotNull String path, @Nullable Integer def) {
        return get(path, def, DataFunction.intValue());
    }


    /**
     * Predicate the value of given path is a {@link Long}.F
     *
     * @param path The path to check.
     * @return True if the value is present and is a long, false otherwise.
     */
    default boolean isLong(@NotNull String path) {
        return isType(path, Long.class);
    }

    /**
     * Get the value as a {@link Long} from the specified path.
     *
     * @param path The path to get the long.
     * @return The long if the path exists and is a long, otherwise 0.
     */
    default @Nullable Long getLong(@NotNull String path) {
        return getLong(path, 0L);
    }

    /**
     * Get the value as a {@link Long} from the specified path.
     *
     * @param path The path to get the long.
     * @param def  The default value to return if the path does not exist.
     * @return The long if the path exists and is a long, otherwise the default value.
     */
    @Contract("_, !null -> !null")
    default @Nullable Long getLong(@NotNull String path, @Nullable Long def) {
        return get(path, def, DataFunction.longValue());
    }

    /**
     * Predicate the value of given path is a {@link Float}.
     *
     * @param path The path to check.
     * @return True if the value is present and is a float, false otherwise.
     */
    default boolean isFloat(@NotNull String path) {
        return isType(path, Float.class);
    }

    /**
     * Get the value as a {@link Float} from the specified path.
     *
     * @param path The path to get the float.
     * @return The float if the path exists and is a float, otherwise 0.
     */
    default @Nullable Float getFloat(@NotNull String path) {
        return getFloat(path, 0.0F);
    }

    /**
     * Get the value as a {@link Float} from the specified path.
     *
     * @param path The path to get the float.
     * @param def  The default value to return if the path does not exist.
     * @return The float if the path exists and is a float, otherwise the default value.
     */
    @Contract("_, !null -> !null")
    default @Nullable Float getFloat(@NotNull String path, @Nullable Float def) {
        return get(path, def, DataFunction.floatValue());
    }

    /**
     * Predicate the value of given path is a {@link Double}.
     *
     * @param path The path to check.
     * @return True if the value is present and is a double, false otherwise.
     */
    default boolean isDouble(@NotNull String path) {
        return isType(path, Double.class);
    }

    /**
     * Get the value as a {@link Double} from the specified path.
     *
     * @param path The path to get the double.
     * @return The double if the path exists and is a double, otherwise 0.
     */
    default @Nullable Double getDouble(@NotNull String path) {
        return getDouble(path, 0.0D);
    }

    /**
     * Get the value as a {@link Double} from the specified path.
     *
     * @param path The path to get the double.
     * @param def  The default value to return if the path does not exist.
     * @return The double if the path exists and is a double, otherwise the default value.
     */
    @Contract("_, !null -> !null")
    default @Nullable Double getDouble(@NotNull String path, @Nullable Double def) {
        return get(path, def, DataFunction.doubleValue());
    }

    /**
     * Predicate the value of given path is a {@link Character}.
     *
     * @param path The path to check.
     * @return True if the value is present and is a char, false otherwise.
     */
    default boolean isChar(@NotNull String path) {
        return isType(path, Boolean.class);
    }

    /**
     * Get the value as a {@link Character} from the specified path.
     *
     * @param path The path to get the char.
     * @return The char if the path exists and is a char, otherwise null.
     */
    default @Nullable Character getChar(@NotNull String path) {
        return getChar(path, null);
    }

    /**
     * Get the value as a {@link Character} from the specified path.
     *
     * @param path The path to get the char.
     * @param def  The default value to return if the path does not exist.
     * @return The char if the path exists and is a char, otherwise the default value.
     */
    @Contract("_, !null -> !null")
    default @Nullable Character getChar(@NotNull String path, @Nullable Character def) {
        return get(path, def, Character.class);
    }

    /**
     * Predicate the value of given path is a {@link String}.
     *
     * @param path The path to check.
     * @return True if the value is present and is a string, false otherwise.
     */
    default boolean isString(@NotNull String path) {
        return isType(path, String.class);
    }

    /**
     * Get the value as a {@link String} from the specified path.
     *
     * @param path The path to get the string.
     * @return The string if the path exists and is a string, otherwise null.
     */
    default @Nullable String getString(@NotNull String path) {
        return getString(path, null);
    }

    /**
     * Get the value as a {@link String} from the specified path.
     *
     * @param path The path to get the string.
     * @param def  The default value to return if the path does not exist.
     * @return The string if the path exists and is a string, otherwise the default value.
     */
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

    /**
     * Get the specific type of collection from the section.
     *
     * @param path        The path to get the collection from
     * @param constructor The constructor of the collection
     * @param parser      The function to parse the values
     * @param <T>         The type of the values
     * @param <C>         The type of the collection
     * @return The collection of values
     */
    default <T, C extends Collection<T>> @NotNull C getCollection(@NotNull String path,
                                                                  @NotNull Supplier<C> constructor,
                                                                  @NotNull DataFunction<Object, T> parser) {
        return parseCollection(getList(path), constructor, parser);
    }

    /**
     * Get the specific type of steam from the section.
     *
     * @param path The path to get the stream from
     * @return The stream of values
     */
    default @NotNull Stream<?> stream(@NotNull String path) {
        List<?> values = getList(path);
        return values == null ? Stream.empty() : values.stream();
    }

    /**
     * Get the specific type of steam from the section.
     *
     * @param path   The path to get the stream from
     * @param parser The function to parse the values
     * @param <T>    The type of the values
     * @return The stream of values
     */
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
