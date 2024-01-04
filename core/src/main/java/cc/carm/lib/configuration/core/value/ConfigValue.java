package cc.carm.lib.configuration.core.value;

import cc.carm.lib.configuration.core.builder.ConfigBuilder;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class ConfigValue<T> extends ValueManifest<T> {

    public static @NotNull ConfigBuilder builder() {
        return new ConfigBuilder();
    }

    protected ConfigValue(@NotNull ValueManifest<T> manifest) {
        super(manifest.provider, manifest.configPath, manifest.headerComments, manifest.inlineComment, manifest.defaultValue);
    }

    /**
     * @param provider       Provider of config files {@link ConfigurationProvider}
     * @param configPath     Config path of this value
     * @param headerComments Header comment contents
     * @param inlineComments Inline comment contents
     * @param defaultValue   The default value
     * @deprecated Please use {@link #ConfigValue(ValueManifest)} instead.
     */
    @Deprecated
    protected ConfigValue(@Nullable ConfigurationProvider<?> provider, @Nullable String configPath,
                          @Nullable List<String> headerComments, @Nullable String inlineComments,
                          @Nullable T defaultValue) {
        super(provider, configPath, headerComments, inlineComments, defaultValue);
    }

    public void initialize(@NotNull ConfigurationProvider<?> provider, boolean saveDefault, @NotNull String configPath,
                           @Nullable List<String> headerComments, @Nullable String inlineComments) {
        this.initialize(provider, configPath, headerComments, inlineComments);
        if (saveDefault) setDefault();
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
    public @Nullable T getOrDefault() {
        return getOptional().orElse(getDefaultValue());
    }

    /**
     * 得到该配置的非空值。
     *
     * @return 非空值
     * @throws NullPointerException 对应数据为空时抛出
     */
    public @NotNull T getNotNull() {
        return Objects.requireNonNull(getOrDefault(), "Value(" + configPath + ") is null.");
    }

    public @NotNull Optional<@Nullable T> getOptional() {
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
        if (!override && getConfiguration().contains(getConfigPath())) return;
        Optional.ofNullable(getDefaultValue()).ifPresent(this::set);
    }

    /**
     * 判断加载的配置是否与默认值相同。
     *
     * @return 获取当前值是否为默认值。
     */
    public boolean isDefault() {
        return Objects.equals(getDefaultValue(), get());
    }

}
