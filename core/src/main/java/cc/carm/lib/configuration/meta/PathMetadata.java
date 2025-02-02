package cc.carm.lib.configuration.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class PathMetadata<T> {

    public static <T> PathMetadata<T> of() {
        return of(() -> null);
    }

    public static <T> PathMetadata<T> of(T defaults) {
        return of(() -> defaults);
    }

    public static <T> PathMetadata<T> of(@NotNull Supplier<@Nullable T> defaults) {
        return of(v -> defaults.get());
    }

    public static <T> PathMetadata<T> of(@NotNull Function<String, @Nullable T> defaults) {
        return new PathMetadata<>(defaults);
    }

    protected Function<String, @Nullable T> defaultFunction;

    public PathMetadata(@NotNull Function<String, @Nullable T> defaults) {
        this.defaultFunction = defaults;
    }

    public boolean isDefault(String path, @NotNull T value) {
        return value.equals(defaults(path));
    }

    public boolean hasDefaults(String path) {
        return defaults(path) != null;
    }

    public T getDefault(String path, @Nullable T suppliedValue) {
        T defaults = defaults(path);
        return defaults == null ? suppliedValue : defaults;
    }

    public @Nullable T defaults(String path) {
        return defaultFunction.apply(path);
    }

    public void setDefaults(Function<String, T> defaultFunction) {
        this.defaultFunction = defaultFunction;
    }

    public void setDefaults(T value) {
        setDefaults((v) -> value);
    }

}
