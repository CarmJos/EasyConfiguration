package cc.carm.lib.configuration.adapter;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Used to get the generic type.
 */
public abstract class ValueType<T> {

    @SuppressWarnings("unchecked")
    public static <T> ValueType<T> of(@NotNull T value) {
        return of((Class<T>) value.getClass());
    }

    public static <T> ValueType<T> of(final Type type) {
        return new ValueType<T>(type) {
        };
    }

    public static <T> ValueType<T> of(final Class<T> clazz) {
        return of((Type) clazz);
    }

    /**
     * Get the generic type of the complex type.
     *
     * @param rawType The raw type
     * @param types   The type arguments
     * @param <T>     The type
     * @return The {@link ValueType}
     */
    public static <T> ValueType<T> of(final Class<?> rawType, final Type... types) {
        ParameterizedType parameterizedType = new ParameterizedType() {
            @Override
            public @NotNull Type @NotNull [] getActualTypeArguments() {
                return types;
            }

            @Override
            public @NotNull Type getRawType() {
                return rawType;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        return of(parameterizedType);
    }

    private final Type type;

    protected ValueType() {
        this.type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private ValueType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean isSubtypeOf(Class<?> target) {
        Class<?> rawType = getRawType();
        return target.isAssignableFrom(rawType);
    }

    public boolean isSubtypeOf(ValueType<?> target) {
        return target.isSubtypeOf(getRawType());
    }

    public boolean isInstance(Object obj) {
        return obj != null && getRawType().isInstance(obj);
    }


    /**
     * 提取当前 ValueType 的原始类型（Class 对象）。
     *
     * @return 对应的 Class 对象
     * @throws IllegalStateException 如果无法提取出原始类型
     */
    public Class<?> getRawType() {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type raw = pt.getRawType();
            if (raw instanceof Class<?>) {
                return (Class<?>) raw;
            }
        }
        throw new IllegalStateException("Unsupported type: " + type);
    }

    @SuppressWarnings("unchecked")
    public T cast(Object obj) {
        if (!isInstance(obj)) {
            throw new ClassCastException("Cannot cast object " + obj + " to type " + this);
        }
        return (T) obj;
    }

    @Override
    public String toString() {
        if (type instanceof Class<?>) {
            return ((Class<?>) type).getName();
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type raw = pt.getRawType();
            StringBuilder sb = new StringBuilder();
            sb.append(raw.getTypeName());
            sb.append('<');
            Type[] args = pt.getActualTypeArguments();
            for (int i = 0; i < args.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(args[i].getTypeName());
            }
            return sb.toString();
        }
        return type.getTypeName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof ValueType) {
            return Objects.equals(type, ((ValueType<?>) obj).type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

}
