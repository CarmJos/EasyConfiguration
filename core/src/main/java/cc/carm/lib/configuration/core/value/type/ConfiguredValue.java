package cc.carm.lib.configuration.core.value.type;

import cc.carm.lib.configuration.core.builder.value.ConfigValueBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfiguredValue<V> extends CachedConfigValue<V> {

    public static <V> ConfigValueBuilder<V> builderOf(Class<V> valueClass) {
        return builder().asValue(valueClass);
    }

    @SuppressWarnings("unchecked")
    public static <V> ConfiguredValue<V> of(@NotNull V defaultValue) {
        return of((Class<V>) defaultValue.getClass(), defaultValue);
    }

    public static <V> ConfiguredValue<V> of(Class<V> valueClass) {
        return of(valueClass, null);
    }

    public static <V> ConfiguredValue<V> of(Class<V> valueClass, @Nullable V defaultValue) {
        return builderOf(valueClass).fromObject().defaults(defaultValue).build();
    }

    protected final @NotNull Class<V> valueClass;

    protected final @NotNull ConfigValueParser<Object, V> parser;
    protected final @NotNull ConfigDataFunction<V, Object> serializer;

    public ConfiguredValue(@NotNull ValueManifest<V> manifest, @NotNull Class<V> valueClass,
                           @NotNull ConfigValueParser<Object, V> parser,
                           @NotNull ConfigDataFunction<V, Object> serializer) {
        super(manifest);
        this.valueClass = valueClass;
        this.parser = parser;
        this.serializer = serializer;
    }

    /**
     * @return Value's type class
     */
    public @NotNull Class<V> getValueClass() {
        return valueClass;
    }

    /**
     * @return Value's parser, cast value from base object.
     */
    public @NotNull ConfigValueParser<Object, V> getParser() {
        return parser;
    }

    /**
     * @return Value's serializer, serialize value to base object.
     */
    public @NotNull ConfigDataFunction<V, Object> getSerializer() {
        return serializer;
    }

    @Override
    public V get() {
        if (!isExpired()) return getCachedOrDefault();
        // Data that is outdated and needs to be parsed again.

        Object value = getValue();
        if (value == null) return getDefaultValue(); // 获取的值不存在，直接使用默认值。
        try {
            // If there are no errors, update the cache and return.
            return updateCache(this.parser.parse(value, this.defaultValue));
        } catch (Exception e) {
            // There was a parsing error, prompted and returned the default value.
            e.printStackTrace();
            return getDefaultValue();
        }

    }

    /**
     * Set the value of the configuration path.
     * Will use {@link #getSerializer()} to serialize the value.
     *
     * @param value The value to be set
     */
    @Override
    public void set(V value) {
        updateCache(value);
        if (value == null) setValue(null);
        else {
            try {
                setValue(serializer.parse(value));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

