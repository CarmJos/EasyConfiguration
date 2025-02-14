package cc.carm.lib.configuration.builder.list;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.impl.AbstractSourceBuilder;
import cc.carm.lib.configuration.function.ValueHandler;
import cc.carm.lib.configuration.value.standard.ConfiguredList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class SourceListBuilder<SOURCE, V>
        extends AbstractSourceBuilder<List<V>, SOURCE, V, ConfiguredList<V>, SourceListBuilder<SOURCE, V>> {

    protected @NotNull Supplier<? extends List<V>> constructor;

    public SourceListBuilder(@NotNull Supplier<? extends List<V>> constructor,
                             @NotNull ValueType<SOURCE> sourceType, @NotNull ValueType<V> paramType,
                             @NotNull ValueHandler<SOURCE, V> parser, @NotNull ValueHandler<V, SOURCE> serializer) {
        super(new ValueType<List<V>>() {
        }, sourceType, paramType, parser, serializer);
        this.constructor = constructor;
    }

    @SafeVarargs
    public final @NotNull SourceListBuilder<SOURCE, V> defaults(@NotNull V... values) {
        return defaults(new ArrayList<>(Arrays.asList(values)));
    }

    public final @NotNull SourceListBuilder<SOURCE, V> defaults(@NotNull Collection<V> values) {
        return defaults(new ArrayList<>(values));
    }

    @Override
    protected @NotNull SourceListBuilder<SOURCE, V> self() {
        return this;
    }

    @Override
    public @NotNull ConfiguredList<V> build() {
        return new ConfiguredList<>(buildManifest(), this.constructor, buildAdapter());
    }

}
