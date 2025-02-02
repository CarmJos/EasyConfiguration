package cc.carm.lib.configuration.value.impl;

import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.ValueManifest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CachedConfigValue<T> extends ConfigValue<T> {

    protected @Nullable T cachedValue;
    protected long parsedTime = -1;

    protected CachedConfigValue(@NotNull ValueManifest<T> manifest) {
        super(manifest);
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
        return this.parsedTime <= 0 || config().isExpired(this.parsedTime);
    }

    protected final T getDefaultFirst(@Nullable T value) {
        return updateCache(this.defaults() == null ? value : this.defaults());
    }

    /**
     * Get the cached value or the default value if the cached value is null
     *
     * @return the cached value or the default value
     */
    protected @Nullable T getCachedOrDefault() {
        return getCachedOrDefault(null);
    }

    /**
     * Get the cached value or the default value if the cached value is null
     *
     * @param emptyValue the value to return if the cached value and the default value are null
     * @return the cached value or the default value
     */
    @Contract("!null->!null")
    protected T getCachedOrDefault(@Nullable T emptyValue) {
        if (getCachedValue() != null) return getCachedValue();
        else if (defaults() != null) return defaults();
        else return emptyValue;
    }

}
