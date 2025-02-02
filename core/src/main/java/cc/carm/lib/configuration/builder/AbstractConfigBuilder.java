package cc.carm.lib.configuration.builder;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.ValueManifest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class AbstractConfigBuilder<T, B extends AbstractConfigBuilder<T, B, P>, P extends ConfigurationProvider<?>> {

    protected final Class<? super P> providerClass;

    protected @Nullable P provider;
    protected @Nullable String path;

    protected @NotNull Supplier<T> defaultValueSupplier = () -> null;

    protected AbstractConfigBuilder(Class<? super P> providerClass) {
        this.providerClass = providerClass;
    }

    protected abstract @NotNull B self();

    public abstract @NotNull ConfigValue<?> build();

    public @NotNull B from(@Nullable P provider) {
        this.provider = provider;
        return self();
    }

    public @NotNull B path(@Nullable String path) {
        this.path = path;
        return self();
    }

    public @NotNull B defaults(@Nullable T defaultValue) {
        return defaults(() -> defaultValue);
    }

    public @NotNull B defaults(@NotNull Supplier<@Nullable T> supplier) {
        this.defaultValueSupplier = supplier;
        return self();
    }

    protected @NotNull ValueManifest<T> buildManifest() {
        return null;
    }

}
