package cc.carm.test.config;

import cc.carm.lib.configuration.source.ConfigurationSection;
import cc.carm.lib.configuration.source.ConfigurationSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestSource extends ConfigurationSource<TestSource, Map<String, String>> {

    public TestSource() {
        super(System.currentTimeMillis());
    }

    @Override
    protected TestSource getThis() {
        return this;
    }

    @Override
    public void save() throws Exception {

    }

    @Override
    protected void onReload() throws Exception {

    }

    @Override
    public @NotNull Map<String, String> original() {
        return null;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return null;
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return null;
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return null;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {

    }

    @Override
    public boolean contains(@NotNull String path) {
        return false;
    }

    @Override
    public boolean isList(@NotNull String path) {
        return false;
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        return null;
    }

    @Override
    public boolean isSection(@NotNull String path) {
        return false;
    }

    @Override
    public @Nullable ConfigurationSection getSection(@NotNull String path) {
        return null;
    }
}
