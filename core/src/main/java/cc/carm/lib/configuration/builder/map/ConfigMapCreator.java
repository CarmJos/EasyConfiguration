package cc.carm.lib.configuration.builder.map;

import cc.carm.lib.configuration.adapter.ValueType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class ConfigMapCreator<K, V> {

    protected final @NotNull ValueType<K> keyType;
    protected final @NotNull ValueType<V> valueType;

    public ConfigMapCreator(@NotNull ValueType<K> keyType, @NotNull ValueType<V> valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public <M extends Map<K, V>> @NotNull ConfigMapBuilder<M, K, V> constructor(@NotNull Supplier<@NotNull M> mapSuppler) {
        return new ConfigMapBuilder<>(mapSuppler, keyType, valueType);
    }

    public <W extends Map<K, V>> @NotNull ConfigMapBuilder<W, K, V> constructor(@NotNull Class<W> type) {
        return constructor(() -> {
            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public @NotNull ConfigMapBuilder<HashMap<K, V>, K, V> asHashMap() {
        return constructor(HashMap::new);
    }

    public @NotNull ConfigMapBuilder<LinkedHashMap<K, V>, K, V> asLinkedMap() {
        return constructor(LinkedHashMap::new);
    }

    public @NotNull ConfigMapBuilder<TreeMap<K, V>, K, V> asTreeMap() {
        return constructor(TreeMap::new);
    }

}