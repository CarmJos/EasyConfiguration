package cc.carm.lib.configuration.source.option;

import org.jetbrains.annotations.NotNull;

public class ConfigurationOption<V> {

    @SuppressWarnings("unchecked")
    public static <T> ConfigurationOption<T> of(@NotNull T defaultValue) {
        return of((Class<T>) defaultValue.getClass(), defaultValue);
    }

    public static <T> ConfigurationOption<T> of(@NotNull Class<T> valueClazz, @NotNull T defaultValue) {
        return new ConfigurationOption<>(valueClazz, defaultValue);
    }

    private final @NotNull Class<V> valueClazz;
    private @NotNull V defaultValue;

    public ConfigurationOption(@NotNull Class<V> valueClazz, @NotNull V defaultValue) {
        this.valueClazz = valueClazz;
        this.defaultValue = defaultValue;
    }

    @NotNull
    public Class<V> valueClass() {
        return this.valueClazz;
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