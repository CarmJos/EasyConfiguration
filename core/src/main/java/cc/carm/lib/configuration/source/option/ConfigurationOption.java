package cc.carm.lib.configuration.source.option;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ConfigurationOption<V> {

    public static <T> ConfigurationOption<T> of(@NotNull T defaultValue) {
        return new ConfigurationOption<>(defaultValue);
    }

    public static <T> ConfigurationOption<T> of(@NotNull Supplier<T> defaultValue) {
        return of(defaultValue.get());
    }

    private @NotNull V defaultValue;

    public ConfigurationOption(@NotNull V defaultValue) {
        this.defaultValue = defaultValue;
    }

    public @NotNull V defaults() {
        return defaultValue;
    }

    /**
     * Set the default value of option.
     *
     * @param defaultValue Default value
     */
    public void defaults(@NotNull V defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isDefault(@NotNull V value) {
        return value.equals(defaultValue);
    }

}