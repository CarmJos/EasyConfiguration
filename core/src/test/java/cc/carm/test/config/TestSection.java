package cc.carm.test.config;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.configuration.source.section.ConfigureSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestSection implements ConfigureSection {
    @Override
    public @NotNull ConfigureSource<?, ?, ?> source() {
        return null;
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return Collections.emptyMap();
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
        return Collections.emptyList();
    }

    @Override
    public boolean isSection(@NotNull String path) {
        return false;
    }

    @Override
    public @Nullable ConfigureSection getSection(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return null;
    }
}
