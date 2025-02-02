package cc.carm.lib.configuration.builder.impl;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.function.ConfigDataFunction;
import cc.carm.lib.configuration.function.ConfigValueHandler;
import cc.carm.lib.configuration.value.ConfigValue;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSourceBuilder<
        V, SOURCE, PARAM, RESULT extends ConfigValue<V>,
        SELF extends AbstractSourceBuilder<V, SOURCE, PARAM, RESULT, SELF>
        > extends CommonConfigBuilder<V, RESULT, SELF> {

    protected final @NotNull ValueType<SOURCE> sourceType;
    protected final @NotNull ValueType<PARAM> paramType;
    protected @NotNull ConfigValueHandler<SOURCE, PARAM> valueParser;
    protected @NotNull ConfigValueHandler<PARAM, SOURCE> valueSerializer;

    public AbstractSourceBuilder(@NotNull ValueType<V> type,
                                 @NotNull ValueType<SOURCE> sourceType, @NotNull ValueType<PARAM> paramType,
                                 @NotNull ConfigValueHandler<SOURCE, PARAM> parser,
                                 @NotNull ConfigValueHandler<PARAM, SOURCE> serializer) {
        super(type);
        this.sourceType = sourceType;
        this.paramType = paramType;
        this.valueParser = parser;
        this.valueSerializer = serializer;
    }

    public @NotNull SELF parse(ConfigDataFunction<SOURCE, PARAM> parser) {
        return parse((p, source) -> parser.handle(source));
    }

    public @NotNull SELF parse(@NotNull ConfigValueHandler<SOURCE, PARAM> parser) {
        this.valueParser = parser;
        return self();
    }

    public @NotNull SELF serialize(@NotNull ConfigValueHandler<PARAM, SOURCE> serializer) {
        this.valueSerializer = serializer;
        return self();
    }

    public @NotNull SELF serialize(@NotNull ConfigDataFunction<PARAM, SOURCE> serializer) {
        return serialize((p, value) -> serializer.handle(value));
    }

    protected ValueAdapter<PARAM> buildAdapter() {
        return new ValueAdapter<>(this.paramType)
                .parser((p, type, data) -> {
                    SOURCE source = p.deserialize(this.sourceType, data);
                    return this.valueParser.handle(p, source);
                })
                .serializer((p, type, data) -> {
                    SOURCE source = this.valueSerializer.handle(p, data);
                    return p.serialize(source);
                });
    }


}
