package cc.carm.lib.configuration.builder.value;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.function.ConfigDataFunction;
import cc.carm.lib.configuration.function.ConfigValueHandler;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import org.jetbrains.annotations.NotNull;

public class SourceValueBuilder<S, V> extends CommonConfigBuilder<V, SourceValueBuilder<S, V>> {

    protected final @NotNull ValueType<S> sourceType;
    protected final @NotNull ValueType<V> valueType;
    protected @NotNull ConfigValueHandler<S, V> valueParser;
    protected @NotNull ConfigValueHandler<V, S> valueSerializer;

    public SourceValueBuilder(@NotNull ValueType<S> sourceType, @NotNull ValueType<V> valueType) {
        this(sourceType, valueType, ConfigValueHandler.required(), ConfigValueHandler.required());
    }

    public SourceValueBuilder(@NotNull ValueType<S> sourceType, @NotNull ValueType<V> valueType,
                              @NotNull ConfigValueHandler<S, V> valueParser,
                              @NotNull ConfigValueHandler<V, S> valueSerializer) {
        this.sourceType = sourceType;
        this.valueType = valueType;
        this.valueParser = valueParser;
        this.valueSerializer = valueSerializer;
    }

    @Override
    protected @NotNull SourceValueBuilder<S, V> self() {
        return this;
    }

    public @NotNull SourceValueBuilder<S, V> parse(ConfigDataFunction<S, V> parser) {
        return parse((p, source) -> parser.handle(source));
    }

    public @NotNull SourceValueBuilder<S, V> parse(@NotNull ConfigValueHandler<S, V> parser) {
        this.valueParser = parser;
        return this;
    }

    public @NotNull SourceValueBuilder<S, V> serialize(@NotNull ConfigValueHandler<V, S> serializer) {
        this.valueSerializer = serializer;
        return this;
    }

    public @NotNull SourceValueBuilder<S, V> serialize(@NotNull ConfigDataFunction<V, S> serializer) {
        return serialize((p, value) -> serializer.handle(value));
    }

    @Override
    public @NotNull ConfiguredValue<V> build() {
        return new ConfiguredValue<>(
                buildManifest(),
                (p, type, data) -> {
                    S source = p.deserialize(this.sourceType, data);
                    return this.valueParser.handle(p, source);
                },
                (p, type, value) -> {
                    S source = this.valueSerializer.handle(p, value);
                    return p.serialize(source);
                }
        );
    }

}
