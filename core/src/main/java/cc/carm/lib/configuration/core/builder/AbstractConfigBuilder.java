package cc.carm.lib.configuration.core.builder;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractConfigBuilder<B extends AbstractConfigBuilder<B, T>, T> {


    protected @Nullable ConfigurationProvider provider;
    protected @Nullable String path;

    protected @NotNull String[] comments = new String[0];
    protected @Nullable T defaultValue;

    protected abstract @NotNull B getThis();

    public abstract @NotNull ConfigValue<?> build();

    public @NotNull B from(@Nullable ConfigurationProvider provider) {
        this.provider = provider;
        return getThis();
    }

    public @NotNull B path(@Nullable String path) {
        this.path = path;
        return getThis();
    }

    public @NotNull B comments(@NotNull String... comments) {
        this.comments = comments;
        return getThis();
    }

    public @NotNull B defaults(@Nullable T defaultValue) {
        this.defaultValue = defaultValue;
        return getThis();
    }


}
