package cc.carm.lib.configuration.core.value.type;

import cc.carm.lib.configuration.core.builder.map.ConfigMapBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ConfiguredMap<K, V> extends CachedConfigValue<Map<K, V>> {

    public static <K, V> @NotNull ConfigMapBuilder<LinkedHashMap<K, V>, K, V> builder(@NotNull Class<K> keyClass,
                                                                                      @NotNull Class<V> valueClass) {
        return builder().asMap(keyClass, valueClass);
    }

    protected final @NotNull Supplier<? extends Map<K, V>> supplier;

    protected final @NotNull Class<K> keyClass;
    protected final @NotNull Class<V> valueClass;

    protected final @NotNull ConfigDataFunction<String, K> keyParser;
    protected final @NotNull ConfigDataFunction<Object, V> valueParser;

    protected final @NotNull ConfigDataFunction<K, String> keySerializer;
    protected final @NotNull ConfigDataFunction<V, Object> valueSerializer;

    public ConfiguredMap(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                         @Nullable List<String> headerComments, @Nullable String inlineComments,
                         @Nullable Map<K, V> defaultValue, @NotNull Supplier<? extends Map<K, V>> supplier,
                         @NotNull Class<K> keyClass, @NotNull ConfigDataFunction<String, K> keyParser,
                         @NotNull Class<V> valueClass, @NotNull ConfigDataFunction<Object, V> valueParser,
                         @NotNull ConfigDataFunction<K, String> keySerializer,
                         @NotNull ConfigDataFunction<V, Object> valueSerializer) {
        super(provider, sectionPath, headerComments, inlineComments, defaultValue);
        this.supplier = supplier;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
        this.keyParser = keyParser;
        this.valueParser = valueParser;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }

    public @NotNull Class<K> getKeyClass() {
        return keyClass;
    }

    public @NotNull Class<V> getValueClass() {
        return valueClass;
    }

    public @NotNull ConfigDataFunction<String, K> getKeyParser() {
        return keyParser;
    }

    public @NotNull ConfigDataFunction<Object, V> getValueParser() {
        return valueParser;
    }

    public @NotNull ConfigDataFunction<K, String> getKeySerializer() {
        return keySerializer;
    }

    public @NotNull ConfigDataFunction<V, Object> getValueSerializer() {
        return valueSerializer;
    }

    @Override
    public @NotNull Map<K, V> get() {
        if (isExpired()) { // ????????????????????????????????????????????????
            Map<K, V> map = supplier.get();

            ConfigurationWrapper section = getConfiguration().getConfigurationSection(getConfigPath());
            if (section == null) return useOrDefault(map);

            Set<String> keys = section.getKeys(false);
            if (keys.isEmpty()) return useOrDefault(map);

            for (String dataKey : keys) {
                Object dataVal = section.get(dataKey);
                if (dataVal == null) continue;
                try {
                    K key = keyParser.parse(dataKey);
                    V value = valueParser.parse(dataVal);
                    map.put(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return updateCache(map);
        } else if (getCachedValue() != null) return getCachedValue();
        else if (getDefaultValue() != null) return getDefaultValue();
        else return supplier.get();
    }

    @Override
    public void set(Map<K, V> value) {
        updateCache(value);
        if (value == null) setValue(null);
        else {
            Map<String, Object> data = new LinkedHashMap<>();
            for (Map.Entry<K, V> entry : value.entrySet()) {
                try {
                    data.put(
                            keySerializer.parse(entry.getKey()),
                            valueSerializer.parse(entry.getValue())
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setValue(data);
        }
    }


}
