package cc.carm.lib.configuration.core.builder;

import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractConfigBuilder<T, B extends AbstractConfigBuilder<T, B, P>, P extends ConfigurationProvider<?>> {

    protected final Class<? super P> providerClass;

    protected @Nullable P provider;
    protected @Nullable String path;

    protected @NotNull String[] comments = new String[0];
    protected boolean startWrap = true;
    protected boolean endWrap = false;

    protected @Nullable T defaultValue;

    public AbstractConfigBuilder(Class<? super P> providerClass) {
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
        this.comments = comments;
        return getThis();
    }

    public @NotNull B setStartWarp(boolean enable) {
        this.startWrap = enable;
        return getThis();
    }

    public @NotNull B startWarp() {
        return setStartWarp(true);
    }

    public @NotNull B setEndWarp(boolean enable) {
        this.endWrap = enable;
        return getThis();
    }

    public @NotNull B endWarp() {
        return setEndWarp(true);
    }

    public @NotNull B defaults(@Nullable T defaultValue) {
        this.defaultValue = defaultValue;
        return getThis();
    }

    protected @Nullable ConfigCommentInfo buildComments() {
        ConfigCommentInfo info = ConfigCommentInfo.of(this.comments, this.startWrap, this.endWrap);
        if (info.equals(ConfigCommentInfo.defaults())) return null;
        else return info;
    }


}
