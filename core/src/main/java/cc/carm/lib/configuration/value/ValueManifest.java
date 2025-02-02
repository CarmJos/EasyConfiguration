package cc.carm.lib.configuration.value;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.source.section.ConfigurationSource;
import cc.carm.lib.configuration.meta.PathMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class ValueManifest<T> {

    protected final @NotNull ValueType<T> type;
    protected @Nullable ConfigurationProvider<?> provider;
    protected @Nullable String path; // Section path

    protected @NotNull Supplier<@Nullable T> defaultSupplier;

    public ValueManifest(ValueType<T> type) {
        this(type, null, null, () -> null);
    }

    public ValueManifest(@NotNull T defaultValue) {
        this(ValueType.of(defaultValue), null, null, () -> defaultValue);
    }

    public ValueManifest(@NotNull ValueType<T> type, @NotNull Supplier<@Nullable T> defaultSupplier) {
        this(type, null, null, defaultSupplier);
    }

    public ValueManifest(@NotNull ValueType<T> type,
                         @Nullable ConfigurationProvider<?> provider, @Nullable String path,
                         @NotNull Supplier<@Nullable T> defaultSupplier) {
        this.type = type;
        this.provider = provider;
        this.path = path;
        this.defaultSupplier = defaultSupplier;
    }

    public @NotNull ValueType<T> type() {
        return this.type;
    }

    public void provider(@NotNull ConfigurationProvider<?> provider) {
        this.provider = provider;
    }

    public void path(@NotNull String path) {
        this.path = path;
    }

    public @Nullable T defaults() {
        return this.defaultSupplier.get();
    }

    public void defaults(@Nullable T defaultValue) {
        defaults(() -> defaultValue);
    }

    public void defaults(@NotNull Supplier<@Nullable T> defaultValue) {
        this.defaultSupplier = defaultValue;
    }

    public boolean hasDefaults() {
        return this.defaultSupplier.get() != null;
    }

    public @NotNull String path() {
        if (path != null) return path;
        else throw new IllegalStateException("No section path provided.");
    }

    public @NotNull ConfigurationProvider<?> provider() {
        if (this.provider != null) return this.provider;
        throw new IllegalStateException("Value does not have a provider.");
    }

    public @NotNull ConfigurationSource<?, ?> config() {
        return provider().source();
    }

    protected Object getData() {
        return config().get(path());
    }

    protected void setData(@Nullable Object value) {
        config().set(path(), value);
    }

    public Map<PathMetadata<?>, Object> metadata() {
        return provider().metadata(path());
    }

}
