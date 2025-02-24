package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MemorySection extends AbstractMapSection<MemorySection> {

    public static MemorySection of() {
        return of(new LinkedHashMap<>());
    }

    public static MemorySection of(@NotNull Consumer<Map<String, Object>> data) {
        return of(() -> {
            Map<String, Object> map = new LinkedHashMap<>();
            data.accept(map);
            return map;
        });
    }

    public static MemorySection of(@NotNull Supplier<Map<?, ?>> data) {
        return of(data.get());
    }

    public static MemorySection of(@NotNull Map<?, ?> data) {
        return of(data, null, "");
    }

    public static MemorySection of(@Nullable MemorySection parent, @NotNull String path) {
        return of(new LinkedHashMap<>(), parent, path);
    }

    public static MemorySection of(@NotNull Map<?, ?> data, @Nullable MemorySection parent, @NotNull String path) {
        return new MemorySection(data, parent, path);
    }

    public MemorySection(@NotNull Map<?, ?> raw, @Nullable MemorySection parent, @NotNull String path) {
        super(parent, path);
        migrate(raw);
    }

    @Override
    public @NotNull MemorySection self() {
        return this;
    }

    @Override
    public @NotNull MemorySection createSection(@NotNull String path, @NotNull Map<?, ?> data) {
        return new MemorySection(data, this, path);
    }

}
