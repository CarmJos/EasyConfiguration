package cc.carm.lib.configuration.source.section;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import org.jetbrains.annotations.NotNull;

public abstract class ConfigurationSource<SELF extends ConfigurationSource<SELF, ORIGINAL>, ORIGINAL>
        implements ConfigurationSection {

    protected final @NotNull ConfigurationHolder<? extends SELF> holder;
    protected long lastUpdateMillis;

    protected ConfigurationSource(@NotNull ConfigurationHolder<? extends SELF> holder,
                                  long lastUpdateMillis) {
        this.holder = holder;
        this.lastUpdateMillis = lastUpdateMillis;
    }

    public @NotNull ConfigurationHolder<? extends SELF> holder() {
        return holder;
    }

    public void reload() throws Exception {
        onReload(); // 调用重写的Reload方法
        this.lastUpdateMillis = System.currentTimeMillis();
    }

    protected abstract SELF self();

    /**
     * @return Original configuration object
     */
    public abstract @NotNull ORIGINAL original();

    public abstract void save() throws Exception;

    protected abstract void onReload() throws Exception;

    public long getLastUpdateMillis() {
        return this.lastUpdateMillis;
    }

    public boolean isExpired(long parsedTime) {
        return getLastUpdateMillis() > parsedTime;
    }

}
