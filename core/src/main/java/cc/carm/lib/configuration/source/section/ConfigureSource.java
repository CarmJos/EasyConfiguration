package cc.carm.lib.configuration.source.section;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public abstract class ConfigureSource<
        SECTION extends ConfigureSection, ORIGINAL,
        SELF extends ConfigureSource<SECTION, ORIGINAL, SELF>>
        implements ConfigureSection {

    protected final @NotNull ConfigurationHolder<? extends SELF> holder;
    protected long lastUpdateMillis;

    protected ConfigureSource(@NotNull ConfigurationHolder<? extends SELF> holder, long lastUpdateMillis) {
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

    /**
     * @return Configuration section
     */
    public abstract @NotNull SECTION section();

    public abstract void save() throws Exception;

    protected abstract void onReload() throws Exception;

    public long getLastUpdateMillis() {
        return this.lastUpdateMillis;
    }

    public boolean isExpired(long parsedTime) {
        return getLastUpdateMillis() > parsedTime;
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return section().getValues(deep);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        section().set(path, value);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return section().contains(path);
    }

    @Override
    public boolean isList(@NotNull String path) {
        return section().isList(path);
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        return section().getList(path);
    }

    @Override
    public boolean isSection(@NotNull String path) {
        return section().isSection(path);
    }

    @Override
    public @Nullable ConfigureSection getSection(@NotNull String path) {
        return section().getSection(path);
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return section().get(path);
    }

}
