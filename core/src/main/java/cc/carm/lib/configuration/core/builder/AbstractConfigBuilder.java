package cc.carm.lib.configuration.core.builder;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.ValueManifest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractConfigBuilder<T, B extends AbstractConfigBuilder<T, B, P>, P extends ConfigurationProvider<?>> {

    protected final Class<? super P> providerClass;

    protected @Nullable P provider;
    protected @Nullable String path;

    protected @Nullable List<String> headerComments;
    protected @Nullable String inlineComment;

    protected @Nullable T defaultValue;

    protected AbstractConfigBuilder(Class<? super P> providerClass) {
        this.providerClass = providerClass;
    }

    protected abstract @NotNull B getThis();

    public abstract @NotNull ConfigValue<?> build();

    public @NotNull B from(@Nullable P provider) {
        this.provider = provider;
        return getThis();
    }

    public @NotNull B path(@Nullable String path) {
        this.path = path;
        return getThis();
    }

    public @NotNull B comments(@NotNull String... comments) {
        return headerComments(comments);
    }

    public @NotNull B headerComments(@NotNull String... comments) {
        return headerComments(Arrays.asList(comments));
    }

    public @NotNull B headerComments(@NotNull List<String> comments) {
        this.headerComments = comments;
        return getThis();
    }

    public @NotNull B inlineComment(@NotNull String comment) {
        this.inlineComment = comment;
        return getThis();
    }

    public @NotNull B defaults(@Nullable T defaultValue) {
        this.defaultValue = defaultValue;
        return getThis();
    }

    public @NotNull B defaults(@NotNull Supplier<@Nullable T> defaultValueSupplier) {
        return defaults(defaultValueSupplier.get());
    }

    protected @NotNull ValueManifest<T> buildManifest() {
        return ValueManifest.of(
                this.provider, this.path,
                this.headerComments, this.inlineComment, this.defaultValue
        );
    }

}
