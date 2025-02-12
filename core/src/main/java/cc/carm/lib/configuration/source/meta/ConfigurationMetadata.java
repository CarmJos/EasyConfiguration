package cc.carm.lib.configuration.source.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ConfigurationMetadata<T> {

    public static <T> ConfigurationMetadata<T> of() {
        return of(() -> null);
    }

    public static <T> ConfigurationMetadata<T> of(T defaults) {
        return of(() -> defaults);
    }

    public static <T> ConfigurationMetadata<T> of(@NotNull Supplier<@Nullable T> defaults) {
        return new ConfigurationMetadata<>(defaults);
    }

    protected Supplier<@Nullable T> defaultsSupplier;

    public ConfigurationMetadata(@NotNull Supplier<@Nullable T> defaults) {
        this.defaultsSupplier = defaults;
    }

    public boolean isDefault(@NotNull T value) {
        return value.equals(defaults());
    }

    public boolean hasDefaults() {
        return defaults() != null;
    }

    public T defaultOrSupply(@Nullable T suppliedValue) {
        return defaultOrSupply(() -> suppliedValue);
    }

    public T defaultOrSupply(Supplier<@Nullable T> suppliedValue) {
        T defaults = defaults();
        return defaults == null ? suppliedValue.get() : defaults;
    }

    public @Nullable T defaults() {
        return defaultsSupplier.get();
    }

    public void setDefaults(Supplier<T> defaultFunction) {
        this.defaultsSupplier = defaultFunction;
    }

    public void setDefaults(T value) {
        setDefaults(() -> value);
    }

}
