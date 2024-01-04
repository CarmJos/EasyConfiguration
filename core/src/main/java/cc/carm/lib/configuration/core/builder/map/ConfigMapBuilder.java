package cc.carm.lib.configuration.core.builder.map;

import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public class ConfigMapBuilder<M extends Map<K, V>, K, V> {

    protected final @NotNull Supplier<? extends M> supplier;

    protected final @NotNull Class<K> keyClass;
    protected final @NotNull Class<V> valueClass;

    public ConfigMapBuilder(@NotNull Supplier<? extends M> supplier, @NotNull Class<K> keyClass, @NotNull Class<V> valueClass) {
        this.supplier = supplier;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    public <MAP extends Map<K, V>> ConfigMapBuilder<MAP, K, V> supplier(@NotNull Supplier<MAP> supplier) {
        return new ConfigMapBuilder<>(supplier, keyClass, valueClass);
    }

    public <S> SourceMapBuilder<M, S, K, V> from(@NotNull Class<S> sourceClass,
                                                 @NotNull ConfigDataFunction<S, V> valueParser,
                                                 @NotNull ConfigDataFunction<V, S> valueSerializer) {
        return new SourceMapBuilder<>(supplier,
                keyClass, ConfigDataFunction.castFromString(this.keyClass), // #String -> key
                sourceClass, ConfigDataFunction.castObject(sourceClass), // #Object -> source
                valueClass, valueParser, // source -> value
                ConfigDataFunction.castToString(), // key -> #String
                valueSerializer/*value -> source*/,
                ConfigDataFunction.toObject()/* source -> #Object */
        );
    }

    public <S> SourceMapBuilder<M, S, K, V> from(@NotNull Class<S> sourceClass) {
        return from(sourceClass, ConfigDataFunction.required(), ConfigDataFunction.required());
    }

    public SourceMapBuilder<M, String, K, V> fromString(@NotNull ConfigDataFunction<String, V> valueParser) {
        return from(String.class, valueParser, ConfigDataFunction.castToString());
    }

    public SourceMapBuilder<M, String, K, V> fromString() {
        return fromString(ConfigDataFunction.castFromString(this.valueClass));
    }

    public SectionMapBuilder<M, K, V> fromSection() {
        return new SectionMapBuilder<>(
                supplier,
                keyClass, ConfigDataFunction.castFromString(keyClass),
                valueClass, ConfigDataFunction.required(),
                ConfigDataFunction.castToString(), ConfigDataFunction.required());
    }

    public SourceMapBuilder<M, Object, K, V> fromObject(@NotNull ConfigDataFunction<Object, V> valueParser) {
        return from(Object.class, valueParser, ConfigDataFunction.toObject());
    }

    public SourceMapBuilder<M, Object, K, V> fromObject() {
        return fromObject(ConfigDataFunction.required());
    }


}
