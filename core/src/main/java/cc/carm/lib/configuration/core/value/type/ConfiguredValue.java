package cc.carm.lib.configuration.core.value.type;

import cc.carm.lib.configuration.core.builder.value.ConfigValueBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ConfiguredValue<V> extends CachedConfigValue<V> {

    public static <V> ConfigValueBuilder<V> builder(Class<V> valueClass) {
        return builder().asValue(valueClass);
    }

    public static <V> ConfiguredValue<V> of(Class<V> valueClass) {
        return of(valueClass, null);
    }

    public static <V> ConfiguredValue<V> of(Class<V> valueClass, @Nullable V defaultValue) {
        return builder(valueClass).fromObject().defaults(defaultValue).build();
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

    public @NotNull Class<V> getValueClass() {
        return valueClass;
    }

    public @NotNull ConfigValueParser<Object, V> getParser() {
        return parser;
    }

    @Override
    public V get() {
        if (isExpired()) { // 已过时的数据，需要重新解析一次。
            Object value = getValue();
            if (value == null) return useDefault(); // 获取的值不存在，直接使用默认值。
            try {
                // 若未出现错误，则直接更新缓存并返回。
                return updateCache(this.parser.parse(value, this.defaultValue));
            } catch (Exception e) {
                // 出现了解析错误，提示并返回默认值。
                e.printStackTrace();
                return useDefault();
            }
        } else return Optional.ofNullable(getCachedValue()).orElse(defaultValue);
    }

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

