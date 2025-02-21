package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MemorySection extends AbstractMapSection<MemorySection> {

    public static MemorySection of() {
        return of((MemorySection) null);
    }

    public static MemorySection of(@NotNull Consumer<Map<String, Object>> data) {
        return of(() -> {
            Map<String, Object> map = new LinkedHashMap<>();
            data.accept(map);
            return map;
        });
    }

    public static MemorySection of(@NotNull Supplier<Map<?, ?>> data) {
        return of(data.get(), null);
    }

    public static MemorySection of(@NotNull Map<?, ?> data) {
        return of(data, null);
    }

    public static MemorySection of(@Nullable MemorySection parent) {
        return of(new LinkedHashMap<>(), parent);
    }

    public static MemorySection of(@NotNull Map<?, ?> data, @Nullable MemorySection parent) {
        return new MemorySection(data, parent);
    }

    public MemorySection(@NotNull Map<?, ?> raw, @Nullable MemorySection parent) {
        super(parent);
        migrate(raw);
    }

    @Override
    public @NotNull MemorySection self() {
        return this;
    }

    @Override
    public @NotNull MemorySection createSection(@NotNull Map<?, ?> data) {
        return new MemorySection(data, this);
    }

}
