package cc.carm.lib.configuration.builder.value;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.impl.AbstractSourceBuilder;
import cc.carm.lib.configuration.function.ConfigValueHandler;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import org.jetbrains.annotations.NotNull;

public class SourceValueBuilder<S, V> extends AbstractSourceBuilder<V, S, V, ConfiguredValue<V>, SourceValueBuilder<S, V>> {


    public SourceValueBuilder(@NotNull ValueType<S> sourceType, @NotNull ValueType<V> valueType,
                              @NotNull ConfigValueHandler<S, V> parser, @NotNull ConfigValueHandler<V, S> serializer) {
        super(valueType, sourceType, valueType, parser, serializer);
    }

    @Override
    protected @NotNull SourceValueBuilder<S, V> self() {
        return this;
    }

    @Override
    public @NotNull ConfiguredValue<V> build() {
        return new ConfiguredValue<>(buildManifest(), buildAdapter());
    }

}
