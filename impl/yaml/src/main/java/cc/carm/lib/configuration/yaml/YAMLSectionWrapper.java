package cc.carm.lib.configuration.yaml;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class YAMLSectionWrapper implements ConfigurationWrapper<ConfigurationSection> {

    private final ConfigurationSection section;

    private YAMLSectionWrapper(ConfigurationSection section) {
        this.section = section;
    }

    @Contract("!null->!null")
    public static @Nullable YAMLSectionWrapper of(@Nullable ConfigurationSection section) {
        return section == null ? null : new YAMLSectionWrapper(section);
    }

    @Override
    public @NotNull ConfigurationSection getSource() {
        return this.section;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return new LinkedHashSet<>(section.getKeys(deep));
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return section.getValues(deep);
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
        return this.section.isList(path);
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        return this.section.getList(path);
    }

    @Override
    public boolean isConfigurationSection(@NotNull String path) {
        return this.section.isConfigurationSection(path);
    }

    @Override
    public @Nullable YAMLSectionWrapper getConfigurationSection(@NotNull String path) {
        return of(this.section.getConfigurationSection(path));
    }

    @Nullable
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz) {
        return getSerializable(path, clazz, null);
    }

    @Nullable
    @Contract("_, _, !null -> !null")
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz, @Nullable T defaultValue) {
        return this.section.getSerializable(path, clazz, defaultValue);
    }

}
