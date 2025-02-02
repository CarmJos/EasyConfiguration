package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;

public abstract class ConfigurationSource<SELF extends ConfigurationSource<SELF, ORIGINAL>, ORIGINAL>
        implements ConfigurationSection {

    protected long updateMillis;

    protected ConfigurationSource(long updateMillis) {
        this.updateMillis = updateMillis;
    }

    public void reload() throws Exception {
        onReload(); // 调用重写的Reload方法
        this.updateMillis = System.currentTimeMillis();
    }

    protected abstract SELF self();

    /**
     * @return Original configuration object
     */
    public abstract @NotNull ORIGINAL original();

    public abstract void save() throws Exception;

    protected abstract void onReload() throws Exception;

    public long getUpdateMillis() {
        return this.updateMillis;
    }

    public boolean isExpired(long parsedTime) {
        return getUpdateMillis() > parsedTime;
    }

}
