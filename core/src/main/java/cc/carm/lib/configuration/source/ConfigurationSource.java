package cc.carm.lib.configuration.source;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ConfigurationSource<S extends ConfigurationSource<S, O>, O> implements ConfigurationSection {

    protected long updateMillis;

    protected ConfigurationSource(long updateMillis) {
        this.updateMillis = updateMillis;
    }

    public void reload() throws Exception {
        onReload(); // 调用重写的Reload方法
        this.updateMillis = System.currentTimeMillis();
    }

    protected abstract S getThis();

    public abstract void save() throws Exception;

    protected abstract void onReload() throws Exception;

    public abstract @NotNull O original();

    @NotNull
    public Set<String> getKeys(boolean deep) {
        return getValues(deep).keySet();
    }

    public long getUpdateMillis() {
        return this.updateMillis;
    }

    public boolean isExpired(long time) {
        return getUpdateMillis() > time;
    }

}
