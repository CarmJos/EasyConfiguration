package cc.carm.lib.configuration.value;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public abstract class ConfigValue<T> extends ValueManifest<T> {

    protected ConfigValue(@NotNull ValueManifest<T> manifest) {
        super(manifest.type, manifest.provider, manifest.path, manifest.defaultSupplier);
    }

    /**
     * 得到该配置的设定值(即读取到的值)。
     * <br> 若初始化时未写入默认值，则可以通过 {@link #getOrDefault()} 方法在该设定值为空时获取默认值。
     *
     * @return 设定值
     */
    public abstract @Nullable T get();

    /**
     * 得到该配置的设定值，若不存在，则返回默认值。
     *
     * @return 设定值或默认值
     */
    public T getOrDefault() {
        return optional().orElse(defaults());
    }

    /**
     * 得到该配置的非空值。
     *
     * @return 非空值
     * @throws NullPointerException 对应数据为空时抛出
     */
    public @NotNull T getNotNull() {
        return Objects.requireNonNull(getOrDefault(), "Value(" + path() + ") [" + type() + "] is null.");
    }

    public @NotNull Optional<@Nullable T> optional() {
        return Optional.ofNullable(get());
    }

    /**
     * 设定该配置的值。
     * <br> 设定后，不会自动保存配置文件；若需要保存，请调用 {@link ConfigurationProvider#save()} 方法。
     *
     * @param value 配置的值
     */
    public abstract void set(@Nullable T value);

    /**
     * 初始化该配置的默认值。
     * <br> 设定后，不会自动保存配置文件；若需要保存，请调用 {@link ConfigurationProvider#save()} 方法。
     */
    public void setDefault() {
        setDefault(false);
    }

    /**
     * 将该配置的值设置为默认值。
     * <br> 设定后，不会自动保存配置文件；若需要保存，请调用 {@link ConfigurationProvider#save()} 方法。
     *
     * @param override 是否覆盖已设定的值
     */
    public void setDefault(boolean override) {
        if (!override && config().contains(path())) return;
        Optional.ofNullable(defaults()).ifPresent(this::set);
    }

    /**
     * 判断加载的配置是否与默认值相同。
     *
     * @return 获取当前值是否为默认值。
     */
    public boolean isDefault() {
        return Objects.equals(defaults(), get());
    }

}
