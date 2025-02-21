package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RawMapSection extends MapSection<RawMapSection> {

    public static RawMapSection of() {
        return of((RawMapSection) null);
    }

    public static RawMapSection of(@NotNull Consumer<Map<String, Object>> data) {
        return of(() -> {
            Map<String, Object> map = new LinkedHashMap<>();
            data.accept(map);
            return map;
        });
    }

    public static RawMapSection of(@NotNull Supplier<Map<?, ?>> data) {
        return of(data.get(), null);
    }

    public static RawMapSection of(@NotNull Map<?, ?> data) {
        return of(data, null);
    }

    public static RawMapSection of(@Nullable RawMapSection parent) {
        return of(new LinkedHashMap<>(), parent);
    }

    public static RawMapSection of(@NotNull Map<?, ?> data, @Nullable RawMapSection parent) {
        return new RawMapSection(data, parent);
    }

    protected RawMapSection(@NotNull Map<?, ?> raw, @Nullable RawMapSection parent) {
        super(parent);
        migrate(raw);
    }

    @Override
    public @NotNull RawMapSection self() {
        return this;
    }

    @Override
    protected @NotNull RawMapSection createChild(@NotNull Map<?, ?> data) {
        return new RawMapSection(data, this);
    }

}
