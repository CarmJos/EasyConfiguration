package cc.carm.lib.configuration.core.source;

import cc.carm.lib.configuration.core.function.ConfigValueParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface ConfigurationReader {

    ConfigurationWrapper<?> getWrapper();

    default boolean isBoolean(@NotNull String path) {
        return getWrapper().isType(path, Boolean.class);
    }

    default boolean getBoolean(@NotNull String path) {
        return getBoolean(path, false);
    }

    @Contract("_, !null -> !null")
    default @Nullable Boolean getBoolean(@NotNull String path, @Nullable Boolean def) {
        return getWrapper().get(path, def, ConfigValueParser.booleanValue());
    }

    default @Nullable Boolean isByte(@NotNull String path) {
        return getWrapper().isType(path, Byte.class);
    }

    default @Nullable Byte getByte(@NotNull String path) {
        return getByte(path, (byte) 0);
    }

    @Contract("_, !null -> !null")
    default @Nullable Byte getByte(@NotNull String path, @Nullable Byte def) {
        return getWrapper().get(path, def, ConfigValueParser.byteValue());
    }

    default boolean isShort(@NotNull String path) {
        return getWrapper().isType(path, Short.class);
    }

    default @Nullable Short getShort(@NotNull String path) {
        return getShort(path, (short) 0);
    }

    @Contract("_, !null -> !null")
    default @Nullable Short getShort(@NotNull String path, @Nullable Short def) {
        return getWrapper().get(path, def, ConfigValueParser.shortValue());
    }


    default boolean isInt(@NotNull String path) {
        return getWrapper().isType(path, Integer.class);
    }

    default @Nullable Integer getInt(@NotNull String path) {
        return getInt(path, 0);
    }

    @Contract("_, !null -> !null")
    default @Nullable Integer getInt(@NotNull String path, @Nullable Integer def) {
        return getWrapper().get(path, def, ConfigValueParser.intValue());
    }


    default boolean isLong(@NotNull String path) {
        return getWrapper().isType(path, Long.class);
    }

    default @Nullable Long getLong(@NotNull String path) {
        return getLong(path, 0L);
    }

    @Contract("_, !null -> !null")
    default @Nullable Long getLong(@NotNull String path, @Nullable Long def) {
        return getWrapper().get(path, def, ConfigValueParser.longValue());
    }


    default boolean isFloat(@NotNull String path) {
        return getWrapper().isType(path, Float.class);
    }

    default @Nullable Float getFloat(@NotNull String path) {
        return getFloat(path, 0.0F);
    }

    @Contract("_, !null -> !null")
    default @Nullable Float getFloat(@NotNull String path, @Nullable Float def) {
        return getWrapper().get(path, def, ConfigValueParser.floatValue());
    }


    default boolean isDouble(@NotNull String path) {
        return getWrapper().isType(path, Double.class);
    }

    default @Nullable Double getDouble(@NotNull String path) {
        return getDouble(path, 0.0D);
    }

    @Contract("_, !null -> !null")
    default @Nullable Double getDouble(@NotNull String path, @Nullable Double def) {
        return getWrapper().get(path, def, ConfigValueParser.doubleValue());
    }


    default boolean isChar(@NotNull String path) {
        return getWrapper().isType(path, Boolean.class);
    }

    default @Nullable Character getChar(@NotNull String path) {
        return getChar(path, null);
    }

    @Contract("_, !null -> !null")
    default @Nullable Character getChar(@NotNull String path, @Nullable Character def) {
        return getWrapper().get(path, def, Character.class);
    }


    default boolean isString(@NotNull String path) {
        return getWrapper().isType(path, String.class);
    }

    default @Nullable String getString(@NotNull String path) {
        return getString(path, null);
    }

    @Contract("_, !null -> !null")
    default @Nullable String getString(@NotNull String path, @Nullable String def) {
        return getWrapper().get(path, def, String.class);
    }

    @Unmodifiable
    default @NotNull List<String> getStringList(@NotNull String path) {
        return parseList(getWrapper().getList(path), ConfigValueParser.castToString());
    }

    @Unmodifiable
    default @NotNull List<Integer> getIntegerList(@NotNull String path) {
        return parseList(getWrapper().getList(path), ConfigValueParser.intValue());
    }

    @Unmodifiable
    default @NotNull List<Long> getLongList(@NotNull String path) {
        return parseList(getWrapper().getList(path), ConfigValueParser.longValue());
    }

    @Unmodifiable
    default @NotNull List<Double> getDoubleList(@NotNull String path) {
        return parseList(getWrapper().getList(path), ConfigValueParser.doubleValue());
    }

    @Unmodifiable
    default @NotNull List<Float> getFloatList(@NotNull String path) {
        return parseList(getWrapper().getList(path), ConfigValueParser.floatValue());
    }

    @Unmodifiable
    default @NotNull List<Byte> getByteList(@NotNull String path) {
        return parseList(getWrapper().getList(path), ConfigValueParser.byteValue());
    }

    @Unmodifiable
    default @NotNull List<Character> getCharList(@NotNull String path) {
        return parseList(getWrapper().getList(path), ConfigValueParser.castObject(Character.class));
    }

    @Unmodifiable
    static <T> @NotNull List<T> parseList(@Nullable List<?> list, ConfigValueParser<Object, T> parser) {
        if (list == null) return Collections.emptyList();
        List<T> values = new ArrayList<>();
        for (Object o : list) {
            try {
                T parsed = parser.parse(o, null);
                if (parsed != null) values.add(parsed);
            } catch (Exception ignored) {
            }
        }
        return values;
    }


}
