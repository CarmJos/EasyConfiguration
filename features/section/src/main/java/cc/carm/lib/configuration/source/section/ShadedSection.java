package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShadedSection implements ConfigureSection {

    protected final @NotNull ConfigureSection section;
    protected final @NotNull ConfigureSection template;

    public ShadedSection(@NotNull ConfigureSection section, @Nullable ConfigureSection template) {
        this.section = section;
        this.template = template;
    }

    @Override
    public @Nullable ShadedSection parent() {
        return null;
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> getValues(boolean deep) {
        if (deep) {
            Map<String, Object> values = new LinkedHashMap<>(template.getValues(true));
            values.putAll(section.getValues(true));
            return values;
        }


        return Collections.emptyMap();
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {

        if (value instanceof ConfigureSection) {

        }

    }

    @Override
    public @Nullable Object get(@NotNull String path) {

        if (section.contains(path)) {


            return section.get(path);
        }

        return null;
    }

    @Override
    public void remove(@NotNull String path) {
        section.remove(path);
    }

    @Override
    public @NotNull ConfigureSection createSection(@NotNull Map<?, ?> data) {
        return null;
    }

}