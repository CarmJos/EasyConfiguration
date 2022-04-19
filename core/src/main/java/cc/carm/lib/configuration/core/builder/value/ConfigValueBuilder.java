package cc.carm.lib.configuration.core.builder.value;

import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ConfigValueBuilder<V> {

    protected final @NotNull Class<V> valueClass;

    public ConfigValueBuilder(@NotNull Class<V> valueClass) {
        this.valueClass = valueClass;
    }

    public @NotNull SectionValueBuilder<V> fromSection() {
        return fromSection(ConfigValueParser.required(), ConfigDataFunction.required());
    }

    public @NotNull SectionValueBuilder<V> fromSection(@NotNull ConfigValueParser<ConfigurationWrapper, V> valueParser,
                                                       @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> valueSerializer) {
        return new SectionValueBuilder<>(this.valueClass, valueParser, valueSerializer);
    }

    public @NotNull <S> SourceValueBuilder<S, V> from(Class<S> sourceClass) {
        return from(
                sourceClass, ConfigDataFunction.required(), ConfigValueParser.required(),
                ConfigDataFunction.required(), ConfigDataFunction.required()
        );
    }

    public @NotNull <S> SourceValueBuilder<S, V> from(@NotNull Class<S> sourceClass,
                                                      @NotNull ConfigDataFunction<Object, S> sourceParser,
                                                      @NotNull ConfigValueParser<S, V> valueParser,
                                                      @NotNull ConfigDataFunction<V, S> valueSerializer,
                                                      @NotNull ConfigDataFunction<S, Object> sourceSerializer) {
        return new SourceValueBuilder<>(
                sourceClass, sourceParser, this.valueClass, valueParser,
                valueSerializer, sourceSerializer
        );
    }

    public @NotNull SourceValueBuilder<Object, V> fromObject() {
        return from(
                Object.class, ConfigDataFunction.identity(),
                ConfigValueParser.castObject(valueClass),
                ConfigDataFunction.toObject(), ConfigDataFunction.toObject()
        );
    }

    public @NotNull SourceValueBuilder<String, V> fromString() {
        return from(
                String.class, ConfigDataFunction.castToString(),
                ConfigValueParser.parseString(this.valueClass),
                ConfigDataFunction.castToString(), ConfigDataFunction.toObject()
        );
    }

}
