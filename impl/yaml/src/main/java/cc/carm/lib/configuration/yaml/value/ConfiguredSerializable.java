package cc.carm.lib.configuration.yaml.value;

import cc.carm.lib.configuration.yaml.YAMLValue;
import cc.carm.lib.configuration.yaml.YAMLConfigProvider;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ConfiguredSerializable<T extends ConfigurationSerializable> extends YAMLValue<T> {

    public static <V extends ConfigurationSerializable> ConfiguredSerializable<V> of(@NotNull Class<V> valueClass) {
        return of(valueClass, null);
    }

    public static <V extends ConfigurationSerializable> ConfiguredSerializable<V> of(@NotNull Class<V> valueClass,
                                                                                             @Nullable V defaultValue) {
        return builder().ofSerializable(valueClass).defaults(defaultValue).build();
    }

    protected final @NotNull Class<T> valueClass;

    public ConfiguredSerializable(@Nullable YAMLConfigProvider provider,
                                  @Nullable String configPath, @NotNull String[] comments,
                                  @NotNull Class<T> valueClass, @Nullable T defaultValue) {
        super(provider, configPath, comments, defaultValue);
        this.valueClass = valueClass;
    }

    @Override
    public @Nullable T get() {
        if (isExpired()) { // 已过时的数据，需要重新解析一次。
            try {
                // 若未出现错误，则直接更新缓存并返回。
                return updateCache(getYAMLConfig().getSerializable(getConfigPath(), valueClass, getDefaultValue()));
            } catch (Exception e) {
                // 出现了解析错误，提示并返回默认值。
                e.printStackTrace();
                return useDefault();
            }
        } else return Optional.ofNullable(getCachedValue()).orElse(defaultValue);
    }

    @Override
    public void set(@Nullable T value) {
        updateCache(value);
        setValue(value);
    }


}
