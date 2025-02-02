package cc.carm.lib.configuration.builder.list;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.impl.AbstractSourceBuilder;
import cc.carm.lib.configuration.function.ConfigValueHandler;
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

    public SourceListBuilder(@NotNull ValueType<SOURCE> sourceType, @NotNull ValueType<V> paramType,
                             @NotNull ConfigValueHandler<SOURCE, V> parser, @NotNull ConfigValueHandler<V, SOURCE> serializer,
                             @NotNull Supplier<? extends List<V>> constructor) {
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
