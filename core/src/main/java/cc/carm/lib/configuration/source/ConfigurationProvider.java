package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.meta.PathMetadata;
import cc.carm.lib.configuration.source.loader.ConfigurationLoader;
import cc.carm.lib.configuration.source.option.ConfigurationOption;
import cc.carm.lib.configuration.source.option.ConfigurationOptionHolder;
import cc.carm.lib.configuration.source.section.ConfigurationSource;
import cc.carm.lib.configuration.value.ValueManifest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ConfigurationProvider<S extends ConfigurationSource<S, ?>> {

    protected final @NotNull S source;
    protected final @NotNull ConfigurationLoader loader;
    protected final @NotNull ValueAdapterRegistry adapters;
    protected final @NotNull ConfigurationOptionHolder options;
    protected final @NotNull Map<String, Map<PathMetadata<?>, Object>> pathMetadata;

    public ConfigurationProvider(@NotNull S source, @NotNull ConfigurationLoader loader,
                                 @NotNull ValueAdapterRegistry adapters,
                                 @NotNull ConfigurationOptionHolder options,
                                 @NotNull Map<String, Map<PathMetadata<?>, Object>> pathMetadata) {
        this.source = source;
        this.loader = loader;
        this.adapters = adapters;
        this.options = options;
        this.pathMetadata = pathMetadata;
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

    public <T> @NotNull T option(@NotNull ConfigurationOption<T> option) {
        return options.get(option);
    }

    public <T> void option(@NotNull ConfigurationOption<T> option, @NotNull T value) {
        options.set(option, value);
    }

    public ValueAdapterRegistry adapters() {
        return this.adapters;
    }

    public ConfigurationLoader loader() {
        return loader;
    }

    public @NotNull Map<String, Map<PathMetadata<?>, Object>> pathMetadata() {
        return pathMetadata;
    }

    public @NotNull Map<PathMetadata<?>, Object> metadata(@NotNull String path) {
        return pathMetadata().computeIfAbsent(path, k -> new java.util.HashMap<>());
    }

    /**
     * Get the value of option.
     *
     * @param type         {@link PathMetadata}
     * @param defaultValue Default value if the value of option is not set.
     * @param <V>          Value type
     * @return Value of option
     */
    @SuppressWarnings("unchecked")
    @Contract("_,_, !null -> !null")
    public <V> @Nullable V meta(@NotNull String path,
                                @NotNull PathMetadata<V> type, @Nullable V defaultValue) {
        return (V) metadata(path).getOrDefault(type, type.getDefault(path, defaultValue));
    }

    /**
     * Get the value of option.
     *
     * @param type {@link PathMetadata}
     * @param <V>  Value type
     * @return Value of option
     */
    public <V> @Nullable V meta(@NotNull String path, @NotNull PathMetadata<V> type) {
        return meta(path, type, null);
    }

    public boolean hasMeta(@NotNull String path, @NotNull PathMetadata<?> type) {
        return metadata(path).containsKey(type) || type.hasDefaults(path);
    }

    /**
     * Set the value of meta, if the value is null, the meta will be removed.
     * <br> Will only be changed in current holder.
     *
     * @param type  {@link PathMetadata}
     * @param value Value of meta
     * @param <V>   Value type
     * @return Previous value of meta
     */
    @SuppressWarnings("unchecked")
    public <V> @Nullable V setMeta(@NotNull String path, @NotNull PathMetadata<V> type, @Nullable V value) {
        if (value == null || type.isDefault(path, value)) {
            return (V) metadata(path).remove(type);
        } else {
            return (V) metadata(path).put(type, value);
        }
    }

    /**
     * Set the value of meta, if the value is null, the meta will not be changed.
     * <br> Will only be changed in current holder.
     *
     * @param type  {@link PathMetadata}
     * @param value Value of meta
     * @param <V>   Value type
     */
    public <V> void setMetaIfAbsent(@NotNull String path, @NotNull PathMetadata<V> type, @Nullable V value) {
        if (value == null || type.isDefault(path, value)) {
            metadata(path).remove(type);
        } else {
            metadata(path).putIfAbsent(type, value);
        }
    }

    /**
     * Set the value of meta, if the value is null, the meta will not be changed.
     * <br> Will only be changed in current holder.
     *
     * @param type  {@link PathMetadata}
     * @param value Value of meta
     * @param <V>   Value type
     */
    @SuppressWarnings("unchecked")
    public <V> @Nullable V setMetaIfPresent(@NotNull String path, @NotNull PathMetadata<V> type, @Nullable V value) {
        Object exists = metadata(path).get(type);
        if (exists == null) return null;

        if (value == null || type.isDefault(path, value)) {
            return (V) metadata(path).remove(type);
        } else {
            return (V) metadata(path).put(type, value);
        }
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
            loader.load(this, configClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(@NotNull Configuration config) {
        try {
            loader.load(this, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(@NotNull ValueManifest<?> value) {
        value.provider(this);
    }

}
