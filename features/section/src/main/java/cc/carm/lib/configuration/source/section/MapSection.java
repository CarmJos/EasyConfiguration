package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class MapSection<R extends MapSection<R>> implements ConfigureSection {

    protected final @NotNull Map<String, Object> data;
    protected final @Nullable R parent;

    protected MapSection(@Nullable R parent) {
        this.parent = parent;
        this.data = new LinkedHashMap<>();
    }

    public void migrate(Map<?, ?> data) {
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            String key = (entry.getKey() == null) ? "null" : entry.getKey().toString();
            if (entry.getValue() instanceof Map) {
                this.data.put(key, createChild((Map<?, ?>) entry.getValue()));
            } else if (entry.getValue() instanceof List) {
                List<Object> list = new ArrayList<>();
                for (Object obj : (List<?>) entry.getValue()) {
                    if (obj instanceof Map) {
                        list.add(createChild((Map<?, ?>) obj));
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

    public abstract @NotNull R self();

    protected abstract @NotNull R createChild(@NotNull Map<?, ?> data);

    protected @NotNull R createChild() {
        return createChild(new LinkedHashMap<>());
    }

    public @NotNull Map<String, Object> data() {
        return this.data;
    }

    public @Nullable R parent() {
        return this.parent;
    }

    /**
     * Get the path separator for the section.
     *
     * @return The path separator
     */
    public char pathSeparator() {
        return '.';
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return Collections.unmodifiableMap(deep ? mappingValues(this, null, true) : data());
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (value instanceof Map) value = createChild((Map<?, ?>) value);

        R section = getSectionFor(path);
        if (section == this) {
            // Even this value is null, we still need to put it in the map
            // to ensure that the path is marked as existing.
            this.data.put(path, value);
        } else {
            section.set(childPath(path), value);
        }
    }

    @Override
    public void remove(@NotNull String path) {
        R section = getSectionFor(path);
        if (section != this) {
            section.remove(childPath(path));
        } else {
            this.data.remove(path);
        }
    }

    @Override
    public boolean contains(@NotNull String path) {
        return get(path) != null;
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        R section = getSectionFor(path);
        return section == this ? data.get(path) : section.get(childPath(path));
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        Object val = get(path);
        return (val instanceof List<?>) ? (List<?>) val : null;
    }

    @Override
    public @Nullable ConfigureSection getSection(@NotNull String path) {
        Object val = get(path);
        return (val instanceof ConfigureSection) ? (ConfigureSection) val : null;
    }

    @SuppressWarnings("unchecked")
    private R getSectionFor(String path) {
        int index = path.indexOf(pathSeparator());
        if (index == -1) return self();

        String root = path.substring(0, index);
        return (R) data().computeIfAbsent(root, k -> createChild());
    }

    private String childPath(String path) {
        int index = path.indexOf(pathSeparator());
        return (index == -1) ? path : path.substring(index + 1);
    }

    /**
     * Map the values of the children of the section to the output map.
     *
     * @param section The section to map the values from
     * @param parent  The parent path
     * @param deep    If the mapping should be deep
     */
    protected Map<String, Object> mappingValues(@NotNull MapSection<?> section, @Nullable String parent, boolean deep) {
        Map<String, Object> output = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : section.data().entrySet()) {
            String path = (parent == null ? "" : parent + pathSeparator()) + entry.getKey();
            output.remove(path);
            output.put(path, entry.getValue());
            if (deep && entry.getValue() instanceof MapSection<?>) {
                output.putAll(mappingValues((MapSection<?>) entry.getValue(), path, true));
            }
        }
        return output;
    }
}
