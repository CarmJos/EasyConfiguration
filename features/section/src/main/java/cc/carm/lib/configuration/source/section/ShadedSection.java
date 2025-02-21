package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;

public class ShadedSection implements ConfigureSection {


    protected final @Nullable ShadedSection parent;
    protected final @NotNull ConfigureSection section;
    protected final @Nullable ConfigureSection template;

    public ShadedSection(@NotNull ShadedSection parent,
                         @NotNull ConfigureSection section, @Nullable ConfigureSection template) {
        this.parent = parent;
        this.section = section;
        this.template = template;
    }


    @Override
    public @Nullable ShadedSection parent() {
        return this.parent;
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> getValues(boolean deep) {
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

}