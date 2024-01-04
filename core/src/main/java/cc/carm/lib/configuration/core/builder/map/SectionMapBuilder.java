package cc.carm.lib.configuration.core.builder.map;

import cc.carm.lib.configuration.core.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.configuration.core.value.type.ConfiguredSectionMap;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SectionMapBuilder<M extends Map<K, V>, K, V> extends CommonConfigBuilder<M, SectionMapBuilder<M, K, V>> {

    protected final @NotNull Supplier<? extends M> supplier;

    protected final @NotNull Class<K> keyClass;
    protected @NotNull ConfigDataFunction<String, K> keyParser;

    protected final @NotNull Class<V> valueClass;
    protected @NotNull ConfigDataFunction<ConfigurationWrapper<?>, V> valueParser;

    protected @NotNull ConfigDataFunction<K, String> keySerializer;
    protected @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> valueSerializer;

    public SectionMapBuilder(@NotNull Supplier<? extends M> supplier,
                             @NotNull Class<K> keyClass, @NotNull ConfigDataFunction<String, K> keyParser,
                             @NotNull Class<V> valueClass, @NotNull ConfigDataFunction<ConfigurationWrapper<?>, V> valueParser,
                             @NotNull ConfigDataFunction<K, String> keySerializer,
                             @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> valueSerializer) {
        this.supplier = supplier;
        this.keyClass = keyClass;
        this.keyParser = keyParser;
        this.valueClass = valueClass;
        this.valueParser = valueParser;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }

    public <MAP extends Map<K, V>> SectionMapBuilder<MAP, K, V> supplier(@NotNull Supplier<MAP> supplier) {
        return new SectionMapBuilder<>(supplier,
                keyClass, keyParser, valueClass, valueParser, keySerializer, valueSerializer
        );
    }

    public @NotNull SectionMapBuilder<M, K, V> defaults(@NotNull Consumer<M> factory) {
        M map = supplier.get();
        factory.accept(map);
        return defaults(map);
    }

    public @NotNull SectionMapBuilder<M, K, V> parseKey(@NotNull ConfigDataFunction<String, K> parser) {
        this.keyParser = parser;
        return this;
    }

    public @NotNull SectionMapBuilder<M, K, V> parseValue(@NotNull ConfigDataFunction<ConfigurationWrapper<?>, V> parser) {
        this.valueParser = parser;
        return this;
    }

    public @NotNull SectionMapBuilder<M, K, V> serializeKey(@NotNull ConfigDataFunction<K, String> serializer) {
        this.keySerializer = serializer;
        return this;
    }

    public @NotNull SectionMapBuilder<M, K, V> serializeValue(@NotNull ConfigDataFunction<V, ? extends Map<String, Object>> serializer) {
        this.valueSerializer = serializer;
        return this;
    }

    public @NotNull SectionMapBuilder<M, K, V> serializeValue(@NotNull BiConsumer<V, Map<String, Object>> serializer) {
        return serializeValue(v -> {
            Map<String, Object> map = new LinkedHashMap<>();
            serializer.accept(v, map);
            return map;
        });
    }


    @Override
    protected @NotNull SectionMapBuilder<M, K, V> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredSectionMap<K, V> build() {
        return new ConfiguredSectionMap<>(
                new ValueManifest<>(provider, path, headerComments, inlineComment, defaultValue),
                this.supplier, this.keyClass, this.keyParser,
                this.valueClass, this.valueParser,
                this.keySerializer, this.valueSerializer
        );
    }

}
