package cc.carm.lib.configuration.core.value;

import cc.carm.lib.configuration.core.builder.ConfigBuilder;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class ConfigValue<T> {

    public static @NotNull ConfigBuilder builder() {
        return new ConfigBuilder();
    }

    protected @Nullable ConfigurationProvider<?> provider;
    protected @Nullable String configPath;

    protected @Nullable List<String> headerComments;
    protected @Nullable String inlineComments;

    protected @Nullable T defaultValue;

    public ConfigValue(@Nullable ConfigurationProvider<?> provider, @Nullable String configPath,
                       @Nullable List<String> headerComments, @Nullable String inlineComments,
                       @Nullable T defaultValue) {
        this.provider = provider;
        this.configPath = configPath;
        this.headerComments = headerComments;
        this.inlineComments = inlineComments;
        this.defaultValue = defaultValue;
    }

    public void initialize(@NotNull ConfigurationProvider<?> provider, boolean saveDefault, @NotNull String configPath,
                           @Nullable List<String> headerComments, @Nullable String inlineComments) {
        if (this.provider == null) this.provider = provider;
        if (this.configPath == null) this.configPath = configPath;
        if (this.headerComments == null) this.headerComments = headerComments;
        if (this.inlineComments == null) this.inlineComments = inlineComments;
        if (saveDefault) setDefault();

        if (getHeaderComments() != null) {
            this.provider.setHeaderComment(getConfigPath(), getHeaderComments());
        }
        if (getInlineComments() != null) {
            this.provider.setInlineComment(getConfigPath(), getInlineComments());
        }

    }

    public @Nullable T getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(@Nullable T defaultValue) {
        this.defaultValue = defaultValue;
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
        T defaultValue = getDefaultValue();
        T value = get();
        if (defaultValue == null && value == null) return true;
        else if (defaultValue != null && value != null) return defaultValue.equals(value);
        else return false;
    }

    public @NotNull ConfigurationProvider<?> getProvider() {
        return Optional.ofNullable(this.provider)
                .orElseThrow(() -> new IllegalStateException("Value(" + configPath + ") does not have a provider."));
    }

    public final @NotNull ConfigurationWrapper getConfiguration() {
        try {
            return getProvider().getConfiguration();
        } catch (Exception ex) {
            throw new IllegalStateException("Value(" + configPath + ") has not been initialized", ex);
        }
    }

    public @NotNull String getConfigPath() {
        return Optional.ofNullable(this.configPath)
                .orElseThrow(() -> new IllegalStateException("No section path provided."));
    }

    protected Object getValue() {
        return getConfiguration().get(getConfigPath());
    }

    protected void setValue(@Nullable Object value) {
        getConfiguration().set(getConfigPath(), value);
    }

    public @Nullable String getInlineComments() {
        return inlineComments;
    }

    @Unmodifiable
    public @Nullable List<String> getHeaderComments() {
        return headerComments;
    }

}
