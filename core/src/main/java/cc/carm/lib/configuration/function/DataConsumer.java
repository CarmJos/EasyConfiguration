package cc.carm.lib.configuration.function;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DataConsumer<T> {

    void accept(@NotNull T data) throws Exception;

    default DataConsumer<T> andThen(DataConsumer<? super T> after) {
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }


}
