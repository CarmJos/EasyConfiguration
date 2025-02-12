package cc.carm.lib.configuration.source.yaml;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.configuration.source.section.ConfigureSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class YAMLSection implements ConfigureSection {

    protected final @NotNull ConfigureSource<? extends YAMLSection, ?, ?> source;
    protected final @NotNull Map<String, Object> data;
    protected final @Nullable YAMLSection parent;

    public YAMLSection(@NotNull ConfigureSource<? extends YAMLSection, ?, ?> source,
                       @NotNull Map<String, Object> data, @Nullable YAMLSection parent) {
        this.source = source;
        this.data = data;
        this.parent = parent;
    }


    @Override
    public @NotNull ConfigureSource<?, ?, ?> source() {
        return this.source;
    }

    @Override
    public @Nullable YAMLSection parent() {
        return this.parent;
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> getValues(boolean deep) {
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
    public @Nullable YAMLSection getSection(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return null;
    }
}
