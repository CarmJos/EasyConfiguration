package cc.carm.lib.configuration.builder.value;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.function.ConfigDataFunction;
import cc.carm.lib.configuration.function.ConfigValueHandler;
import cc.carm.lib.configuration.source.section.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ConfigValueBuilder<V> {

    protected final @NotNull ValueType<V> type;

    public ConfigValueBuilder(@NotNull ValueType<V> type) {
        this.type = type;
    }

    public @NotNull <S> SourceValueBuilder<S, V> from(@NotNull Class<S> clazz) {
        return new SourceValueBuilder<>(ValueType.of(clazz), this.type);
    }

    public @NotNull <S> SourceValueBuilder<S, V> from(@NotNull ValueType<S> sourceType) {
        return new SourceValueBuilder<>(sourceType, this.type);
    }

    public @NotNull <S> SourceValueBuilder<S, V> from(@NotNull ValueType<S> sourceType,
                                                      @NotNull ConfigValueHandler<S, V> valueParser,
                                                      @NotNull ConfigValueHandler<V, S> valueSerializer) {
        return new SourceValueBuilder<>(sourceType, this.type, valueParser, valueSerializer);
    }

    public @NotNull SourceValueBuilder<String, V> fromString() {
        return from(String.class);
    }

    public @NotNull SectionValueBuilder<V> fromSection() {
        return new SectionValueBuilder<>(this.type);
    }

    public @NotNull SectionValueBuilder<V> fromSection(@NotNull ConfigValueHandler<ConfigurationSection, V> valueParser,
                                                       @NotNull ConfigValueHandler<V, ? extends Map<Object, Object>> valueSerializer) {
        return new SectionValueBuilder<>(this.type, valueParser, valueSerializer);
    }

}
