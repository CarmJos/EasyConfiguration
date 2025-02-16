package cc.carm.lib.configuration.function;


import cc.carm.lib.configuration.source.ConfigurationHolder;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ValueConsumer<U, T> {

    void accept(@NotNull ConfigurationHolder<?> holder, @NotNull U unit, @NotNull T data) throws Exception;

    default ValueConsumer<U, T> andThen(ValueConsumer<? super T, ? super U> after) {
        return (holder, unit, data) -> {
            accept(holder, unit, data);
            after.accept(holder, data, unit);
        };
    }

}


