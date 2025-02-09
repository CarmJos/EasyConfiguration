package cc.carm.lib.configuration.value.standard;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueParser;
import cc.carm.lib.configuration.adapter.ValueSerializer;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.section.ConfigurationSection;
import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfiguredMap<K, V> extends CachedConfigValue<Map<K, V>> implements Map<K, V> {

    protected final @NotNull Supplier<? extends Map<K, V>> constructor;

    protected final @NotNull ValueAdapter<K> keyAdapter;
    protected final @NotNull ValueAdapter<V> valueAdapter;

    protected ConfiguredMap(@NotNull ValueManifest<Map<K, V>> manifest,
                            @NotNull Supplier<? extends Map<K, V>> constructor,
                            @NotNull ValueAdapter<K> keyAdapter, @NotNull ValueAdapter<V> valueAdapter) {
        super(manifest);
        this.constructor = constructor;
        this.keyAdapter = keyAdapter;
        this.valueAdapter = valueAdapter;
    }

    public @NotNull ValueAdapter<K> keyAdapter() {
        return keyAdapter;
    }

    public @NotNull ValueType<K> keyType() {
        return keyAdapter().type();
    }

    public @NotNull ValueAdapter<V> valueAdapter() {
        return valueAdapter;
    }

    public @NotNull ValueType<V> valueType() {
        return valueAdapter().type();
    }

    private Map<K, V> createMap() {
        return this.constructor.get();
    }

    @Override
    public @NotNull Map<K, V> get() {
        if (!cacheExpired()) return getCachedOrDefault(createMap());
        // If the value is expired, we need to update it
        Map<K, V> map = createMap();

        ConfigurationSection section = config().getSection(path());
        if (section == null) return getDefaultFirst(map);

        Set<String> keys = section.getKeys(false);
        if (keys.isEmpty()) return getDefaultFirst(map);

        ValueParser<K> keyParser = parserFor(keyAdapter);
        ValueParser<V> valueParser = parserFor(valueAdapter);
        if (keyParser == null || valueParser == null) return getDefaultFirst(map);

        for (String dataKey : keys) {
            Object dataVal = section.get(dataKey);
            if (dataVal == null) continue;
            try {
                K key = keyParser.parse(holder(), keyType(), dataKey);
                V value = valueParser.parse(holder(), valueType(), dataVal);
                map.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return updateCache(map);
    }

    @Override
    public V get(Object key) {
        return get().get(key);
    }

    public V getNotNull(Object key) {
        return Objects.requireNonNull(get(key));
    }

    @Override
    public void set(@Nullable Map<K, V> value) {
        updateCache(value);
        if (value == null) {
            setData(null);
            return;
        }

        ValueSerializer<K> keySerializer = serializerFor(keyAdapter);
        ValueSerializer<V> valueSerializer = serializerFor(valueAdapter);
        if (keySerializer == null || valueSerializer == null) return;

        Map<Object, Object> data = new LinkedHashMap<>();

        for (Map.Entry<K, V> entry : value.entrySet()) {
            try {
                data.put(
                        keySerializer.serialize(holder(), keyType(), entry.getKey()),
                        valueSerializer.serialize(holder(), valueType(), entry.getValue())
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setData(data);
    }

    public <T> @NotNull T handle(Function<Map<K, V>, T> function) {
        Map<K, V> m = get();
        T result = function.apply(m);
        set(m);
        return result;
    }

    public @NotNull ConfiguredMap<K, V> modify(Consumer<Map<K, V>> consumer) {
        Map<K, V> m = get();
        consumer.accept(m);
        set(m);
        return this;
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
    public boolean containsKey(Object key) {
        return get().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return get().containsValue(value);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        return handle(m -> m.put(key, value));
    }

    @Override
    public V remove(Object key) {
        return handle(m -> m.remove(key));
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        modify(map -> map.putAll(m));
    }

    @Override
    public void clear() {
        modify(Map::clear);
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return get().keySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return get().values();
    }

    @NotNull
    @Override
    @Unmodifiable
    public Set<Entry<K, V>> entrySet() {
        return get().entrySet();
    }

}
