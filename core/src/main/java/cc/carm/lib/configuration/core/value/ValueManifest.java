package cc.carm.lib.configuration.core.value;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;

/**
 * 配置值描述清单。用于描述一个配置值的相关基础信息。
 *
 * @param <T> 配置值类型
 */
public class ValueManifest<T> {

    public static <V> ValueManifest<V> of(@Nullable ConfigurationProvider<?> provider, @Nullable String configPath,
                                          @Nullable List<String> headerComments, @Nullable String inlineComments) {
        return new ValueManifest<>(provider, configPath, headerComments, inlineComments, null);
    }

    public static <V> ValueManifest<V> of(@Nullable ConfigurationProvider<?> provider, @Nullable String configPath,
                                          @Nullable List<String> headerComments, @Nullable String inlineComments,
                                          @Nullable V defaultValue) {
        return new ValueManifest<>(provider, configPath, headerComments, inlineComments, defaultValue);
    }

    protected @Nullable ConfigurationProvider<?> provider;
    protected @Nullable String configPath;

    protected @Nullable List<String> headerComments;
    protected @Nullable String inlineComment;

    protected @Nullable T defaultValue;

    /**
     * @param provider       配置文件提供者
     * @param configPath     配置路径
     * @param headerComments 头部注释内容
     * @param inlineComment  行内注释内容
     * @param defaultValue   默认参数值
     */
    public ValueManifest(@Nullable ConfigurationProvider<?> provider, @Nullable String configPath,
                         @Nullable List<String> headerComments, @Nullable String inlineComment,
                         @Nullable T defaultValue) {
        this.provider = provider;
        this.configPath = configPath;
        this.headerComments = headerComments;
        this.inlineComment = inlineComment;
        this.defaultValue = defaultValue;
    }

    /**
     * 此方法用于 {@link cc.carm.lib.configuration.core.ConfigInitializer<T>} 工具对配置值进行统一初始化操作。
     *
     * @param provider       配置文件提供者
     * @param configPath     配置路径
     * @param headerComments 头部注释内容
     * @param inlineComment  行内注释内容
     */
    protected void initialize(@NotNull ConfigurationProvider<?> provider, @NotNull String configPath,
                              @Nullable List<String> headerComments, @Nullable String inlineComment) {
        if (this.provider == null) this.provider = provider;
        if (this.configPath == null) this.configPath = configPath;
        if (this.headerComments == null) this.headerComments = headerComments;
        if (this.inlineComment == null) this.inlineComment = inlineComment;

        if (getHeaderComments() != null) {
            this.provider.setHeaderComment(getConfigPath(), getHeaderComments());
        }
        if (getInlineComment() != null) {
            this.provider.setInlineComment(getConfigPath(), getInlineComment());
        }
    }

    public @Nullable T getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(@Nullable T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public @NotNull ConfigurationProvider<?> getProvider() {
        return Optional.ofNullable(this.provider)
                .orElseThrow(() -> new IllegalStateException("Value(" + configPath + ") does not have a provider."));
    }

    public final @NotNull ConfigurationWrapper<?> getConfiguration() {
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
        String path = getConfigPath(); // 当未指定路径时，优先抛出异常
        return getConfiguration().get(path);
    }

    protected void setValue(@Nullable Object value) {
        getConfiguration().set(getConfigPath(), value);
    }

    public @Nullable String getInlineComment() {
        return inlineComment;
    }

    @Unmodifiable
    public @Nullable List<String> getHeaderComments() {
        return headerComments;
    }


}
