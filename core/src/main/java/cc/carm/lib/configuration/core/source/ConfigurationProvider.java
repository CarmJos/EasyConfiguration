package cc.carm.lib.configuration.core.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.ConfigurationRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public abstract void reload() throws Exception;

    public abstract void save() throws Exception;

    public abstract void setComment(@Nullable String path, @Nullable ConfigCommentInfo comment);

    public abstract @Nullable ConfigCommentInfo getComment(@Nullable String path);

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
