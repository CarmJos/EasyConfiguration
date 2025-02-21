package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.Supplier;

public class ShadedSection implements ConfigureSection {

    protected @NotNull ConfigureSection template;
    protected @Nullable ConfigureSection source;

    public ShadedSection(@NotNull ConfigureSection template, @Nullable ConfigureSection source) {
        this.template = template;
        this.source = source;
    }

    @Override
    public @Nullable ConfigureSection parent() {
        return null;
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> getValues(boolean deep) {
        if (source == null) {
            return template.getValues(deep);
        }

        // 本函数为，当 getValues 时，递归合并 source 和 template
        return merge(template, source).getValues(deep);
    }

    private ConfigureSection merge(ConfigureSection templateSection, ConfigureSection valueSection) {
        MemorySection merged = MemorySection.of();
        List<String> existingKey = new ArrayList<>();

        for (Map.Entry<String, Object> entry : valueSection.getValues(false).entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof ConfigureSection) {
                ConfigureSection subSectionFromValue = (ConfigureSection) value;
                ConfigureSection subSectionFromTemplate = (ConfigureSection) templateSection.get(key);
                if(subSectionFromTemplate == null) {
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
            if (existingKey.contains(entry.getKey())) {
                continue;
            }
            merged.set(entry.getKey(), entry.getValue());
        }

        return merged;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (value instanceof ConfigureSection) {
            ConfigureSection targetSection = (ConfigureSection) value;
            for (Map.Entry<String, Object> entry : targetSection.getValues(true).entrySet()) {
                set(path+pathSeparator()+entry.getKey(), entry.getValue());
            }
            return;
        } else if (Objects.equals(get(path), value)){
            remove(path);
            return;
        }

        Optional.ofNullable(source).ifPresent(source -> source.set(path, value));
    }

    @Override
    public void remove(@NotNull String path) {
        Optional.ofNullable(source).ifPresent(source -> source.remove(path));
    }

    @Override
    public @NotNull ConfigureSection createSection(@NotNull Map<?, ?> data) {
        throw new UnsupportedOperationException("not supported yet");
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
              return new ShadedSection(templateSection, (ConfigureSection) value);
            }
        }
        return value;
    }

    public @Nullable Object getFromTemplate(@NotNull String path) {
        Object value = template.get(path);
        if (value instanceof ConfigureSection) {
            return new ShadedSection((ConfigureSection) value, null);
        } else {
            return value;
        }
    }

    public ConfigureSection getSource() {
        return source;
    }
}