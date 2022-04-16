package cc.carm.lib.configuration.core.source;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.ConfigurationRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigurationProvider {

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

    public abstract @NotNull ConfigurationWrapper getConfiguration();

    public abstract void reload() throws Exception;

    public abstract void save() throws Exception;

    public abstract void setComments(@NotNull String path, @NotNull String... comments);

    public abstract @Nullable String[] getComments(@NotNull String path);

    public void initialize(Class<? extends ConfigurationRoot> configClazz) {
        ConfigInitializer.initialize(this, configClazz, true);
    }

}
