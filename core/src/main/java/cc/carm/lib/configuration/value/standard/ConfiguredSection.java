package cc.carm.lib.configuration.value.standard;

import cc.carm.lib.configuration.core.builder.value.SectionValueBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ConfiguredSection<V> extends CachedConfigValue<V> {

    public static <V> @NotNull SectionValueBuilder<V> builderOf(@NotNull Class<V> valueClass) {
        return builder().asValue(valueClass).fromSection();
    }

    protected final @NotNull Class<V> valueClass;

    protected final @NotNull ConfigValueParser<ConfigurationWrapper<?>, V> parser;
    protected final @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> serializer;

    public ConfiguredSection(@NotNull ValueManifest<V> manifest, @NotNull Class<V> valueClass,
                             @NotNull ConfigValueParser<ConfigurationWrapper<?>, V> parser,
                             @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> serializer) {
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
     * @return Value's parser, cast value from section.
     */
    public @NotNull ConfigValueParser<ConfigurationWrapper<?>, V> getParser() {
        return parser;
    }

    /**
     * @return Value's serializer, serialize value to section.
     */
    public @NotNull ConfigDataFunction<V, ? extends Map<String, Object>> getSerializer() {
        return serializer;
    }

    /**
     * @return Get the value that parsed from the configuration section.
     */
    @Override
    public @Nullable V get() {
        if (!isExpired()) return getCachedOrDefault();
        // Data that is outdated and needs to be parsed again.

        ConfigurationWrapper<?> section = getConfiguration().getConfigurationSection(getConfigPath());
        if (section == null) return getDefaultValue();

        try {
            // If there are no errors, update the cache and return.
            return updateCache(this.parser.parse(section, this.defaultValue));
        } catch (Exception e) {
            // There was a parsing error, prompted and returned the default value.
            e.printStackTrace();
            return getDefaultValue();
        }

    }

    /**
     * Use the specified value to update the configuration section.
     * Will use {@link #getSerializer()} to serialize the value to section.
     *
     * @param value The value that needs to be set in the configuration.
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
