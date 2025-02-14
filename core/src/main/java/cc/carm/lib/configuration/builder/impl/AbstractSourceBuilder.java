package cc.carm.lib.configuration.builder.impl;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.function.DataFunction;
import cc.carm.lib.configuration.function.ValueHandler;
import cc.carm.lib.configuration.value.ConfigValue;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSourceBuilder<
        V, SOURCE, PARAM, RESULT extends ConfigValue<V>,
        SELF extends AbstractSourceBuilder<V, SOURCE, PARAM, RESULT, SELF>
        > extends CommonConfigBuilder<V, RESULT, SELF> {

    protected final @NotNull ValueType<SOURCE> sourceType;
    protected final @NotNull ValueType<PARAM> paramType;
    protected @NotNull ValueHandler<SOURCE, PARAM> valueParser;
    protected @NotNull ValueHandler<PARAM, SOURCE> valueSerializer;

    protected AbstractSourceBuilder(@NotNull ValueType<V> type,
                                 @NotNull ValueType<SOURCE> sourceType, @NotNull ValueType<PARAM> paramType,
                                 @NotNull ValueHandler<SOURCE, PARAM> parser,
                                 @NotNull ValueHandler<PARAM, SOURCE> serializer) {
        super(type);
        this.sourceType = sourceType;
        this.paramType = paramType;
        this.valueParser = parser;
        this.valueSerializer = serializer;
    }

    public @NotNull SELF parse(DataFunction<SOURCE, PARAM> parser) {
        return parse((p, source) -> parser.handle(source));
    }

    public @NotNull SELF parse(@NotNull ValueHandler<SOURCE, PARAM> parser) {
        this.valueParser = parser;
        return self();
    }

    public @NotNull SELF serialize(@NotNull ValueHandler<PARAM, SOURCE> serializer) {
        this.valueSerializer = serializer;
        return self();
    }

    public @NotNull SELF serialize(@NotNull DataFunction<PARAM, SOURCE> serializer) {
        return serialize((p, value) -> serializer.handle(value));
    }

    protected ValueAdapter<PARAM> buildAdapter() {
        return new ValueAdapter<>(this.paramType)
                .parser((holder, type, data) -> {
                    SOURCE source = holder.deserialize(this.sourceType, data);
                    return this.valueParser.handle(holder, source);
                })
                .serializer((holder, type, data) -> {
                    SOURCE source = this.valueSerializer.handle(holder, data);
                    return holder.serialize(source);
                });
    }


}
