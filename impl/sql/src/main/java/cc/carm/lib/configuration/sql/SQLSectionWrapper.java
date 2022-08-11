package cc.carm.lib.configuration.sql;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SQLSectionWrapper implements ConfigurationWrapper {

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return null;
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
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
    public @Nullable Object get(@NotNull String path) {
        return null;
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
    public boolean isConfigurationSection(@NotNull String path) {
        return false;
    }

    @Override
    public @Nullable ConfigurationWrapper getConfigurationSection(@NotNull String path) {
        return null;
    }

}
