package cc.carm.lib.configuration.core.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapFactory<S extends Map<K, V>, K, V> {

    private final S map;

    protected MapFactory(S map) {
        this.map = map;
    }

    public MapFactory<S, K, V> put(K key, V value) {
        this.map.put(key, value);
        return this;
    }

    public MapFactory<S, K, V> remove(K key) {
        this.map.remove(key);
        return this;
    }

    public MapFactory<S, K, V> clear() {
        this.map.clear();
        return this;
    }

    public S build() {
        return get();
    }

    public S get() {
        return map;
    }

    public static <K, V> MapFactory<HashMap<K, V>, K, V> hashMap() {
        return new MapFactory<>(new HashMap<>());
    }

    public static <K, V> MapFactory<HashMap<K, V>, K, V> hashMap(K firstKey, V firstValue) {
        return MapFactory.<K, V>hashMap().put(firstKey, firstValue);
    }

    public static <K, V> MapFactory<LinkedHashMap<K, V>, K, V> linkedMap() {
        return of(new LinkedHashMap<>());
    }

    public static <K, V> MapFactory<LinkedHashMap<K, V>, K, V> linkedMap(K firstKey, V firstValue) {
        return MapFactory.<K, V>linkedMap().put(firstKey, firstValue);
    }

    public static <K extends Comparable<K>, V> MapFactory<TreeMap<K, V>, K, V> treeMap() {
        return of(new TreeMap<>());
    }

    public static <K extends Comparable<K>, V> MapFactory<TreeMap<K, V>, K, V> treeMap(K firstKey, V firstValue) {
        return MapFactory.<K, V>treeMap().put(firstKey, firstValue);
    }

    public static <M extends Map<K, V>, K, V> MapFactory<M, K, V> of(M map) {
        return new MapFactory<>(map);
    }

}
