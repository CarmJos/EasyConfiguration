package cc.carm.lib.configuration.builder.value;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.function.ConfigDataFunction;
import cc.carm.lib.configuration.function.ConfigValueHandler;
import cc.carm.lib.configuration.source.section.ConfigurationSection;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SectionValueBuilder<V> extends CommonConfigBuilder<V, SectionValueBuilder<V>> {

    protected final @NotNull ValueType<V> valueType;

    protected @NotNull ConfigValueHandler<ConfigurationSection, V> parser;
    protected @NotNull ConfigValueHandler<V, ? extends Map<Object, Object>> serializer;

    public SectionValueBuilder(@NotNull ValueType<V> valueType) {
        this(valueType, ConfigValueHandler.required(), ConfigValueHandler.required());
    }

    public SectionValueBuilder(@NotNull ValueType<V> valueType,
                               @NotNull ConfigValueHandler<ConfigurationSection, V> parser,
                               @NotNull ConfigValueHandler<V, ? extends Map<Object, Object>> serializer) {
        this.valueType = valueType;
        this.parser = parser;
        this.serializer = serializer;
    }

    @Override
    protected @NotNull SectionValueBuilder<V> self() {
        return this;
    }

    public @NotNull SectionValueBuilder<V> parse(ConfigDataFunction<ConfigurationSection, V> valueParser) {
        return parse((p, section) -> valueParser.handle(section));
    }

    public @NotNull SectionValueBuilder<V> parse(ConfigValueHandler<ConfigurationSection, V> valueParser) {
        this.parser = valueParser;
        return this;
    }

    public @NotNull SectionValueBuilder<V> serialize(ConfigDataFunction<V, ? extends Map<Object, Object>> serializer) {
        return serialize((p, value) -> serializer.handle(value));
    }

    public @NotNull SectionValueBuilder<V> serialize(ConfigValueHandler<V, ? extends Map<Object, Object>> serializer) {
        this.serializer = serializer;
        return this;
    }

    public @NotNull SectionValueBuilder<V> serialize(Consumer<Map<Object, Object>> serializer) {
        return serialize((p, value) -> {
            Map<Object, Object> map = new LinkedHashMap<>();
            serializer.accept(map);
            return map;
        });
    }

    @Override
    public @NotNull ConfiguredValue<V> build() {
        return ConfiguredValue.of(
                buildManifest(),
                (p, type, data) -> {
                    ConfigurationSection section = p.deserialize(ConfigurationSection.class, data);
                    if (section == null) return null;
                    return this.parser.handle(p, section);
                },
                (p, type, data) -> {
                    Map<Object, Object> map = this.serializer.handle(p, data);
                    return map == null || map.isEmpty() ? null : map; // Map is a type of original data
                }
        );
    }

}
