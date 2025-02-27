package cc.carm.lib.configuration.source.sql;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.function.DataFunction;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class SQLValueResolver<T> {

    public static final @NotNull SQLValueResolver<String> STRING = SQLValueResolver.of(ValueType.STRING, s -> s);
    public static final @NotNull SQLValueResolver<Byte> BYTE = SQLValueResolver.of(ValueType.BYTE, Byte::parseByte);
    public static final @NotNull SQLValueResolver<Short> SHORT = SQLValueResolver.of(ValueType.SHORT, Short::parseShort);
    public static final @NotNull SQLValueResolver<Integer> INTEGER = SQLValueResolver.of(ValueType.INTEGER, Integer::parseInt);
    public static final @NotNull SQLValueResolver<Long> LONG = SQLValueResolver.of(ValueType.LONG, Long::parseLong);
    public static final @NotNull SQLValueResolver<Float> FLOAT = SQLValueResolver.of(ValueType.FLOAT, Float::parseFloat);
    public static final @NotNull SQLValueResolver<Double> DOUBLE = SQLValueResolver.of(ValueType.DOUBLE, Double::parseDouble);
    public static final @NotNull SQLValueResolver<Boolean> BOOLEAN = SQLValueResolver.of(ValueType.BOOLEAN, Boolean::parseBoolean);
    public static final @NotNull SQLValueResolver<Character> CHAR = SQLValueResolver.of(ValueType.CHAR, s -> s.charAt(0));

    public static final @NotNull SQLValueResolver<List<?>> LIST = new SQLValueResolver<List<?>>(new ValueType<List<?>>() {
    }) {
        @Override
        public @Nullable List<?> resolve(@NotNull ConfigurationHolder<? extends SQLSource> holder, String data) throws Exception {
            return holder.config().gson().fromJson(data, List.class);
        }

        @Override
        public @Nullable String serialize(@NotNull ConfigurationHolder<? extends SQLSource> holder, Object value) {
            return holder.config().gson().toJson(value);
        }
    };

    public static final @NotNull SQLValueResolver<Map<?, ?>> MAP = new SQLValueResolver<Map<?, ?>>(new ValueType<Map<?, ?>>() {
    }) {
        @Override
        public @Nullable Map<?, ?> resolve(@NotNull ConfigurationHolder<? extends SQLSource> holder, String data) throws Exception {
            return holder.config().gson().fromJson(data, LinkedHashMap.class);
        }

        @Override
        public @Nullable String serialize(@NotNull ConfigurationHolder<? extends SQLSource> holder, Object value) {
            return holder.config().gson().toJson(value);
        }
    };

    public static final @NotNull Map<Integer, SQLValueResolver<?>> STANDARD_RESOLVERS = Collections.unmodifiableMap(standards());

    static Map<Integer, SQLValueResolver<?>> standards() {
        Map<Integer, SQLValueResolver<?>> map = new LinkedHashMap<>();
        map.put(0, STRING);
        map.put(1, BYTE);
        map.put(2, SHORT);
        map.put(3, INTEGER);
        map.put(4, LONG);
        map.put(5, FLOAT);
        map.put(6, DOUBLE);
        map.put(7, BOOLEAN);
        map.put(8, CHAR);
        map.put(10, LIST);
        map.put(11, MAP);
        return map;
    }

    public static <V> SQLValueResolver<V> of(@NotNull ValueType<V> type, @NotNull DataFunction<String, V> resolver) {
        return new SQLValueResolver<V>(type) {
            @Override
            public @NotNull V resolve(@NotNull ConfigurationHolder<? extends SQLSource> holder, String data) throws Exception {
                return resolver.handle(data);
            }
        };
    }

    protected final @NotNull ValueType<T> type;

    protected SQLValueResolver(@NotNull ValueType<T> type) {
        this.type = type;
    }

    public @NotNull ValueType<T> getType() {
        return type;
    }

    public boolean isTypeOf(@NotNull Class<?> clazz) {
        return type.isSubtypeOf(clazz);
    }

    public boolean isTypeOf(@NotNull ValueType<?> valueType) {
        return valueType.equals(type);
    }

    public abstract @Nullable T resolve(@NotNull ConfigurationHolder<? extends SQLSource> holder, String data) throws Exception;

    public @Nullable String serialize(@NotNull ConfigurationHolder<? extends SQLSource> holder, Object value) throws Exception {
        return String.valueOf(value);
    }

}
