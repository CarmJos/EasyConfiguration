package cc.carm.lib.configuration.sql;

import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SQLValueResolver<T> {

    public static <V> SQLValueResolver<V> of(int id, @NotNull Class<V> clazz,
                                             @NotNull ConfigDataFunction<String, V> resolver) {
        return of(id, clazz, resolver, String::valueOf);
    }

    public static <V> SQLValueResolver<V> of(int id, @NotNull Class<V> clazz,
                                             @NotNull ConfigDataFunction<String, V> resolver,
                                             @NotNull Function<V, String> serializer) {
        return new SQLValueResolver<>(id, clazz, resolver, serializer);
    }

    protected final int id;
    protected final @NotNull Class<T> clazz;
    protected final @NotNull ConfigDataFunction<String, T> resolver;
    protected final @NotNull Function<T, String> serializer;

    protected SQLValueResolver(int id, @NotNull Class<T> clazz,
                               @NotNull ConfigDataFunction<String, T> resolver,
                               @NotNull Function<T, String> serializer) {
        this.id = id;
        this.clazz = clazz;
        this.resolver = resolver;
        this.serializer = serializer;
    }

    public int getID() {
        return id;
    }

    public @NotNull Class<T> getClazz() {
        return clazz;
    }

    public boolean isTypeOf(@NotNull Class<?> clazz) {
        return this.clazz.isAssignableFrom(clazz);
    }

    public @NotNull T resolve(String data) throws Exception {
        return resolver.parse(data);
    }

    public @Nullable String serialize(T value) {
        return String.valueOf(value);
    }

    public @Nullable String serializeObject(Object value) {
        return isTypeOf(value.getClass()) ? serialize(clazz.cast(value)) : null;
    }

}
