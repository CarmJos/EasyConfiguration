package cc.carm.lib.configuration.source.json;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.configuration.source.section.ConfigureSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class JSONSection implements ConfigureSection {

    protected final @NotNull ConfigureSource<? extends JSONSection, ?, ?> source;
    protected final @NotNull Map<String, Object> data;
    protected final @Nullable JSONSection parent;

    public JSONSection(@NotNull ConfigureSource<? extends JSONSection, ?, ?> source,
                       @NotNull Map<?, ?> data, @Nullable JSONSection parent) {
        this.source = source;
        this.parent = parent;
        this.data = new LinkedHashMap<>();

        for (Map.Entry<?, ?> entry : data.entrySet()) {
            String key = (entry.getKey() == null) ? "null" : entry.getKey().toString();

            if (entry.getValue() instanceof Map) {
                this.data.put(key, new JSONSection(source, (Map<?, ?>) entry.getValue(), this));
            } else if (entry.getValue() instanceof List) {
                List<Object> list = new ArrayList<>();
                for (Object obj : (List<?>) entry.getValue()) {
                    if (obj instanceof Map) {
                        list.add(new JSONSection(source, (Map<?, ?>) obj, this));
                    } else {
                        list.add(obj);
                    }
                }
                this.data.put(key, list);
            } else {
                this.data.put(key, entry.getValue());
            }
        }
    }

    @Override
    public @NotNull ConfigureSource<? extends JSONSection, ?, ?> source() {
        return this.source;
    }

    public @NotNull Map<String, Object> data() {
        return this.data;
    }

    public @Nullable JSONSection parent() {
        return this.parent;
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        if (deep) {
            Map<String, Object> values = new LinkedHashMap<>();
            mapChildrenValues(values, this, null, true);
            return Collections.unmodifiableMap(values);
        } else return Collections.unmodifiableMap(data());
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (value instanceof Map) {
            value = new JSONSection(source(), (Map<?, ?>) value, this);
        }

        JSONSection section = getSectionFor(path);
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
        JSONSection section = getSectionFor(path);
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
    public boolean isSection(@NotNull String path) {
        return get(path) instanceof JSONSection;
    }

    @Override
    public @Nullable ConfigureSection getSection(@NotNull String path) {
        Object val = get(path);
        return (val instanceof ConfigureSection) ? (ConfigureSection) val : null;
    }

    private JSONSection getSectionFor(String path) {
        int index = path.indexOf(separator());
        if (index == -1) return this;

        String root = path.substring(0, index);
        Object section = this.data.get(root);
        if (section == null) {
            section = new JSONSection(source(), new LinkedHashMap<>(), this);
            this.data.put(root, section);
        }

        return (JSONSection) section;
    }

    private String getChild(String path) {
        int index = path.indexOf(separator());
        return (index == -1) ? path : path.substring(index + 1);
    }


    /**
     * Map the values of the children of the section to the output map.
     *
     * @param output  The map to map the values to
     * @param section The section to map the values from
     * @param parent  The parent path
     * @param deep    If the mapping should be deep
     * @author md_5, sk89q
     */
    protected void mapChildrenValues(@NotNull Map<String, Object> output, @NotNull JSONSection section,
                                     @Nullable String parent, boolean deep) {
        for (Map.Entry<String, Object> entry : section.data().entrySet()) {
            String path = (parent == null ? "" : parent + separator()) + entry.getKey();
            output.remove(path);
            output.put(path, entry.getValue());
            if (deep && entry.getValue() instanceof JSONSection) {
                this.mapChildrenValues(output, (JSONSection) entry.getValue(), path, true);
            }
        }
    }
}
