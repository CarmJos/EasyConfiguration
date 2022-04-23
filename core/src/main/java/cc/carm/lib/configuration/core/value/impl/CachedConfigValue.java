package cc.carm.lib.configuration.core.value.impl;

import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CachedConfigValue<T> extends ConfigValue<T> {


    protected @Nullable T cachedValue;
    protected long parsedTime = -1;

    public CachedConfigValue(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                             @Nullable ConfigCommentInfo comments, @Nullable T defaultValue) {
        super(provider, sectionPath, comments, defaultValue);
    }

    protected T updateCache(T value) {
        this.parsedTime = System.currentTimeMillis();
        this.cachedValue = value;
        return getCachedValue();
    }

    public @Nullable T getCachedValue() {
        return cachedValue;
    }

    public boolean isExpired() {
        return this.parsedTime <= 0 || getProvider().isExpired(this.parsedTime);
    }

    protected final T useDefault() {
        return useOrDefault(null);
    }

    protected final T useOrDefault(@Nullable T value) {
        return updateCache(this.defaultValue == null ? value : this.defaultValue);
    }

}
