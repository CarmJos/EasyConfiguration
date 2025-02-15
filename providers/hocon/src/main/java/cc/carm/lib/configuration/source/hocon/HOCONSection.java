package cc.carm.lib.configuration.source.hocon;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import com.typesafe.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Map;

public class HOCONSection implements ConfigureSection {

    protected final @NotNull HOCONSource source;
    protected final @Nullable HOCONSection parent;
    protected final @NotNull Config data;

    public HOCONSection(@NotNull HOCONSource source, @Nullable HOCONSection parent,
                        @NotNull Config data) {
        this.source = source;
        this.parent = parent;
        this.data = data;
    }

    public @NotNull Config data() {
        return this.data;
    }

    @Override
    public @NotNull HOCONSource source() {
        return this.source;
    }

    @Override
    public @Nullable HOCONSection parent() {
        return this.parent;
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> getValues(boolean deep) {
        return data().root().unwrapped();
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {

    }

    @Override
    public boolean contains(@NotNull String path) {
        return data().hasPath(path);
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        return data().getAnyRefList(path);
    }

    @Override
    public @Nullable HOCONSection getSection(@NotNull String path) {
        return data().getConfig(path) == null ? null : new HOCONSection(source, this, data().getConfig(path));
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return data().getAnyRef(path);
    }

}
