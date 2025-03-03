package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public class ShadedSection implements ConfigureSection {

    public static ShadedSection create(@NotNull ConfigureSection template, @Nullable ConfigureSection source) {
        return new ShadedSection(null, template, source);
    }

    protected final @Nullable ShadedSection parent;
    protected @NotNull ConfigureSection template;
    protected @Nullable ConfigureSection source;

    public ShadedSection(@Nullable ShadedSection parent,
                         @NotNull ConfigureSection template, @Nullable ConfigureSection source) {
        this.parent = parent;
        this.template = template;
        this.source = source;
    }

    @Override
    public @Nullable ConfigureSection parent() {
        return this.parent;
    }

    @Override
    public @NotNull String path() {
        return this.template.path();
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> getValues(boolean deep) {
        if (source == null) return template.getValues(deep);
        // 本函数为，当 getValues 时，递归合并 source 和 template
        return merge(template, source).getValues(deep);
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> asMap() {
        if (source == null) return template.asMap();
        return merge(template, source).asMap();
    }

    @Override
    public @NotNull @UnmodifiableView Set<String> getKeys(boolean deep) {
        Set<String> keys = new HashSet<>(template.getKeys(deep));
        if (source != null) {
            keys.addAll(source.getKeys(deep));
        }
        return Collections.unmodifiableSet(keys);
    }

    private ConfigureSection merge(ConfigureSection templateSection, ConfigureSection valueSection) {
        MemorySection merged = MemorySection.of();
        Set<String> existingKey = new HashSet<>();

        for (Map.Entry<String, Object> entry : valueSection.getValues(false).entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof ConfigureSection) {
                ConfigureSection subSectionFromValue = (ConfigureSection) value;
                ConfigureSection subSectionFromTemplate = (ConfigureSection) templateSection.get(key);
                if (subSectionFromTemplate == null) {
                    merged.set(key, value);
                } else {
                    merged.set(key, merge(subSectionFromTemplate, subSectionFromValue));
                }
            } else {
                merged.set(key, value);
            }
            existingKey.add(key);
        }

        for (Map.Entry<String, Object> entry : templateSection.getValues(false).entrySet()) {
            if (existingKey.contains(entry.getKey())) continue;
            merged.set(entry.getKey(), entry.getValue());
        }

        return merged;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (value instanceof ConfigureSection) {
            ConfigureSection targetSection = (ConfigureSection) value;
            for (Map.Entry<String, Object> entry : targetSection.getValues(true).entrySet()) {
                set(path + pathSeparator() + entry.getKey(), entry.getValue());
            }
            return;
        } else if (Objects.equals(get(path), value)) {
            remove(path);
            return;
        }

        Optional.ofNullable(source).ifPresent(s -> s.set(path, value));
    }

    @Override
    public void remove(@NotNull String path) {
        Optional.ofNullable(source).ifPresent(s -> s.remove(path));
    }

    @Override
    public @NotNull ConfigureSection createSection(@NotNull String path, @NotNull Map<?, ?> data) {
        if (source == null) {
            return new ShadedSection(this, template, MemorySection.of(data));
        } else {
            ConfigureSection section = source.computeSection(path, data);
            return new ShadedSection(this, template, section);
        }
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        if (source == null) {
            return getFromTemplate(path);
        }

        Object value = source.get(path);
        if (value == null) {
            return getFromTemplate(path);
        }
        if (value instanceof ConfigureSection) {
            ConfigureSection templateSection = (ConfigureSection) template.get(path);
            if (templateSection == null) {
                return value;
            } else {
                return new ShadedSection(this, templateSection, (ConfigureSection) value);
            }
        }
        return value;
    }

    public @Nullable Object getFromTemplate(@NotNull String path) {
        Object value = template.get(path);
        if (value instanceof ConfigureSection) {
            return new ShadedSection(this, (ConfigureSection) value, null);
        } else {
            return value;
        }
    }

    public @Nullable ConfigureSection getSource() {
        return source;
    }
}