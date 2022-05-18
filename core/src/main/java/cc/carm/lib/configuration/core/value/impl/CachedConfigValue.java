package cc.carm.lib.configuration.core.value.impl;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.ConfigValue;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class CachedConfigValue<T> extends ConfigValue<T> {


    protected @Nullable T cachedValue;
    protected long parsedTime = -1;

    public CachedConfigValue(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                             @Nullable List<String> headerComments, @Nullable String inlineComments,
                             @Nullable T defaultValue) {
        super(provider, sectionPath, headerComments, inlineComments, defaultValue);
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
