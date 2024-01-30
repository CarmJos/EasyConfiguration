package cc.carm.lib.configuration.builder.list;

import cc.carm.lib.configuration.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.function.ConfigDataFunction;
import cc.carm.lib.configuration.value.standard.ConfiguredList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SourceListBuilder<S, V> extends CommonConfigBuilder<List<V>, SourceListBuilder<S, V>> {

    protected final @NotNull Class<? super S> sourceClass;
    protected @NotNull ConfigDataFunction<Object, S> sourceParser;

    protected final @NotNull Class<V> valueClass;
    protected @NotNull ConfigDataFunction<S, V> valueParser;

    protected @NotNull ConfigDataFunction<V, S> valueSerializer;
    protected @NotNull ConfigDataFunction<S, Object> sourceSerializer;

    public SourceListBuilder(@NotNull Class<? super S> sourceClass, @NotNull ConfigDataFunction<Object, S> sourceParser,
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

    @SafeVarargs
    public final @NotNull SourceListBuilder<S, V> defaults(@NotNull V... values) {
        return defaults(new ArrayList<>(Arrays.asList(values)));
    }

    public final @NotNull SourceListBuilder<S, V> defaults(@NotNull Collection<V> values) {
        return defaults(new ArrayList<>(values));
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
                buildManifest(), this.valueClass,
                this.sourceParser.andThen(this.valueParser),
                this.valueSerializer.andThen(sourceSerializer)
        );
    }


}
