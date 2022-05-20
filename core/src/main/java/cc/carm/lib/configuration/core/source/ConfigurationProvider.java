package cc.carm.lib.configuration.core.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.ConfigurationRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public abstract class ConfigurationProvider<W extends ConfigurationWrapper> {

    protected long updateTime;

    public ConfigurationProvider() {
        this.updateTime = System.currentTimeMillis();
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public boolean isExpired(long time) {
        return this.updateTime > time;
    }

    public abstract @NotNull W getConfiguration();

    public void reload() throws Exception {
        onReload(); // 调用重写的Reload方法
        this.updateTime = System.currentTimeMillis();
    }

    public abstract void save() throws Exception;

    protected abstract void onReload() throws Exception;

    public abstract void setHeaderComment(@Nullable String path, @Nullable List<String> comments);

    public abstract void setInlineComment(@NotNull String path, @Nullable String comment);

    @Nullable
    @Unmodifiable
    public abstract List<String> getHeaderComment(@Nullable String path);

    public abstract @Nullable String getInlineComment(@NotNull String path);

    public abstract @NotNull ConfigInitializer<? extends ConfigurationProvider<W>> getInitializer();

    public void initialize(Class<? extends ConfigurationRoot> configClazz) {
        initialize(configClazz, true);
    }

    public void initialize(Class<? extends ConfigurationRoot> configClazz, boolean saveDefaults) {
        getInitializer().initialize(configClazz, saveDefaults);
    }

    public void initialize(Class<? extends ConfigurationRoot> configClazz, boolean saveDefaults, boolean loadSubClasses) {
        getInitializer().initialize(configClazz, saveDefaults, loadSubClasses);
    }

}
