package cc.carm.lib.configuration.sql;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * For SQL configs, that primary path will be directly mapped to the value.
 *
 * @author CarmJos
 */
public class SQLConfigWrapper implements ConfigurationWrapper<Map<String, Object>> {

    private static final char SEPARATOR = '.';
    protected final @NotNull SQLConfigProvider provider;
    protected final @NotNull Map<String, Object> data;

    SQLConfigWrapper(@NotNull SQLConfigProvider provider, @NotNull Map<?, ?> map) {
        this.provider = provider;
        this.data = new LinkedHashMap<>();

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = (entry.getKey() == null) ? "null" : entry.getKey().toString();

            if (entry.getValue() instanceof Map) {
                this.data.put(key, new SQLConfigWrapper(provider, (Map<?, ?>) entry.getValue()));
            } else {
                this.data.put(key, entry.getValue());
            }
        }
    }

    @Override
    public @NotNull Map<String, Object> getSource() {
        return this.data;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return getValues(deep).keySet();
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        // Deep is not supported for SQL configs.
        return new LinkedHashMap<>(this.data);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (value instanceof Map) {
            value = new SQLConfigWrapper(this.provider, (Map<?, ?>) value);
        }

        SQLConfigWrapper section = getSectionFor(path);
        if (section == this) {
            if (value == null) {
                this.data.remove(path);
            } else {
                this.data.put(path, value);
            }
        } else {
            section.set(getChild(path), value);
        }

        this.provider.updated.add(path);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return get(path) != null;
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        SQLConfigWrapper section = getSectionFor(path);
        return section == this ? data.get(path) : section.get(getChild(path));
    }

    @Override
    public boolean isList(@NotNull String path) {
        return get(path) instanceof List<?>;
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        Object val = get(path);
        return (val instanceof List<?>) ? (List<?>) val : null;
    }

    @Override
    public boolean isConfigurationSection(@NotNull String path) {
        return get(path) instanceof SQLConfigWrapper;
    }

    @Override
    public @Nullable SQLConfigWrapper getConfigurationSection(@NotNull String path) {
        Object val = get(path);
        return (val instanceof SQLConfigWrapper) ? (SQLConfigWrapper) val : null;
    }

    private SQLConfigWrapper getSectionFor(String path) {
        int index = path.indexOf(SEPARATOR);
        if (index == -1) return this;

        String root = path.substring(0, index);
        Object section = this.data.computeIfAbsent(root, k -> new SQLConfigWrapper(this.provider, new LinkedHashMap<>()));

        return (SQLConfigWrapper) section;
    }

    private String getChild(String path) {
        int index = path.indexOf(SEPARATOR);
        return (index == -1) ? path : path.substring(index + 1);
    }

}
