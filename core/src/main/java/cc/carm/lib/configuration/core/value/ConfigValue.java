package cc.carm.lib.configuration.core.value;

import cc.carm.lib.configuration.core.builder.ConfigBuilder;
import cc.carm.lib.configuration.core.builder.value.ConfigValueBuilder;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public abstract class ConfigValue<T> {

    public static <V> ConfigValueBuilder<V> builder(Class<V> valueClass) {
        return ConfigBuilder.asValue(valueClass);
    }

    public static <V> ConfigValue<V> of(Class<V> valueClass) {
        return builder(valueClass).fromObject().build();
    }

    protected @Nullable T defaultValue;

    protected @Nullable ConfigurationProvider provider;
    protected @Nullable String configPath;
    protected @NotNull String[] comments;

    public ConfigValue(@Nullable ConfigurationProvider provider, @Nullable String configPath,
                       @NotNull String[] comments, @Nullable T defaultValue) {
        this.provider = provider;
        this.configPath = configPath;
        this.comments = comments;

        this.defaultValue = defaultValue;
    }

    public void initialize(@NotNull ConfigurationProvider provider, @NotNull String configPath,
                           @NotNull String... comments) {
        if (this.provider == null) this.provider = provider;
        if (this.configPath == null) this.configPath = configPath;
        if (this.comments.length == 0) this.comments = comments;

        this.provider.setComments(this.configPath, this.comments);
        get();
    }

    public @Nullable T getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(@Nullable T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public abstract @Nullable T get();

    public @Nullable T getOrDefault() {
        return Optional.ofNullable(get()).orElse(getDefaultValue());
    }

    public @NotNull T getNotNull() {
        return Objects.requireNonNull(getOrDefault(), "Value(" + configPath + ") is null.");
    }

    public @NotNull Optional<@Nullable T> getOptional() {
        return Optional.ofNullable(get());
    }

    public abstract void set(@Nullable T value);

    public void setDefault() {
        Optional.ofNullable(getDefaultValue()).ifPresent(this::set);
    }

    public @NotNull ConfigurationProvider getProvider() {
        return Optional.ofNullable(this.provider)
                .orElseThrow(() -> new IllegalStateException("Value(" + configPath + ") does not have a provider."));
    }

    public final @NotNull ConfigurationWrapper getConfiguration() {
        try {
            return getProvider().getConfiguration();
        } catch (Exception ex) {
            throw new IllegalStateException("Value(" + configPath + ") has not been initialized", ex);
        }
    }

    public @NotNull String getConfigPath() {
        return Optional.ofNullable(this.configPath)
                .orElseThrow(() -> new IllegalStateException("No section path provided."));
    }

    protected Object getValue() {
        return getConfiguration().get(getConfigPath());
    }

    protected void setValue(@Nullable Object value) {
        getConfiguration().set(getConfigPath(), value);
    }

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }

}
