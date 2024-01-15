package cc.carm.lib.configuration.core.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;

/**
 * 配置文件提供者，用于为 {@link ConfigValue} 提供配置文件的源，以便实现读取、保存等操作。
 *
 * @param <W> 配置文件的原生功能类
 */
public abstract class ConfigurationProvider<W extends ConfigurationWrapper<?>> {

    protected long updateTime;

    protected ConfigurationProvider() {
        this.updateTime = System.currentTimeMillis();
    }

    /**
     * 得到配置文件的更新(最后加载)时间。
     *
     * @return 更新时间
     */
    public long getUpdateTime() {
        return updateTime;
    }

    /**
     * 用于 {@link CachedConfigValue} 判断缓存值是否过期(即缓存的时间早于配置文件的最后加载时间)。
     *
     * @param time 缓存值时的时间戳
     * @return 缓存值是否过期
     */
    public boolean isExpired(long time) {
        return getUpdateTime() > time;
    }

    /**
     * 得到配置文件的原生功能类。
     *
     * @return 原生类
     */
    public abstract @NotNull W getConfiguration();

    /**
     * 重载当前配置文件。(将不会保存已修改的内容)
     *
     * @throws Exception 当重载出现错误时抛出
     */
    public void reload() throws Exception {
        onReload(); // 调用重写的Reload方法
        this.updateTime = System.currentTimeMillis();
    }

    /**
     * 将当前对配置文件的更改进行保存。
     *
     * @throws Exception 当保存出现错误时抛出
     */
    public abstract void save() throws Exception;

    /**
     * 针对于不同配置文件类型所执行的重载操作。
     *
     * @throws Exception 当操作出现错误时抛出。
     */
    protected abstract void onReload() throws Exception;

    public abstract @Nullable ConfigurationComments getComments();

    public void setHeaderComment(@Nullable String path, @Nullable List<String> comments) {
        if (getComments() == null) return;
        getComments().setHeaderComments(path, comments);
    }

    public void setInlineComment(@NotNull String path, @Nullable String comment) {
        if (getComments() == null) return;
        getComments().setInlineComment(path, comment);
    }

    @Nullable
    @Unmodifiable
    public List<String> getHeaderComment(@Nullable String path) {
        return Optional.ofNullable(getComments()).map(c -> c.getHeaderComment(path)).orElse(null);
    }

    public @Nullable String getInlineComment(@NotNull String path) {
        return Optional.ofNullable(getComments()).map(c -> c.getInlineComment(path)).orElse(null);
    }

    public abstract @NotNull ConfigInitializer<? extends ConfigurationProvider<W>> getInitializer();

    /**
     * 初始化指定类以及其子类的所有 {@link ConfigValue} 对象。
     *
     * @param configClazz 配置文件类，须继承于 {@link Configuration} 。
     */
    public void initialize(Class<? extends Configuration> configClazz) {
        initialize(configClazz, true);
    }

    /**
     * 初始化指定类以及其子类的所有 {@link ConfigValue} 对象。
     *
     * @param configClazz  配置文件类，须继承于 {@link Configuration} 。
     * @param saveDefaults 是否写入默认值(默认为 true)。
     */
    public void initialize(Class<? extends Configuration> configClazz, boolean saveDefaults) {
        this.getInitializer().initialize(configClazz, saveDefaults);
    }

    /**
     * 初始化指定类的所有 {@link ConfigValue} 对象。
     *
     * @param configClazz    配置文件类，须继承于 {@link Configuration} 。
     * @param saveDefaults   是否写入默认值(默认为 true)。
     * @param loadSubClasses 是否加载内部子类(默认为 true)。
     */
    public void initialize(Class<? extends Configuration> configClazz, boolean saveDefaults, boolean loadSubClasses) {
        this.getInitializer().initialize(configClazz, saveDefaults, loadSubClasses);
    }

    /**
     * 初始化指定实例的所有 {@link ConfigValue} 与内部 {@link Configuration} 对象。
     *
     * @param config 配置文件实例类，须实现 {@link Configuration} 。
     */
    public void initialize(@NotNull Configuration config) {
        this.getInitializer().initialize(config, true);
    }

    /**
     * 初始化指定实例的所有 {@link ConfigValue} 与内部 {@link Configuration} 对象。
     *
     * @param config       配置文件实例类，须实现 {@link Configuration} 。
     * @param saveDefaults 是否写入默认值(默认为 true)。
     */
    public void initialize(@NotNull Configuration config, boolean saveDefaults) {
        this.getInitializer().initialize(config, saveDefaults);
    }

}
