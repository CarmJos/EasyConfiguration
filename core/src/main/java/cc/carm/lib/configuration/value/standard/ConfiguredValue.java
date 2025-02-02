package cc.carm.lib.configuration.value.standard;

import cc.carm.lib.configuration.adapter.ValueParser;
import cc.carm.lib.configuration.adapter.ValueSerializer;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.value.ConfigValueBuilder;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ConfiguredValue<V> extends CachedConfigValue<V> {

    public static <V> ConfigValueBuilder<V> builderOf(@NotNull Class<V> type) {
        return new ConfigValueBuilder<>(ValueType.of(type));
    }

    public static <V> ConfigValueBuilder<V> builderOf(@NotNull ValueType<V> type) {
        return new ConfigValueBuilder<>(type);
    }

    public static <V> ConfiguredValue<V> of(@NotNull V defaults) {
        return of(ValueType.of(defaults), () -> defaults);
    }

    public static <V> ConfiguredValue<V> of(@NotNull Class<V> type) {
        return of(ValueType.of(type), () -> null);
    }

    public static <V> ConfiguredValue<V> of(@NotNull Class<V> type, @NotNull Supplier<@Nullable V> defaultSupplier) {
        return of(ValueType.of(type), defaultSupplier);
    }

    public static <V> ConfiguredValue<V> of(@NotNull ValueType<V> type) {
        return of(type, () -> null);
    }

    public static <V> ConfiguredValue<V> of(@NotNull ValueType<V> type, @NotNull Supplier<@Nullable V> defaultSupplier) {
        return of(
                new ValueManifest<>(type, defaultSupplier),
                (provider, t, data) -> provider.deserialize(type, data),
                (provider, t, value) -> provider.serialize(value)
        );
    }

    public static <V> ConfiguredValue<V> of(@NotNull ValueManifest<V> manifest,
                                            @Nullable ValueParser<V> parser,
                                            @Nullable ValueSerializer<V> serializer) {
        return new ConfiguredValue<>(manifest, parser, serializer);
    }

    protected final @Nullable ValueParser<V> parser;
    protected final @Nullable ValueSerializer<V> serializer;

    public ConfiguredValue(@NotNull ValueManifest<V> manifest,
                           @Nullable ValueParser<V> parser,
                           @Nullable ValueSerializer<V> serializer) {
        super(manifest);
        this.parser = parser;
        this.serializer = serializer;
    }

    /**
     * @return Value's parser, parse base object to value.
     */
    public @Nullable ValueParser<V> parser() {
        return parser;
    }

    /**
     * @return Value's serializer, parse value to base object.
     */
    public @Nullable ValueSerializer<V> serializer() {
        return serializer;
    }

    @Override
    public V get() {
        if (!isExpired()) return getCachedOrDefault();
        // Data that is outdated and needs to be parsed again.

        Object data = getData();
        if (data == null) return defaults();

        ValueParser<V> parser = parser();
        if (parser == null) return defaults(); // No parser, return default value.

        try {
            // If there are no errors, update the cache and return.
            return updateCache(parser.deserialize(provider(), type(), data));
        } catch (Exception e) {
            // There was a parsing error, prompted and returned the default value.
            e.printStackTrace();
            return defaults();
        }

    }

    /**
     * Set the value of the configuration path.
     * Will use {@link #serializer()} to serialize the value.
     *
     * @param value The value to be set
     */
    @Override
    public void set(V value) {
        updateCache(value); // Update cache
        if (value == null) {
            setData(null);
            return;
        }

        ValueSerializer<V> serializer = serializer();
        if (serializer == null) return; // No serializer, do nothing.

        try {
            setData(serializer.serialize(provider(), type(), value));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

