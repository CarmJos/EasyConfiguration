package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SourcedSection extends AbstractMapSection<SourcedSection> {

    public static @NotNull SourcedSection root(@NotNull ConfigureSource<? extends SourcedSection, ?, ?> source) {
        return new SourcedSection(source, new LinkedHashMap<>(), null, "");
    }

    public static @NotNull SourcedSection root(@NotNull ConfigureSource<? extends SourcedSection, ?, ?> source,
                                               @Nullable Map<?, ?> raw) {
        return new SourcedSection(source, raw == null ? new LinkedHashMap<>() : raw, null, "");
    }

    protected final @NotNull ConfigureSource<? extends SourcedSection, ?, ?> source;

    public SourcedSection(@NotNull ConfigureSource<? extends SourcedSection, ?, ?> source,
                          @NotNull Map<?, ?> raw, @Nullable SourcedSection parent, @NotNull String path) {
        super(parent, path);
        this.source = source;
        migrate(raw);
    }

    public @NotNull ConfigureSource<? extends SourcedSection, ?, ?> source() {
        return source;
    }

    @Override
    public char pathSeparator() {
        return source().pathSeparator();
    }

    @Override
    public @NotNull SourcedSection self() {
        return this;
    }

    @Override
    public @NotNull SourcedSection createSection(@NotNull String path, @NotNull Map<?, ?> data) {
        return new SourcedSection(source(), data, this, path);
    }

}
