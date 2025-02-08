package cc.carm.lib.configuration.builder;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.source.meta.ConfigurationMetaHolder;
import cc.carm.lib.configuration.source.meta.ConfigurationMetadata;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.ValueManifest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
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
    protected @NotNull BiConsumer<ConfigurationProvider<?>, String> initializer = (provider, path) -> {
    };

    protected AbstractConfigBuilder(Class<? super PROVIDER> providerClass, ValueType<TYPE> type) {
        this.providerClass = providerClass;
        this.type = type;
    }

    public @NotNull ValueType<TYPE> type() {
        return type;
    }

    protected abstract @NotNull SELF self();

    public abstract @NotNull RESULT build();

    public @NotNull SELF provider(@Nullable PROVIDER provider) {
        this.provider = provider;
        return self();
    }

    public @NotNull SELF path(@Nullable String path) {
        this.path = path;
        return self();
    }

    public @NotNull SELF initializer(@NotNull BiConsumer<ConfigurationProvider<?>, String> initializer) {
        this.initializer = initializer;
        return self();
    }

    public @NotNull SELF append(@NotNull BiConsumer<ConfigurationProvider<?>, String> initializer) {
        return initializer(initializer.andThen(initializer));
    }

    public @NotNull SELF append(@NotNull Consumer<ConfigurationProvider<?>> initializer) {
        return append((provider, path) -> initializer.accept(provider));
    }

    public @NotNull SELF defaults(@Nullable TYPE defaultValue) {
        return defaults(() -> defaultValue);
    }

    public @NotNull SELF defaults(@NotNull Supplier<@Nullable TYPE> supplier) {
        this.defaultValueSupplier = supplier;
        return self();
    }

    public <M> @NotNull SELF meta(@NotNull Consumer<@NotNull ConfigurationMetaHolder> metaConsumer) {
        return append((provider, path) -> metaConsumer.accept(provider.metadata(path)));
    }

    public <M> @NotNull SELF meta(@NotNull ConfigurationMetadata<M> type, @Nullable M value) {
        return meta(holder -> holder.set(type, value));
    }

    protected @NotNull ValueManifest<TYPE> buildManifest() {
        return new ValueManifest<>(
                type(), this.defaultValueSupplier, this.initializer,
                this.provider, this.path
        );
    }

}
