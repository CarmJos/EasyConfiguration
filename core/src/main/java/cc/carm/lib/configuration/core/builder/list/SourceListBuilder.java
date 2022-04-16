package cc.carm.lib.configuration.core.builder.list;

import cc.carm.lib.configuration.core.builder.AbstractConfigBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SourceListBuilder<S, V>
        extends AbstractConfigBuilder<SourceListBuilder<S, V>, List<V>> {

    protected final @NotNull Class<S> sourceClass;
    protected @NotNull ConfigDataFunction<Object, S> sourceParser;

    protected final @NotNull Class<V> valueClass;
    protected @NotNull ConfigDataFunction<S, V> valueParser;

    protected @NotNull ConfigDataFunction<V, S> valueSerializer;
    protected @NotNull ConfigDataFunction<S, Object> sourceSerializer;

    public SourceListBuilder(@NotNull Class<S> sourceClass, @NotNull ConfigDataFunction<Object, S> sourceParser,
                             @NotNull Class<V> valueClass, @NotNull ConfigDataFunction<S, V> valueParser,
                             @NotNull ConfigDataFunction<V, S> valueSerializer,
                             @NotNull ConfigDataFunction<S, Object> sourceSerializer) {
        this.sourceClass = sourceClass;
        this.sourceParser = sourceParser;
        this.sourceSerializer = sourceSerializer;
        this.valueClass = valueClass;
        this.valueParser = valueParser;
        this.valueSerializer = valueSerializer;
    }

    public @NotNull SourceListBuilder<S, V> parseSource(ConfigDataFunction<Object, S> sourceParser) {
        this.sourceParser = sourceParser;
        return this;
    }

    public @NotNull SourceListBuilder<S, V> parseValue(ConfigDataFunction<S, V> valueParser) {
        this.valueParser = valueParser;
        return this;
    }

    public @NotNull SourceListBuilder<S, V> serializeValue(ConfigDataFunction<V, S> serializer) {
        this.valueSerializer = serializer;
        return this;
    }

    public @NotNull SourceListBuilder<S, V> serializeSource(ConfigDataFunction<S, Object> serializer) {
        this.sourceSerializer = serializer;
        return this;
    }

    @Override
    protected @NotNull SourceListBuilder<S, V> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredList<V> build() {
        return new ConfiguredList<>(
                this.provider, this.path, this.comments,
                this.valueClass, this.defaultValue,
                this.sourceParser.andThen(this.valueParser),
                this.valueSerializer.andThen(sourceSerializer)
        );
    }


}
