package cc.carm.lib.configuration.bungee;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class BungeeSectionWrapper implements ConfigurationWrapper {

    private final Configuration section;

    private BungeeSectionWrapper(Configuration section) {
        this.section = section;
    }

    @Contract("!null->!null")
    public static @Nullable BungeeSectionWrapper of(@Nullable Configuration section) {
        return section == null ? null : new BungeeSectionWrapper(section);
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return new LinkedHashSet<>(section.getKeys());
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return section.getKeys().stream()
                .collect(Collectors.toMap(key -> key, section::get, (a, b) -> b, LinkedHashMap::new));
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        this.section.set(path, value);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return this.section.contains(path);
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return this.section.get(path);
    }

    @Override
    public boolean isList(@NotNull String path) {
        return get(path) instanceof List<?>;
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        return this.section.getList(path);
    }

    @Override
    public boolean isConfigurationSection(@NotNull String path) {
        return true; // No provided functions :( SRY
    }

    @Override
    public @Nullable ConfigurationWrapper getConfigurationSection(@NotNull String path) {
        return of(this.section.getSection(path));
    }
}
