package cc.carm.lib.configuration.json;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Some code comes from BungeeCord's implementation of the JsonConfiguration.
 *
 * @author md_5, CarmJos
 */
public class JSONConfigWrapper implements ConfigurationWrapper {

    private static final char SEPARATOR = '.';
    protected final Map<String, Object> data;

    JSONConfigWrapper(Map<?, ?> map) {
        this.data = new LinkedHashMap<>();

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = (entry.getKey() == null) ? "null" : entry.getKey().toString();

            if (entry.getValue() instanceof Map) {
                this.data.put(key, new JSONConfigWrapper((Map<?, ?>) entry.getValue()));
            } else {
                this.data.put(key, entry.getValue());
            }
        }
    }


    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return new LinkedHashSet<>(this.data.keySet());
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return new LinkedHashMap<>(this.data);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (value instanceof Map) {
            value = new JSONConfigWrapper((Map<?, ?>) value);
        }

        JSONConfigWrapper section = getSectionFor(path);
        if (section == this) {
            if (value == null) {
                this.data.remove(path);
            } else {
                this.data.put(path, value);
            }
        } else {
            section.set(getChild(path), value);
        }
    }

    @Override
    public boolean contains(@NotNull String path) {
        return get(path) != null;
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        JSONConfigWrapper section = getSectionFor(path);
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
        return get(path) instanceof JSONConfigWrapper;
    }

    @Override
    public @Nullable ConfigurationWrapper getConfigurationSection(@NotNull String path) {
        Object val = get(path);
        return (val instanceof JSONConfigWrapper) ? (JSONConfigWrapper) val : null;
    }

    private JSONConfigWrapper getSectionFor(String path) {
        int index = path.indexOf(SEPARATOR);
        if (index == -1) return this;

        String root = path.substring(0, index);
        Object section = this.data.get(root);
        if (section == null) {
            section = new JSONConfigWrapper(new LinkedHashMap<>());
            this.data.put(root, section);
        }

        return (JSONConfigWrapper) section;
    }

    private String getChild(String path) {
        int index = path.indexOf(SEPARATOR);
        return (index == -1) ? path : path.substring(index + 1);
    }

}
