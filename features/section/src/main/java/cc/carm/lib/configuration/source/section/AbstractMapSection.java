package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public abstract class AbstractMapSection<R extends AbstractMapSection<R>> implements ConfigureSection {

    protected final @NotNull Map<String, Object> data;
    protected final @Nullable R parent;
    protected final @NotNull String path;

    protected AbstractMapSection(@Nullable R parent, @NotNull String path) {
        this.parent = parent;
        this.path = path;
        this.data = new LinkedHashMap<>();
    }

    public void migrate(Map<?, ?> data) {
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            String key = (entry.getKey() == null) ? "" : entry.getKey().toString();
            if (entry.getValue() instanceof Map) {
                this.data.put(key, createSection(key, (Map<?, ?>) entry.getValue()));
            } else if (entry.getValue() instanceof List) {
                List<Object> list = new ArrayList<>();
                int index = 0;
                for (Object obj : (List<?>) entry.getValue()) {
                    if (obj instanceof Map) {
                        list.add(createSection(key + "[" + index + "]", (Map<?, ?>) obj));
                    } else {
                        list.add(obj);
                    }
                    index++;
                }
                this.data.put(key, list);
            } else {
                this.data.put(key, entry.getValue());
            }
        }
    }

    public abstract @NotNull R self();

    @Override
    public abstract @NotNull R createSection(@NotNull String path, @NotNull Map<?, ?> data);

    @Override
    public @NotNull String path() {
        return this.path;
    }

    @Override
    public boolean contains(@NotNull String path) {
        R section = getSectionFor(path);
        if (section == this) {
            return this.data().containsKey(path);
        } else {
            return section.contains(childPath(path));
        }
    }

    public @NotNull Map<String, Object> data() {
        return this.data;
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public int size(boolean deep) {
        return deep ? getKeys(true).size() : this.data.size();
    }

    @UnmodifiableView
    public @NotNull Map<String, Object> rawMap() {
        Map<String, Object> output = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : this.data.entrySet()) {
            if (entry.getValue() instanceof AbstractMapSection<?>) {
                output.put(entry.getKey(), ((AbstractMapSection<?>) entry.getValue()).rawMap());
            } else if (entry.getValue() instanceof List<?>) {
                List<Object> list = new ArrayList<>();
                for (Object obj : (List<?>) entry.getValue()) {
                    if (obj instanceof AbstractMapSection<?>) {
                        list.add(((AbstractMapSection<?>) obj).rawMap());
                    } else {
                        list.add(obj);
                    }
                }
                output.put(entry.getKey(), list);
            } else {
                output.put(entry.getKey(), entry.getValue());
            }
        }
        return output;
    }

    public @Nullable R parent() {
        return this.parent;
    }


    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return Collections.unmodifiableMap(deep ? mappingValues(this, null, true, String.valueOf(pathSeparator())) : data());
    }

    @Override
    public @NotNull @UnmodifiableView Set<String> getKeys(boolean deep) {
        return Collections.unmodifiableSet(deep ? mappingKeys(this, null, true, String.valueOf(pathSeparator())) : data().keySet());
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (value instanceof Map) value = createSection(path, (Map<?, ?>) value);

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
    public @Nullable Object get(@NotNull String path) {
        R section = getSectionFor(path);
        return section == this ? data.get(path) : section.get(childPath(path));
    }

    @SuppressWarnings("unchecked")
    private R getSectionFor(String path) {
        int index = path.indexOf(pathSeparator());
        if (index == -1) return self();

        String root = path.substring(0, index);
        return (R) computeSection(root);
    }

    /**
     * Map the values of the children of the section to the output map.
     *
     * @param section The section to map the values from
     * @param parent  The parent path
     * @param deep    If the mapping should be deep
     */
    protected static Map<String, Object> mappingValues(@NotNull AbstractMapSection<?> section, @Nullable String parent, boolean deep, String pathSeparator) {
        Map<String, Object> output = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : section.data().entrySet()) {
            String path = (parent == null ? "" : parent + pathSeparator) + entry.getKey();
            output.remove(path);
            output.put(path, entry.getValue());
            if (deep && entry.getValue() instanceof AbstractMapSection<?>) {
                output.putAll(mappingValues((AbstractMapSection<?>) entry.getValue(), path, true, pathSeparator));
            }
        }
        return output;
    }

    protected static Set<String> mappingKeys(@NotNull AbstractMapSection<?> section, @Nullable String parent, boolean deep, String pathSeparator) {
        Set<String> keys = new LinkedHashSet<>();
        for (Map.Entry<String, Object> entry : section.data().entrySet()) {
            String path = (parent == null ? "" : parent + pathSeparator) + entry.getKey();
            keys.add(path);
            if (deep && entry.getValue() instanceof AbstractMapSection<?>) {
                keys.addAll(mappingKeys((AbstractMapSection<?>) entry.getValue(), path, true, pathSeparator));
            }
        }
        return keys;
    }


}
