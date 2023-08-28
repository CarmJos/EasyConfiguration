package cc.carm.lib.configuration.core.builder.list;

import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class ConfigListBuilder<V> {

    protected final @NotNull Class<V> valueClass;

    public ConfigListBuilder(@NotNull Class<V> valueClass) {
        this.valueClass = valueClass;
    }

    public @NotNull <S> SourceListBuilder<S, V> from(@NotNull Class<? super S> sourceClass,
                                                     @NotNull ConfigDataFunction<Object, S> sourceParser,
                                                     @NotNull ConfigDataFunction<S, V> valueParser,
                                                     @NotNull ConfigDataFunction<V, S> valueSerializer,
                                                     @NotNull ConfigDataFunction<S, Object> sourceSerializer) {
        return new SourceListBuilder<>(sourceClass, sourceParser, this.valueClass, valueParser, valueSerializer, sourceSerializer);
    }


    public @NotNull <S> SourceListBuilder<S, V> from(Class<S> sourceClass) {
        return from(sourceClass,
                ConfigDataFunction.required(), ConfigDataFunction.required(),
                ConfigDataFunction.required(), ConfigDataFunction.required()
        );
    }

    public @NotNull SourceListBuilder<Object, V> fromObject() {
        return from(
                Object.class, ConfigDataFunction.identity(),
                ConfigDataFunction.castObject(valueClass),
                ConfigDataFunction.toObject(), ConfigDataFunction.toObject()
        );
    }

    public @NotNull SourceListBuilder<String, V> fromString() {
        return from(
                String.class, ConfigDataFunction.castToString(),
                ConfigDataFunction.castFromString(this.valueClass),
                ConfigDataFunction.castToString(), ConfigDataFunction.toObject()
        );
    }

    public @NotNull SourceListBuilder<Map<String, Object>, V> fromMap() {
        return from(
                Map.class, obj -> (Map<String, Object>) obj,
                ConfigDataFunction.required(),
                ConfigDataFunction.required(),
                ConfigDataFunction.toObject()
        );
    }

}
