package cc.carm.lib.configuration.builder;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.ConfigurationHolder;
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
        TYPE, RESULT extends ConfigValue<TYPE>, HOLDER extends ConfigurationHolder<?>,
        SELF extends AbstractConfigBuilder<TYPE, RESULT, HOLDER, SELF>
        > {

    protected final Class<? super HOLDER> providerClass;
    protected final ValueType<TYPE> type;

    protected @Nullable HOLDER holder;
    protected @Nullable String path;

    protected @NotNull Supplier<@Nullable TYPE> defaultValueSupplier = () -> null;
    protected @NotNull BiConsumer<ConfigurationHolder<?>, String> initializer = (h, p) -> {
    };

    protected AbstractConfigBuilder(Class<? super HOLDER> providerClass, ValueType<TYPE> type) {
        this.providerClass = providerClass;
        this.type = type;
    }

    public @NotNull ValueType<TYPE> type() {
        return type;
    }

    protected abstract @NotNull SELF self();

    public abstract @NotNull RESULT build();

    public @NotNull SELF holder(@Nullable HOLDER holder) {
        this.holder = holder;
        return self();
    }

    public @NotNull SELF path(@Nullable String path) {
        this.path = path;
        return self();
    }

    public @NotNull SELF initializer(@NotNull BiConsumer<ConfigurationHolder<?>, String> initializer) {
        this.initializer = initializer;
        return self();
    }

    public @NotNull SELF append(@NotNull BiConsumer<ConfigurationHolder<?>, String> initializer) {
        return initializer(initializer.andThen(initializer));
    }

    public @NotNull SELF append(@NotNull Consumer<ConfigurationHolder<?>> initializer) {
        return append((provider, valuePath) -> initializer.accept(provider));
    }

    public @NotNull SELF defaults(@Nullable TYPE defaultValue) {
        return defaults(() -> defaultValue);
    }

    public @NotNull SELF defaults(@NotNull Supplier<@Nullable TYPE> supplier) {
        this.defaultValueSupplier = supplier;
        return self();
    }

    public <M> @NotNull SELF meta(@NotNull Consumer<@NotNull ConfigurationMetaHolder> metaConsumer) {
        return append((h, p) -> metaConsumer.accept(h.metadata(p)));
    }

    public <M> @NotNull SELF meta(@NotNull ConfigurationMetadata<M> type, @Nullable M value) {
        return meta(h -> h.set(type, value));
    }

    protected @NotNull ValueManifest<TYPE> buildManifest() {
        return new ValueManifest<>(
                type(), this.defaultValueSupplier, this.initializer,
                this.holder, this.path
        );
    }

}
