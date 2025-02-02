package cc.carm.lib.configuration.builder;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.ValueManifest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class AbstractConfigBuilder<
        TYPE, RESULT extends ConfigValue<TYPE>, PROVIDER extends ConfigurationProvider<?>,
        SELF extends AbstractConfigBuilder<TYPE, RESULT, PROVIDER, SELF>
        > {

    protected final Class<? super PROVIDER> providerClass;
    protected final ValueType<TYPE> type;

    protected @Nullable PROVIDER provider;
    protected @Nullable String path;

    protected @NotNull Supplier<TYPE> defaultValueSupplier = () -> null;

    protected AbstractConfigBuilder(Class<? super PROVIDER> providerClass, ValueType<TYPE> type) {
        this.providerClass = providerClass;
        this.type = type;
    }

    public @NotNull ValueType<TYPE> type() {
        return type;
    }

    protected abstract @NotNull SELF self();

    public abstract @NotNull RESULT build();

    public @NotNull SELF from(@Nullable PROVIDER provider) {
        this.provider = provider;
        return self();
    }

    public @NotNull SELF path(@Nullable String path) {
        this.path = path;
        return self();
    }

    public @NotNull SELF defaults(@Nullable TYPE defaultValue) {
        return defaults(() -> defaultValue);
    }

    public @NotNull SELF defaults(@NotNull Supplier<@Nullable TYPE> supplier) {
        this.defaultValueSupplier = supplier;
        return self();
    }

    protected @NotNull ValueManifest<TYPE> buildManifest() {
        return new ValueManifest<>(type(), this.provider, this.path, this.defaultValueSupplier);
    }

}
