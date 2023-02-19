package cc.carm.lib.configuration.core.value.type;

import cc.carm.lib.configuration.core.builder.list.ConfigListBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConfiguredList<V> extends CachedConfigValue<List<V>> implements List<V> {

    public static <V> @NotNull ConfigListBuilder<V> builder(@NotNull Class<V> valueClass) {
        return builder().asList(valueClass);
    }

    protected final @NotNull Class<V> valueClass;

    protected final @NotNull ConfigDataFunction<Object, V> parser;
    protected final @NotNull ConfigDataFunction<V, Object> serializer;

    public ConfiguredList(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                          @Nullable List<String> headerComments, @Nullable String inlineComments,
                          @NotNull Class<V> valueClass, @Nullable List<V> defaultValue,
                          @NotNull ConfigDataFunction<Object, V> parser,
                          @NotNull ConfigDataFunction<V, Object> serializer) {
        super(provider, sectionPath, headerComments, inlineComments, defaultValue);
        this.valueClass = valueClass;
        this.parser = parser;
        this.serializer = serializer;
    }

    @Override
    public @NotNull List<V> get() {
        if (isExpired()) { // 已过时的数据，需要重新解析一次。
            List<V> list = new ArrayList<>();
            List<?> data = getConfiguration().contains(getConfigPath()) ?
                    getConfiguration().getList(getConfigPath()) : null;
            if (data == null) return useOrDefault(list);
            for (Object dataVal : data) {
                if (dataVal == null) continue;
                try {
                    list.add(parser.parse(dataVal));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return updateCache(list);
        } else if (getCachedValue() != null) return getCachedValue();
        else if (getDefaultValue() != null) return getDefaultValue();
        else return new ArrayList<>();
    }

    @Override
    public V get(int index) {
        return get().get(index);
    }

    public <T> @NotNull T modifyValue(Function<List<V>, T> function) {
        List<V> list = get();
        T result = function.apply(list);
        set(list);
        return result;
    }

    public @NotNull List<V> modifyList(Consumer<List<V>> consumer) {
        List<V> list = get();
        consumer.accept(list);
        set(list);
        return list;
    }

    @Override
    public void set(@Nullable List<V> value) {
        updateCache(value);
        if (value == null) setValue(null);
        else {
            List<Object> data = new ArrayList<>();
            for (V val : value) {
                if (val == null) continue;
                try {
                    data.add(serializer.parse(val));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            setValue(data);
        }
    }

    @Override
    public V set(int index, V element) {
        return modifyValue(list -> list.set(index, element));
    }

    @Override
    public int size() {
        return get().size();
    }

    @Override
    public boolean isEmpty() {
        return get().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return get().contains(o);
    }

    @NotNull
    @Override
    public Iterator<V> iterator() {
        return get().iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return get().toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T[] a) {
        return get().toArray(a);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(get()).containsAll(c);
    }

    @Override
    public boolean add(V v) {
        modifyValue(list -> list.add(v));
        return true;
    }

    @Override
    public void add(int index, V element) {
        modifyList(list -> list.add(index, element));
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends V> c) {
        return modifyValue(list -> list.addAll(c));
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends V> c) {
        return modifyValue(list -> list.addAll(index, c));
    }

    @Override
    public boolean remove(Object o) {
        return modifyValue(list -> list.remove(o));
    }

    @Override
    public V remove(int index) {
        return modifyValue(list -> list.remove(index));
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return modifyValue(list -> list.removeAll(c));
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return modifyValue(list -> list.retainAll(c));
    }

    @Override
    public void clear() {
        modifyList(List::clear);
    }
    
    @Override
    public int indexOf(Object o) {
        return get().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return get().lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<V> listIterator() {
        return get().listIterator();
    }

    @NotNull
    @Override
    public ListIterator<V> listIterator(int index) {
        return get().listIterator(index);
    }

    @NotNull
    @Override
    public List<V> subList(int fromIndex, int toIndex) {
        return get().subList(fromIndex, toIndex);
    }

}
