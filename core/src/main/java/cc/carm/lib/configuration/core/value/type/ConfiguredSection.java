package cc.carm.lib.configuration.core.value.type;

import cc.carm.lib.configuration.core.builder.value.SectionValueBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConfiguredSection<V> extends CachedConfigValue<V> {

    public static <V> @NotNull SectionValueBuilder<V> builder(@NotNull Class<V> valueClass) {
        return builder().asValue(valueClass).fromSection();
    }

    protected final @NotNull Class<V> valueClass;

    protected final @NotNull ConfigValueParser<ConfigurationWrapper, V> parser;
    protected final @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> serializer;

    public ConfiguredSection(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                             @Nullable List<String> headerComments, @Nullable String inlineComments,
                             @NotNull Class<V> valueClass, @Nullable V defaultValue,
                             @NotNull ConfigValueParser<ConfigurationWrapper, V> parser,
                             @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> serializer) {
        super(provider, sectionPath, headerComments, inlineComments, defaultValue);
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
        if (isExpired()) { // ????????????????????????????????????????????????
            ConfigurationWrapper section = getConfiguration().getConfigurationSection(getConfigPath());
            if (section == null) return useDefault();
            try {
                // ??????????????????????????????????????????????????????
                return updateCache(this.parser.parse(section, this.defaultValue));
            } catch (Exception e) {
                // ???????????????????????????????????????????????????
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
