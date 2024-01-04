package cc.carm.lib.configuration.core.builder.map;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class ConfigMapCreator<K, V> {

    protected final @NotNull Class<K> keyClass;
    protected final @NotNull Class<V> valueClass;

    public ConfigMapCreator(@NotNull Class<K> keyClass, @NotNull Class<V> valueClass) {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    public <M extends Map<K, V>> @NotNull ConfigMapBuilder<M, K, V> asMap(Supplier<? extends M> mapSuppler) {
        return new ConfigMapBuilder<>(mapSuppler, keyClass, valueClass);
    }

    public @NotNull ConfigMapBuilder<HashMap<K, V>, K, V> asHashMap() {
        return asMap(HashMap::new);
    }

    public @NotNull ConfigMapBuilder<LinkedHashMap<K, V>, K, V> asLinkedMap() {
        return asMap(LinkedHashMap::new);
    }

    public @NotNull ConfigMapBuilder<TreeMap<K, V>, K, V> asTreeMap() {
        return asMap(TreeMap::new);
    }

    public @NotNull ConfigMapBuilder<TreeMap<K, V>, K, V> asTreeMap(@NotNull Comparator<? super K> comparator) {
        return asMap(() -> new TreeMap<>(comparator));
    }

}
