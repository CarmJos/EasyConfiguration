package cc.carm.lib.configuration.value;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.source.ConfigurationSource;
import cc.carm.lib.configuration.value.meta.ValueMetaList;
import cc.carm.lib.configuration.value.meta.ValueMetaType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Supplier;

public class ValueManifest<T> {

    protected final @NotNull Map<ValueMetaType<?>, Object> metadata;
    protected @Nullable ConfigurationProvider<?> provider;
    protected @NotNull Supplier<@Nullable T> defaultSupplier;

    public ValueManifest(@NotNull Map<ValueMetaType<?>, Object> metadata,
                         @Nullable ConfigurationProvider<?> provider, @NotNull Supplier<@Nullable T> defaultSupplier) {
        this.metadata = metadata;
        this.provider = provider;
        this.defaultSupplier = defaultSupplier;
    }

    public @Nullable T defaults() {
        return this.defaultSupplier.get();
    }

    public void defaults(@Nullable T defaultValue) {
        defaults(() -> defaultValue);
    }

    public void defaults(@NotNull Supplier<@Nullable T> defaultValue) {
        this.defaultSupplier = defaultValue;
    }

    public @NotNull ConfigurationProvider<?> provider() {
        if (this.provider != null) return this.provider;
        throw new IllegalStateException("Value does not have a provider.");
    }

    public @NotNull ConfigurationSource<?, ?> config() {
        return provider().source();
    }

    public @NotNull String path() {
        String path = getMeta(ValueMetaList.PATH);
        if (path != null) return path;
        else throw new IllegalStateException("No section path provided.");
    }

    protected Object getValue() {
        return config().get(path());
    }

    protected void setValue(@Nullable Object value) {
        config().set(path(), value);
    }

    public Map<ValueMetaType<?>, Object> metadata() {
        return metadata;
    }

    /**
     * Get the value of option.
     *
     * @param type         {@link ValueMetaType}
     * @param defaultValue Default value if the value of option is not set.
     * @param <V>          Value type
     * @return Value of option
     */
    @SuppressWarnings("unchecked")
    @Contract("_, !null -> !null")
    public <V> @Nullable V getMeta(@NotNull ValueMetaType<V> type, @Nullable V defaultValue) {
        return (V) metadata().getOrDefault(type, type.getDefault(this, defaultValue));
    }

    /**
     * Get the value of option.
     *
     * @param type {@link ValueMetaType}
     * @param <V>  Value type
     * @return Value of option
     */
    public <V> @Nullable V getMeta(@NotNull ValueMetaType<V> type) {
        return getMeta(type, null);
    }

    public boolean hasMeta(@NotNull ValueMetaType<?> type) {
        return metadata().containsKey(type) || type.hasDefaults(this);
    }

    /**
     * Set the value of meta, if the value is null, the meta will be removed.
     * <br> Will only be changed in current holder.
     *
     * @param type  {@link ValueMetaType}
     * @param value Value of meta
     * @param <V>   Value type
     * @return Previous value of meta
     */
    @SuppressWarnings("unchecked")
    public <V> @Nullable V setMeta(@NotNull ValueMetaType<V> type, @Nullable V value) {
        if (value == null || type.isDefault(this, value)) {
            return (V) metadata().remove(type);
        } else {
            return (V) metadata().put(type, value);
        }
    }

    /**
     * Set the value of meta, if the value is null, the meta will not be changed.
     * <br> Will only be changed in current holder.
     *
     * @param type  {@link ValueMetaType}
     * @param value Value of meta
     * @param <V>   Value type
     */
    public <V> void setMetaIfAbsent(@NotNull ValueMetaType<V> type, @Nullable V value) {
        if (value == null || type.isDefault(this, value)) {
            metadata().remove(type);
        } else {
            metadata().putIfAbsent(type, value);
        }
    }

    /**
     * Set the value of meta, if the value is null, the meta will not be changed.
     * <br> Will only be changed in current holder.
     *
     * @param type  {@link ValueMetaType}
     * @param value Value of meta
     * @param <V>   Value type
     */
    @SuppressWarnings("unchecked")
    public <V> @Nullable V setMetaIfPresent(@NotNull ValueMetaType<V> type, @Nullable V value) {
        Object exists = metadata().get(type);
        if (exists == null) return null;

        if (value == null || type.isDefault(this, value)) {
            return (V) metadata().remove(type);
        } else {
            return (V) metadata().put(type, value);
        }
    }


}
