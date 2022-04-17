package cc.carm.lib.configuration.core.builder;

import cc.carm.lib.configuration.core.builder.list.ConfigListBuilder;
import cc.carm.lib.configuration.core.builder.map.ConfigMapBuilder;
import cc.carm.lib.configuration.core.builder.value.ConfigValueBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class ConfigBuilder {

    public <V> @NotNull ConfigValueBuilder<V> asValue(@NotNull Class<V> valueClass) {
        return new ConfigValueBuilder<>(valueClass);
    }

    public <V> @NotNull ConfigListBuilder<V> asList(@NotNull Class<V> valueClass) {
        return new ConfigListBuilder<>(valueClass);
    }

    public <K, V> @NotNull ConfigMapBuilder<LinkedHashMap<K, V>, K, V> asMap(@NotNull Class<K> keyClass,
                                                                                    @NotNull Class<V> valueClass) {
        return new ConfigMapBuilder<>(LinkedHashMap::new, keyClass, valueClass);
    }

    public <K, V> @NotNull ConfigMapBuilder<HashMap<K, V>, K, V> asHashMap(@NotNull Class<K> keyClass,
                                                                                  @NotNull Class<V> valueClass) {
        return asMap(keyClass, valueClass).supplier(HashMap::new);
    }

    public <K, V> @NotNull ConfigMapBuilder<LinkedHashMap<K, V>, K, V> asLinkedMap(@NotNull Class<K> keyClass,
                                                                                          @NotNull Class<V> valueClass) {
        return asMap(keyClass, valueClass);
    }

    public <K extends Comparable<K>, V> @NotNull ConfigMapBuilder<TreeMap<K, V>, K, V> asTreeMap(@NotNull Class<K> keyClass,
                                                                                                        @NotNull Class<V> valueClass) {
        return asMap(keyClass, valueClass).supplier(TreeMap::new);
    }

}
