package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.loader.ConfigurationInitializer;
import cc.carm.lib.configuration.source.meta.ConfigurationMetaHolder;
import cc.carm.lib.configuration.source.option.ConfigurationOptionHolder;
import cc.carm.lib.configuration.source.section.ConfigurationSource;
import cc.carm.lib.configuration.value.ValueManifest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ConfigurationProvider<S extends ConfigurationSource<S, ?>> {

    protected final @NotNull S source;
    protected final @NotNull ValueAdapterRegistry adapters;
    protected final @NotNull ConfigurationOptionHolder options;
    protected final @NotNull Map<String, ConfigurationMetaHolder> metadata;

    protected final @NotNull ConfigurationInitializer initializer;

    public ConfigurationProvider(@NotNull S source,
                                 @NotNull ValueAdapterRegistry adapters,
                                 @NotNull ConfigurationOptionHolder options,
                                 @NotNull Map<String, ConfigurationMetaHolder> metadata,
                                 @NotNull ConfigurationInitializer initializer) {
        this.source = source;
        this.initializer = initializer;
        this.adapters = adapters;
        this.options = options;
        this.metadata = metadata;
    }

    public @NotNull S source() {
        return source;
    }

    public void reload() throws Exception {
        source().reload();
    }

    public void save() throws Exception {
        source().save();
    }

    public ConfigurationOptionHolder options() {
        return options;
    }

    public @NotNull Map<String, ConfigurationMetaHolder> metadata() {
        return this.metadata;
    }

    public @NotNull ConfigurationMetaHolder metadata(@Nullable String path) {
        return metadata().computeIfAbsent(path, k -> new ConfigurationMetaHolder());
    }


    public ValueAdapterRegistry adapters() {
        return this.adapters;
    }

    public ConfigurationInitializer initializer() {
        return initializer;
    }

    @Contract("_,null -> null")
    public <T> T deserialize(@NotNull Class<T> type, @Nullable Object source) throws Exception {
        return adapters().deserialize(this, type, source);
    }

    @Contract("_,null -> null")
    public <T> T deserialize(@NotNull ValueType<T> type, @Nullable Object source) throws Exception {
        return adapters().deserialize(this, type, source);
    }

    @Contract("null -> null")
    public <T> Object serialize(@Nullable T value) throws Exception {
        return adapters().serialize(this, value);
    }

    public void load(Class<? extends Configuration> configClass) {
        try {
            initializer.initialize(this, configClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(@NotNull Configuration config) {
        try {
            initializer.initialize(this, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(@NotNull ValueManifest<?> value) {
        value.provider(this);
    }

}
