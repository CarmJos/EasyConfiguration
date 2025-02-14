package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.loader.ConfigurationInitializer;
import cc.carm.lib.configuration.source.meta.ConfigurationMetaHolder;
import cc.carm.lib.configuration.source.meta.ConfigurationMetadata;
import cc.carm.lib.configuration.source.option.ConfigurationOptionHolder;
import cc.carm.lib.configuration.source.section.ConfigureSource;
import cc.carm.lib.configuration.value.ValueManifest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ConfigurationHolder<SOURCE extends ConfigureSource<?, ?, SOURCE>> {

    protected final @NotNull ValueAdapterRegistry adapters;
    protected final @NotNull ConfigurationOptionHolder options;
    protected final @NotNull Map<String, ConfigurationMetaHolder> metadata;

    protected final @NotNull ConfigurationInitializer initializer;

    public ConfigurationHolder(@NotNull ValueAdapterRegistry adapters,
                               @NotNull ConfigurationOptionHolder options,
                               @NotNull Map<String, ConfigurationMetaHolder> metadata,
                               @NotNull ConfigurationInitializer initializer) {
        this.initializer = initializer;
        this.adapters = adapters;
        this.options = options;
        this.metadata = metadata;
    }

    public abstract @NotNull SOURCE config();

    public void reload() throws Exception {
        config().reload();
    }

    public void save() throws Exception {
        config().save();
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

    @NotNull
    @UnmodifiableView
    public <M> Map<String, M> extractMetadata(@NotNull ConfigurationMetadata<M> type) {
        Map<String, M> metas = new LinkedHashMap<>();
        for (Map.Entry<String, ConfigurationMetaHolder> entry : this.metadata.entrySet()) {
            M data = entry.getValue().get(type);
            if (data != null) metas.put(entry.getKey(), data);
        }
        return Collections.unmodifiableMap(metas);
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

    public void initialize(Class<? extends Configuration> configClass) {
        try {
            initializer.initialize(this, configClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize(@NotNull Configuration config) {
        try {
            initializer.initialize(this, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize(@NotNull ValueManifest<?> value) {
        value.holder(this);
    }

}
