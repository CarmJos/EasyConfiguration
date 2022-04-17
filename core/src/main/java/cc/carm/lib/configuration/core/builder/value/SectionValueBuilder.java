package cc.carm.lib.configuration.core.builder.value;

import cc.carm.lib.configuration.core.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SectionValueBuilder<V>
        extends CommonConfigBuilder<V, SectionValueBuilder<V>> {


    protected final @NotNull Class<V> valueClass;

    protected @NotNull ConfigValueParser<ConfigurationWrapper, V> parser;
    protected @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> serializer;

    public SectionValueBuilder(@NotNull Class<V> valueClass,
                               @NotNull ConfigValueParser<ConfigurationWrapper, V> parser,
                               @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> serializer) {
        this.valueClass = valueClass;
        this.parser = parser;
        this.serializer = serializer;
    }


    @Override
    protected @NotNull SectionValueBuilder<V> getThis() {
        return this;
    }

    public @NotNull SectionValueBuilder<V> parseValue(ConfigValueParser<ConfigurationWrapper, V> valueParser) {
        this.parser = valueParser;
        return this;
    }

    public @NotNull SectionValueBuilder<V> serializeValue(ConfigDataFunction<V, ? extends Map<String, Object>> serializer) {
        this.serializer = serializer;
        return this;
    }

    @Override
    public @NotNull ConfiguredSection<V> build() {
        return new ConfiguredSection<>(
                this.provider, this.path, this.comments,
                this.valueClass, this.defaultValue,
                this.parser, this.serializer
        );
    }

}
