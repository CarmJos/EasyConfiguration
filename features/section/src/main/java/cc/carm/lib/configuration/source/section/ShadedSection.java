package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;

public class ShadedSection implements ConfigureSection {


    @Override
    public @Nullable ConfigureSection parent() {
        return null;
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> getValues(boolean deep) {
        return Collections.emptyMap();
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {

    }

    @Override
    public void remove(@NotNull String path) {

    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return null;
    }
    
}