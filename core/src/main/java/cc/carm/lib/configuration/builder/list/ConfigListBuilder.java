package cc.carm.lib.configuration.builder.list;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.function.ConfigValueHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConfigListBuilder<V> {

    protected final @NotNull ValueType<V> type;

    public ConfigListBuilder(@NotNull ValueType<V> type) {
        this.type = type;
    }

    public <S> @NotNull SourceListBuilder<S, V> from(@NotNull Class<S> sourceType) {
        return from(ValueType.of(sourceType));
    }

    public <S> @NotNull SourceListBuilder<S, V> from(@NotNull ValueType<S> sourceType) {
        return new SourceListBuilder<>(sourceType, type, ConfigValueHandler.required(), ConfigValueHandler.required(), ArrayList::new);
    }

    public @NotNull SourceListBuilder<String, V> fromString() {
        return from(String.class);
    }

    public @NotNull SectionListBuilder<V> fromSection() {
        return new SectionListBuilder<>(type, ConfigValueHandler.required(), ConfigValueHandler.required(), ArrayList::new);
    }


}
