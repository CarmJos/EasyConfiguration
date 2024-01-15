package cc.carm.lib.configuration.value.standard;

import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.impl.ConfigValueMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public class ConfiguredSectionMap<K, V> extends ConfigValueMap<K, V, ConfigurationWrapper<?>> {

    public ConfiguredSectionMap(@NotNull ValueManifest<Map<K, V>> manifest,
                                @NotNull Supplier<? extends Map<K, V>> mapObjSupplier,
                                @NotNull Class<K> keyClass, @NotNull ConfigDataFunction<String, K> keyParser,
                                @NotNull Class<V> valueClass, @NotNull ConfigDataFunction<ConfigurationWrapper<?>, V> valueParser,
                                @NotNull ConfigDataFunction<K, String> keySerializer,
                                @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> valueSerializer) {
        super(
                manifest, ConfigurationWrapper.class, mapObjSupplier,
                keyClass, keyParser, valueClass, valueParser,
                keySerializer, valueSerializer.andThen(s -> (Object) s)
        );
    }

    @Override
    public ConfigurationWrapper<?> getSource(ConfigurationWrapper<?> section, String dataKey) {
        return section.getConfigurationSection(dataKey);
    }

}
