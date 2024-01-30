//package cc.carm.lib.configuration.value.standard;
//
//import cc.carm.lib.configuration.function.ConfigDataFunction;
//import cc.carm.lib.configuration.value.ValueManifest;
//import cc.carm.lib.configuration.value.impl.ConfigValueMap;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Map;
//import java.util.function.Supplier;
//
//public class ConfiguredMap<K, V> extends ConfigValueMap<K, V, Object> {
//
//    public ConfiguredMap(@NotNull ValueManifest<Map<K, V>> manifest,
//                         @NotNull Supplier<? extends Map<K, V>> mapObjSupplier,
//                         @NotNull Class<K> keyClass, @NotNull ConfigDataFunction<String, K> keyParser,
//                         @NotNull Class<V> valueClass, @NotNull ConfigDataFunction<Object, V> valueParser,
//                         @NotNull ConfigDataFunction<K, String> keySerializer,
//                         @NotNull ConfigDataFunction<V, Object> valueSerializer) {
//        super(manifest, Object.class, mapObjSupplier, keyClass, keyParser, valueClass, valueParser, keySerializer, valueSerializer);
//    }
//
//    @Override
//    public Object getSource(ConfigurationWrapper<?> section, String dataKey) {
//        return section.get(dataKey);
//    }
//
//}
