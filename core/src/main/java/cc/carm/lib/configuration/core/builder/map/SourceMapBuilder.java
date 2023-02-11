package cc.carm.lib.configuration.core.builder.map;

import cc.carm.lib.configuration.core.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.value.type.ConfiguredMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SourceMapBuilder<M extends Map<K, V>, S, K, V> extends CommonConfigBuilder<M, SourceMapBuilder<M, S, K, V>> {

    protected final @NotNull Supplier<@NotNull M> supplier;

    protected final @NotNull Class<K> keyClass;
    protected @NotNull ConfigDataFunction<String, K> keyParser;

    protected final @NotNull Class<S> sourceClass;
    protected @NotNull ConfigDataFunction<Object, S> sourceParser;

    protected final @NotNull Class<V> valueClass;
    protected @NotNull ConfigDataFunction<S, V> valueParser;

    protected @NotNull ConfigDataFunction<K, String> keySerializer;
    protected @NotNull ConfigDataFunction<V, S> valueSerializer;
    protected @NotNull ConfigDataFunction<S, Object> sourceSerializer;

    public SourceMapBuilder(@NotNull Supplier<@NotNull M> supplier,
                            @NotNull Class<K> keyClass, @NotNull ConfigDataFunction<String, K> keyParser,
                            @NotNull Class<S> sourceClass, @NotNull ConfigDataFunction<Object, S> sourceParser,
                            @NotNull Class<V> valueClass, @NotNull ConfigDataFunction<S, V> valueParser,
                            @NotNull ConfigDataFunction<K, String> keySerializer,
                            @NotNull ConfigDataFunction<V, S> valueSerializer,
                            @NotNull ConfigDataFunction<S, Object> sourceSerializer) {
        this.supplier = supplier;
        this.keyClass = keyClass;
        this.keyParser = keyParser;
        this.valueClass = valueClass;
        this.valueParser = valueParser;
        this.sourceClass = sourceClass;
        this.sourceParser = sourceParser;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
        this.sourceSerializer = sourceSerializer;
    }

    public <MAP extends Map<K, V>> SourceMapBuilder<MAP, S, K, V> supplier(@NotNull Supplier<MAP> supplier) {
        return new SourceMapBuilder<>(supplier,
                keyClass, keyParser, sourceClass, sourceParser, valueClass, valueParser,
                keySerializer, valueSerializer, sourceSerializer
        );
    }

    public @NotNull SourceMapBuilder<M, S, K, V> defaults(@NotNull Consumer<M> factory) {
        M map = supplier.get();
        factory.accept(map);
        return defaults(map);
    }

    public @NotNull SourceMapBuilder<M, S, K, V> parseKey(@NotNull ConfigDataFunction<String, K> parser) {
        this.keyParser = parser;
        return this;
    }

    public @NotNull SourceMapBuilder<M, S, K, V> parseSource(@NotNull ConfigDataFunction<Object, S> parser) {
        this.sourceParser = parser;
        return this;
    }

    public @NotNull SourceMapBuilder<M, S, K, V> parseValue(@NotNull ConfigDataFunction<S, V> parser) {
        this.valueParser = parser;
        return this;
    }

    public @NotNull SourceMapBuilder<M, S, K, V> serializeKey(@NotNull ConfigDataFunction<K, String> serializer) {
        this.keySerializer = serializer;
        return this;
    }

    public @NotNull SourceMapBuilder<M, S, K, V> serializeValue(@NotNull ConfigDataFunction<V, S> serializer) {
        this.valueSerializer = serializer;
        return this;
    }

    public @NotNull SourceMapBuilder<M, S, K, V> serializeSource(@NotNull ConfigDataFunction<S, Object> serializer) {
        this.sourceSerializer = serializer;
        return this;
    }

    @Override
    protected @NotNull SourceMapBuilder<M, S, K, V> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredMap<K, V> build() {
        return new ConfiguredMap<>(
                this.provider, this.path,
                this.headerComments, this.inlineComment,
                this.defaultValue, this.supplier,
                this.keyClass, this.keyParser,
                this.valueClass, this.sourceParser.andThen(this.valueParser),
                this.keySerializer, this.valueSerializer.andThen(this.sourceSerializer)
        );
    }

}
