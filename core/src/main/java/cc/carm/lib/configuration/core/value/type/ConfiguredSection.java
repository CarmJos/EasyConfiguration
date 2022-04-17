package cc.carm.lib.configuration.core.value.type;

import cc.carm.lib.configuration.core.builder.ConfigBuilder;
import cc.carm.lib.configuration.core.builder.value.SectionValueBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class ConfiguredSection<V> extends CachedConfigValue<V> {

    public static <V> @NotNull SectionValueBuilder<V> builder(@NotNull Class<V> valueClass) {
        return builder().asValue(valueClass).fromSection();
    }

    protected final @NotNull Class<V> valueClass;

    protected final @NotNull ConfigValueParser<ConfigurationWrapper, V> parser;
    protected final @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> serializer;

    public ConfiguredSection(@Nullable ConfigurationProvider provider,
                             @Nullable String sectionPath, @NotNull String[] comments,
                             @NotNull Class<V> valueClass, @Nullable V defaultValue,
                             @NotNull ConfigValueParser<ConfigurationWrapper, V> parser,
                             @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> serializer) {
        super(provider, sectionPath, comments, defaultValue);
        this.valueClass = valueClass;
        this.parser = parser;
        this.serializer = serializer;
    }

    public @NotNull Class<V> getValueClass() {
        return valueClass;
    }

    public @NotNull ConfigValueParser<ConfigurationWrapper, V> getParser() {
        return parser;
    }

    public @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> getSerializer() {
        return serializer;
    }

    @Override
    public @Nullable V get() {
        if (isExpired()) { // 已过时的数据，需要重新解析一次。
            ConfigurationWrapper section = getConfiguration().getConfigurationSection(getConfigPath());
            if (section == null) return useDefault();
            try {
                // 若未出现错误，则直接更新缓存并返回。
                return updateCache(this.parser.parse(section, this.defaultValue));
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
//                getConfiguration().createSection(getSectionPath(), serializer.parse(value));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
