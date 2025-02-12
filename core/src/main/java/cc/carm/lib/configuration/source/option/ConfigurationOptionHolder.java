package cc.carm.lib.configuration.source.option;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigurationOptionHolder {

    public static @NotNull ConfigurationOptionHolder of(@NotNull Map<ConfigurationOption<?>, Object> options) {
        return new ConfigurationOptionHolder(new ConcurrentHashMap<>(options));
    }

    protected final Map<ConfigurationOption<?>, Object> options;

    public ConfigurationOptionHolder() {
        this(new ConcurrentHashMap<>());
    }

    public ConfigurationOptionHolder(Map<ConfigurationOption<?>, Object> options) {
        this.options = options;
    }

    public @NotNull Map<ConfigurationOption<?>, Object> values() {
        return options;
    }

    /**
     * Get the value of option.
     *
     * @param type {@link ConfigurationOption}
     * @param <V>  Value type
     * @return Value of option
     */
    @SuppressWarnings("unchecked")
    public <V> @NotNull V get(@NotNull ConfigurationOption<V> type) {
        return Optional.ofNullable(values().get(type)).map(v -> (V) v).orElseGet(type::defaults);
    }

    /**
     * Set the value of option.
     *
     * @param type  {@link ConfigurationOption}
     * @param value Value of option
     * @param <V>   Value type
     * @return Previous value of option
     */
    @SuppressWarnings("unchecked")
    public <V> @Nullable V set(@NotNull ConfigurationOption<V> type, @Nullable V value) {
        if (value == null) {
            return (V) values().remove(type);
        } else {
            return (V) values().put(type, value);
        }
    }

    /**
     * Set the value of option to option's {@link ConfigurationOption#defaults()}.
     *
     * @param type {@link ConfigurationOption}
     * @param <V>  Value type
     * @return Previous value of option
     */
    public <V> @Nullable V clear(@NotNull ConfigurationOption<V> type) {
        return set(type, null);
    }

}
