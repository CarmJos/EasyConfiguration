package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class MemorySection extends MapSection<MemorySection> {

    public static @NotNull MemorySection root(@NotNull ConfigureSource<? extends MemorySection, ?, ?> source) {
        return new MemorySection(source, new LinkedHashMap<>(), null);
    }

    public static @NotNull MemorySection root(@NotNull ConfigureSource<? extends MemorySection, ?, ?> source,
                                              @Nullable Map<?, ?> raw) {
        return new MemorySection(source, raw == null ? new LinkedHashMap<>() : raw, null);
    }

    protected final @NotNull ConfigureSource<? extends MemorySection, ?, ?> source;

    protected MemorySection(@NotNull ConfigureSource<? extends MemorySection, ?, ?> source,
                            @NotNull Map<?, ?> raw, @Nullable MemorySection parent) {
        super(parent);
        this.source = source;
        migrate(raw);
    }

    public @NotNull ConfigureSource<? extends MemorySection, ?, ?> source() {
        return source;
    }

    @Override
    public char pathSeparator() {
        return source().pathSeparator();
    }

    @Override
    public @NotNull MemorySection self() {
        return this;
    }

    @Override
    protected @NotNull MemorySection createChild(@NotNull Map<?, ?> data) {
        return new MemorySection(source(), data, this);
    }

}
