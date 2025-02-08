package cc.carm.lib.configuration.value;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.source.meta.ConfigurationMetaHolder;
import cc.carm.lib.configuration.source.section.ConfigurationSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ValueManifest<T> {

    protected final @NotNull ValueType<T> type;
    protected final @NotNull BiConsumer<@NotNull ConfigurationProvider<?>, @NotNull String> initializer;

    protected @Nullable ConfigurationProvider<?> provider;
    protected @Nullable String path; // Section path

    protected @NotNull Supplier<@Nullable T> defaultSupplier;


    public ValueManifest(@NotNull ValueType<T> type) {
        this(type, () -> null, EMPTY_INITIALIZER, null, null);
    }

    public ValueManifest(@NotNull T defaultValue) {
        this(ValueType.of(defaultValue), () -> defaultValue);
    }

    public ValueManifest(@NotNull ValueType<T> type, @NotNull Supplier<@Nullable T> defaultSupplier) {
        this(type, defaultSupplier, EMPTY_INITIALIZER, null, null);
    }

    public ValueManifest(@NotNull ValueType<T> type, @NotNull Supplier<@Nullable T> defaultSupplier,
                         @NotNull BiConsumer<@NotNull ConfigurationProvider<?>, @NotNull String> initializer) {
        this(type, defaultSupplier, initializer, null, null);
    }

    public ValueManifest(@NotNull ValueType<T> type, @NotNull Supplier<@Nullable T> defaultSupplier,
                         @NotNull BiConsumer<@NotNull ConfigurationProvider<?>, @NotNull String> initializer,
                         @Nullable ConfigurationProvider<?> provider, @Nullable String path) {
        this.type = type;
        this.initializer = initializer;
        this.defaultSupplier = defaultSupplier;
        this.provider = provider;
        this.path = path;
        initialize();
    }

    protected ValueManifest(@NotNull ValueManifest<T> manifest) {
        this(manifest.type, manifest.defaultSupplier, manifest.initializer, manifest.provider, manifest.path);
    }

    public void initialize(@NotNull ConfigurationProvider<?> provider, @NotNull String path) {
        this.provider = provider;
        this.path = path;
        initialize();
    }

    protected void initialize() {
        if (provider != null && path != null) this.initializer.accept(provider, path);
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

    public ConfigurationMetaHolder metadata() {
        return provider().metadata(path());
    }

    protected Object getData() {
        return config().get(path());
    }

    protected void setData(@Nullable Object value) {
        config().set(path(), value);
    }


    private static final @NotNull BiConsumer<@NotNull ConfigurationProvider<?>, @NotNull String> EMPTY_INITIALIZER = (provider, path) -> {
    };

}
