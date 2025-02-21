package cc.carm.lib.configuration.source.section;

import cc.carm.lib.configuration.function.DataFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ImmutableSection implements ConfigureSection {

    public static ImmutableSection of(@NotNull ConfigureSection section) {
        if (section instanceof ImmutableSection) {
            return (ImmutableSection) section;
        } else return new ImmutableSection(null, section);
    }

    protected final @Nullable ImmutableSection parent;
    protected final @NotNull ConfigureSection section;

    private ImmutableSection(@Nullable ImmutableSection parent, @NotNull ConfigureSection section) {
        this.parent = parent;
        this.section = section;
    }

    @Override
    public @Nullable ImmutableSection parent() {
        return this.parent;
    }

    private @NotNull ConfigureSection section() {
        return section;
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> getValues(boolean deep) {
        return section().getValues(deep);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        throw new IllegalStateException("This section is not modifiable!");
    }

    @Override
    public void remove(@NotNull String path) {
        throw new IllegalStateException("This section is not modifiable!");
    }

    @Override
    public @NotNull ConfigureSection createSection(@NotNull Map<?, ?> data) {
        return new ImmutableSection(this, section().createSection(data));
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        Object value = section().get(path);
        if (value instanceof ConfigureSection && !(value instanceof ImmutableSection)) {
            return new ImmutableSection(this, (ConfigureSection) value);
        }
        return value;
    }

    @Override
    public @Nullable ConfigureSection getSection(@NotNull String path) {
        ConfigureSection get = section().getSection(path);
        if (get != null && !(get instanceof ImmutableSection)) {
            return new ImmutableSection(this, get);
        }
        return get;
    }

    @Override
    public char pathSeparator() {
        return section().pathSeparator();
    }

    @Override
    public boolean isRoot() {
        return section().isRoot();
    }

    @Override
    public boolean isEmpty() {
        return section().isEmpty();
    }

    @Override
    public @NotNull @UnmodifiableView Set<String> getKeys(boolean deep) {
        return section().getKeys(deep);
    }

    @Override
    public @NotNull @UnmodifiableView Set<String> keys() {
        return section().keys();
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> values() {
        return section().values();
    }

    @Override
    public Stream<Map.Entry<String, Object>> stream() {
        return section().stream();
    }

    @Override
    public void forEach(@NotNull BiConsumer<String, Object> action) {
        section().forEach(action);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return section().contains(path);
    }

    @Override
    public boolean containsValue(@NotNull String path) {
        return section().containsValue(path);
    }

    @Override
    public <T> boolean isType(@NotNull String path, @NotNull Class<T> typeClass) {
        return section().isType(path, typeClass);
    }

    @Override
    public boolean isList(@NotNull String path) {
        return section().isList(path);
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        return section().getList(path);
    }

    @Override
    public boolean isSection(@NotNull String path) {
        return section().isSection(path);
    }

    @Override
    public <T> @Nullable T get(@NotNull String path, @NotNull Class<T> type) {
        return section().get(path, type);
    }

    @Override
    public <T> @Nullable T get(@NotNull String path, @NotNull DataFunction<@Nullable Object, T> parser) {
        return section().get(path, parser);
    }

    @Override
    public <T> @Nullable T get(@NotNull String path, @Nullable T defaults, @NotNull Class<T> clazz) {
        return section().get(path, defaults, clazz);
    }

    @Override
    public <T> @Nullable T get(@NotNull String path, @Nullable T defaultValue, @NotNull DataFunction<Object, T> parser) {
        return section().get(path, defaultValue, parser);
    }

    @Override
    public boolean isBoolean(@NotNull String path) {
        return section().isBoolean(path);
    }

    @Override
    public boolean getBoolean(@NotNull String path) {
        return section().getBoolean(path);
    }

    @Override
    public @Nullable Boolean getBoolean(@NotNull String path, @Nullable Boolean def) {
        return section().getBoolean(path, def);
    }

    @Override
    public @Nullable Boolean isByte(@NotNull String path) {
        return section().isByte(path);
    }

    @Override
    public @Nullable Byte getByte(@NotNull String path) {
        return section().getByte(path);
    }

    @Override
    public @Nullable Byte getByte(@NotNull String path, @Nullable Byte def) {
        return section().getByte(path, def);
    }

    @Override
    public boolean isShort(@NotNull String path) {
        return section().isShort(path);
    }

    @Override
    public @Nullable Short getShort(@NotNull String path) {
        return section().getShort(path);
    }

    @Override
    public @Nullable Short getShort(@NotNull String path, @Nullable Short def) {
        return section().getShort(path, def);
    }

    @Override
    public boolean isInt(@NotNull String path) {
        return section().isInt(path);
    }

    @Override
    public @Nullable Integer getInt(@NotNull String path) {
        return section().getInt(path);
    }

    @Override
    public @Nullable Integer getInt(@NotNull String path, @Nullable Integer def) {
        return section().getInt(path, def);
    }

    @Override
    public boolean isLong(@NotNull String path) {
        return section().isLong(path);
    }

    @Override
    public @Nullable Long getLong(@NotNull String path) {
        return section().getLong(path);
    }

    @Override
    public @Nullable Long getLong(@NotNull String path, @Nullable Long def) {
        return section().getLong(path, def);
    }

    @Override
    public boolean isFloat(@NotNull String path) {
        return section().isFloat(path);
    }

    @Override
    public @Nullable Float getFloat(@NotNull String path) {
        return section().getFloat(path);
    }

    @Override
    public @Nullable Float getFloat(@NotNull String path, @Nullable Float def) {
        return section().getFloat(path, def);
    }

    @Override
    public boolean isDouble(@NotNull String path) {
        return section().isDouble(path);
    }

    @Override
    public @Nullable Double getDouble(@NotNull String path) {
        return section().getDouble(path);
    }

    @Override
    public @Nullable Double getDouble(@NotNull String path, @Nullable Double def) {
        return section().getDouble(path, def);
    }

    @Override
    public boolean isChar(@NotNull String path) {
        return section().isChar(path);
    }

    @Override
    public @Nullable Character getChar(@NotNull String path) {
        return section().getChar(path);
    }

    @Override
    public @Nullable Character getChar(@NotNull String path, @Nullable Character def) {
        return section().getChar(path, def);
    }

    @Override
    public boolean isString(@NotNull String path) {
        return section().isString(path);
    }

    @Override
    public @Nullable String getString(@NotNull String path) {
        return section().getString(path);
    }

    @Override
    public @Nullable String getString(@NotNull String path, @Nullable String def) {
        return section().getString(path, def);
    }

    @Override
    public @NotNull <V> List<V> getList(@NotNull String path, @NotNull DataFunction<Object, V> parser) {
        return section().getList(path, parser);
    }

    @Override
    public @NotNull List<String> getStringList(@NotNull String path) {
        return section().getStringList(path);
    }

    @Override
    public @NotNull List<Integer> getIntegerList(@NotNull String path) {
        return section().getIntegerList(path);
    }

    @Override
    public @NotNull List<Long> getLongList(@NotNull String path) {
        return section().getLongList(path);
    }

    @Override
    public @NotNull List<Double> getDoubleList(@NotNull String path) {
        return section().getDoubleList(path);
    }

    @Override
    public @NotNull List<Float> getFloatList(@NotNull String path) {
        return section().getFloatList(path);
    }

    @Override
    public @NotNull List<Byte> getByteList(@NotNull String path) {
        return section().getByteList(path);
    }

    @Override
    public @NotNull List<Character> getCharList(@NotNull String path) {
        return section().getCharList(path);
    }

    @Override
    public <T, C extends Collection<T>> @NotNull C getCollection(@NotNull String path, @NotNull Supplier<C> constructor, @NotNull DataFunction<Object, T> parser) {
        return section().getCollection(path, constructor, parser);
    }

    @Override
    public @NotNull Stream<?> stream(@NotNull String path) {
        return section().stream(path);
    }

    @Override
    public @NotNull <T> Stream<T> stream(@NotNull String path, @NotNull Function<Object, T> parser) {
        return section().stream(path, parser);
    }

    @Override
    public String childPath(String path) {
        return section().childPath(path);
    }

    @Override
    public int hashCode() {
        return section.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(section, obj);
    }

}
