package cc.carm.lib.configuration.builder.list;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.function.ValueHandler;
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
        return new SourceListBuilder<>(sourceType, type, ValueHandler.required(), ValueHandler.required(), ArrayList::new);
    }

    public @NotNull SourceListBuilder<String, V> fromString() {
        return new SourceListBuilder<>(ValueType.STRING, type, ValueHandler.required(), ValueHandler.stringValue(), ArrayList::new);
    }

    public @NotNull SectionListBuilder<V> fromSection() {
        return new SectionListBuilder<>(type, ValueHandler.required(), ValueHandler.required(), ArrayList::new);
    }


}
