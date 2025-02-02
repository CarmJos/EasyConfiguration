package cc.carm.lib.configuration.builder.impl;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.function.ConfigDataFunction;
import cc.carm.lib.configuration.function.ConfigValueHandler;
import cc.carm.lib.configuration.source.section.ConfigurationSection;
import cc.carm.lib.configuration.value.ConfigValue;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractSectionBuilder<
        TYPE, PARAM,
        RESULT extends ConfigValue<TYPE>,
        SELF extends AbstractSectionBuilder<TYPE, PARAM, RESULT, SELF>
        > extends CommonConfigBuilder<TYPE, RESULT, SELF> {

    protected final @NotNull ValueType<PARAM> paramType;

    protected @NotNull ConfigValueHandler<ConfigurationSection, PARAM> parser;
    protected @NotNull ConfigValueHandler<PARAM, ? extends Map<Object, Object>> serializer;

    public AbstractSectionBuilder(@NotNull ValueType<TYPE> type, @NotNull ValueType<PARAM> paramType,
                                  @NotNull ConfigValueHandler<ConfigurationSection, PARAM> parser,
                                  @NotNull ConfigValueHandler<PARAM, ? extends Map<Object, Object>> serializer) {
        super(type);
        this.paramType = paramType;
        this.parser = parser;
        this.serializer = serializer;
    }

    public @NotNull SELF parse(ConfigDataFunction<ConfigurationSection, PARAM> valueParser) {
        return parse((p, section) -> valueParser.handle(section));
    }

    public @NotNull SELF parse(ConfigValueHandler<ConfigurationSection, PARAM> valueParser) {
        this.parser = valueParser;
        return self();
    }

    public @NotNull SELF serialize(ConfigDataFunction<PARAM, ? extends Map<Object, Object>> serializer) {
        return serialize((p, value) -> serializer.handle(value));
    }

    public @NotNull SELF serialize(ConfigValueHandler<PARAM, ? extends Map<Object, Object>> serializer) {
        this.serializer = serializer;
        return self();
    }

    public @NotNull SELF serialize(Consumer<Map<Object, Object>> serializer) {
        return serialize((p, value) -> {
            Map<Object, Object> map = new LinkedHashMap<>();
            serializer.accept(map);
            return map;
        });
    }

    protected ValueAdapter<PARAM> buildAdapter() {
        return new ValueAdapter<>(this.paramType)
                .parser((p, type, data) -> {
                    ConfigurationSection section = p.deserialize(ConfigurationSection.class, data);
                    if (section == null) return null;
                    return this.parser.handle(p, section);
                })
                .serializer((p, type, data) -> {
                    Map<Object, Object> map = this.serializer.handle(p, data);
                    return map == null || map.isEmpty() ? null : map;
                });
    }
}
