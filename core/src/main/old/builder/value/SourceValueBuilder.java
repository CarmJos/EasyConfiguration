package cc.carm.lib.configuration.builder.value;

import cc.carm.lib.configuration.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.function.ConfigDataFunction;
import cc.carm.lib.configuration.function.ConfigValueParser;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import org.jetbrains.annotations.NotNull;

public class SourceValueBuilder<S, V> extends CommonConfigBuilder<V, SourceValueBuilder<S, V>> {

    protected final @NotNull Class<S> sourceClass;
    protected @NotNull ConfigDataFunction<Object, S> sourceParser;

    protected final @NotNull Class<V> valueClass;
    protected @NotNull ConfigValueParser<S, V> valueParser;

    protected @NotNull ConfigDataFunction<S, Object> sourceSerializer;
    protected @NotNull ConfigDataFunction<V, S> valueSerializer;

    public SourceValueBuilder(@NotNull Class<S> sourceClass, @NotNull ConfigDataFunction<Object, S> sourceParser,
                              @NotNull Class<V> valueClass, @NotNull ConfigValueParser<S, V> valueParser,
                              @NotNull ConfigDataFunction<V, S> valueSerializer,
                              @NotNull ConfigDataFunction<S, Object> sourceSerializer) {
        this.sourceClass = sourceClass;
        this.sourceParser = sourceParser;
        this.valueClass = valueClass;
        this.valueParser = valueParser;
        this.sourceSerializer = sourceSerializer;
        this.valueSerializer = valueSerializer;
    }

    @Override
    protected @NotNull SourceValueBuilder<S, V> getThis() {
        return this;
    }

    public @NotNull SourceValueBuilder<S, V> parseSource(@NotNull ConfigDataFunction<Object, S> sourceParser) {
        this.sourceParser = sourceParser;
        return this;
    }

    public @NotNull SourceValueBuilder<S, V> parseValue(ConfigDataFunction<S, V> valueParser) {
        return parseValue((section, path) -> valueParser.parse(section));
    }

    public @NotNull SourceValueBuilder<S, V> parseValue(@NotNull ConfigValueParser<S, V> valueParser) {
        this.valueParser = valueParser;
        return this;
    }

    public @NotNull SourceValueBuilder<S, V> serializeValue(@NotNull ConfigDataFunction<V, S> serializer) {
        this.valueSerializer = serializer;
        return this;
    }

    public @NotNull SourceValueBuilder<S, V> serializeSource(@NotNull ConfigDataFunction<S, Object> serializer) {
        this.sourceSerializer = serializer;
        return this;
    }

    @Override
    public @NotNull ConfiguredValue<V> build() {
        return new ConfiguredValue<>(
                buildManifest(), this.valueClass,
                this.valueParser.compose(this.sourceParser),
                this.valueSerializer.andThen(sourceSerializer)
        );
    }

}
