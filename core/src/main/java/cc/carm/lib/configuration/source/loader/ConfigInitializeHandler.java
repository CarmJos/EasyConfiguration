package cc.carm.lib.configuration.source.loader;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ConfigInitializeHandler<T> {

    static <T> ConfigInitializeHandler<T> start() {
        return (provider, path, value) -> {
        };
    }

    void whenInitialize(@NotNull ConfigurationHolder<?> holder, @Nullable String path, @NotNull T value) throws Exception;

    default ConfigInitializeHandler<T> andThen(ConfigInitializeHandler<T> after) {
        return (provider, path, value) -> {
            whenInitialize(provider, path, value);
            after.whenInitialize(provider, path, value);
        };
    }

    default ConfigInitializeHandler<T> compose(ConfigInitializeHandler<T> before) {
        return (provider, path, value) -> {
            before.whenInitialize(provider, path, value);
            whenInitialize(provider, path, value);
        };
    }


}
