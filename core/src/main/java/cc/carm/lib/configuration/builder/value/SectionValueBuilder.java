package cc.carm.lib.configuration.builder.value;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.impl.AbstractSectionBuilder;
import cc.carm.lib.configuration.function.ValueHandler;
import cc.carm.lib.configuration.source.section.ConfigurationSection;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SectionValueBuilder<V> extends AbstractSectionBuilder<V, V, ConfiguredValue<V>, SectionValueBuilder<V>> {

    public SectionValueBuilder(@NotNull ValueType<V> type,
                               @NotNull ValueHandler<ConfigurationSection, V> parser,
                               @NotNull ValueHandler<V, ? extends Map<Object, Object>> serializer) {
        super(type, type, parser, serializer);
    }

    @Override
    protected @NotNull SectionValueBuilder<V> self() {
        return this;
    }

    @Override
    public @NotNull ConfiguredValue<V> build() {
        return ConfiguredValue.of(buildManifest(), buildAdapter());
    }

}
