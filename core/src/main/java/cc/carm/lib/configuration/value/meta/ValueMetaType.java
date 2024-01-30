package cc.carm.lib.configuration.value.meta;

import cc.carm.lib.configuration.value.ValueManifest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class ValueMetaType<T> {

    public static <T> ValueMetaType<T> of() {
        return of(() -> null);
    }

    public static <T> ValueMetaType<T> of(T defaults) {
        return of(() -> defaults);
    }

    public static <T> ValueMetaType<T> of(@NotNull Supplier<@Nullable T> defaults) {
        return of(v -> defaults.get());
    }

    public static <T> ValueMetaType<T> of(@NotNull Function<ValueManifest<?>, @Nullable T> defaults) {
        return new ValueMetaType<>(defaults);
    }

    protected Function<ValueManifest<?>, @Nullable T> defaultFunction;

    public ValueMetaType(@NotNull Function<ValueManifest<?>, @Nullable T> defaults) {
        this.defaultFunction = defaults;
    }

    public boolean isDefault(ValueManifest<?> manifest, @NotNull T value) {
        return value.equals(defaults(manifest));
    }

    public boolean hasDefaults(ValueManifest<?> manifest) {
        return defaults(manifest) != null;
    }

    public T getDefault(ValueManifest<?> manifest, @Nullable T suppliedValue) {
        T defaults = defaults(manifest);
        return defaults == null ? suppliedValue : defaults;
    }

    public @Nullable T defaults(ValueManifest<?> manifest) {
        return defaultFunction.apply(manifest);
    }

    public void setDefaults(Function<ValueManifest<?>, T> defaultFunction) {
        this.defaultFunction = defaultFunction;
    }

    public void setDefaults(T value) {
        setDefaults((v) -> value);
    }

}
