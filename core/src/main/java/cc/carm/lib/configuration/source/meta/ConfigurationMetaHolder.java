package cc.carm.lib.configuration.source.meta;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ConfigurationMetaHolder {

    protected final @NotNull Map<ConfigurationMetadata<?>, Object> values;

    public ConfigurationMetaHolder() {
        this(new ConcurrentHashMap<>());
    }

    public ConfigurationMetaHolder(@NotNull Map<ConfigurationMetadata<?>, Object> values) {
        this.values = values;
    }

    public @NotNull Map<ConfigurationMetadata<?>, Object> values() {
        return values;
    }

    /**
     * Get the value of option.
     *
     * @param type         {@link ConfigurationMetadata}
     * @param defaultValue Default value if the value of option is not set.
     * @param <V>          Value type
     * @return Value of option
     */
    @SuppressWarnings("unchecked")
    @Contract("_,!null -> !null")
    public <V> @Nullable V get(@NotNull ConfigurationMetadata<V> type, @Nullable V defaultValue) {
        return (V) values().getOrDefault(type, type.defaultOrSupply(defaultValue));
    }

    /**
     * Get the value of option.
     *
     * @param type         {@link ConfigurationMetadata}
     * @param defaultValue Default value if the value of option is not set.
     * @param <V>          Value type
     * @return Value of option
     */
    @SuppressWarnings("unchecked")
    @Contract("_,!null -> !null")
    public <V> @Nullable V get(@NotNull ConfigurationMetadata<V> type, Supplier<@Nullable V> defaultValue) {
        return (V) values().getOrDefault(type, type.defaultOrSupply(defaultValue));
    }

    /**
     * Get the value of option.
     *
     * @param type {@link ConfigurationMetadata}
     * @param <V>  Value type
     * @return Value of option
     */
    public <V> @Nullable V get(@NotNull ConfigurationMetadata<V> type) {
        return get(type, (V) null);
    }

    public boolean contains(@NotNull ConfigurationMetadata<?> type) {
        return values().containsKey(type) || type.hasDefaults();
    }

    /**
     * Set the value of meta, if the value is null, the meta will be removed.
     * <br> Will only be changed in current holder.
     *
     * @param type  {@link ConfigurationMetadata}
     * @param value Value of meta
     * @param <V>   Value type
     * @return Previous value of meta
     */
    @SuppressWarnings("unchecked")
    public <V> @Nullable V set(@NotNull ConfigurationMetadata<V> type, @Nullable V value) {
        if (value == null || type.isDefault(value)) {
            return (V) values().remove(type);
        } else {
            return (V) values().put(type, value);
        }
    }

    /**
     * Set the value of meta, if the value is null, the meta will not be changed.
     * <br> Will only be changed in current holder.
     *
     * @param type  {@link ConfigurationMetadata}
     * @param value Value of meta
     * @param <V>   Value type
     */
    public <V> void setIfAbsent(@NotNull ConfigurationMetadata<V> type, @Nullable V value) {
        if (value == null || type.isDefault(value)) {
            values().remove(type);
        } else {
            values().putIfAbsent(type, value);
        }
    }

    /**
     * Set the value of meta, if the value is null, the meta will not be changed.
     * <br> Will only be changed in current holder.
     *
     * @param type  {@link ConfigurationMetadata}
     * @param value Value of meta
     * @param <V>   Value type
     * @return Previous value of meta
     */
    @SuppressWarnings("unchecked")
    public <V> @Nullable V setIfPresent(@NotNull ConfigurationMetadata<V> type, @Nullable V value) {
        Object exists = values().get(type);
        if (exists == null) return null;

        if (value == null || type.isDefault(value)) {
            return (V) values().remove(type);
        } else {
            return (V) values().put(type, value);
        }
    }

}
