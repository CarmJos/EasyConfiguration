package cc.carm.lib.configuration.adapter;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Value adapter, used to convert the value of the configuration file into the objects.
 *
 * @param <TYPE> The type of the target value
 */
public class ValueAdapter<TYPE>
        implements ValueSerializer<TYPE>, ValueParser<TYPE> {

    protected final @NotNull ValueType<TYPE> type;
    protected @Nullable ValueSerializer<TYPE> serializer;
    protected @Nullable ValueParser<TYPE> deserializer;

    public ValueAdapter(@NotNull ValueType<TYPE> type) {
        this(type, null, null);
    }

    public ValueAdapter(@NotNull ValueType<TYPE> type,
                        @Nullable ValueSerializer<TYPE> serializer,
                        @Nullable ValueParser<TYPE> deserializer) {
        this.type = type;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public @NotNull ValueType<TYPE> type() {
        return type;
    }

    public @Nullable ValueSerializer<TYPE> serializer() {
        return serializer;
    }

    public @Nullable ValueParser<TYPE> parser() {
        return deserializer;
    }

    public void serializer(@Nullable ValueSerializer<TYPE> serializer) {
        this.serializer = serializer;
    }

    public void parser(@Nullable ValueParser<TYPE> deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public Object serialize(@NotNull ConfigurationProvider<?> provider, @NotNull ValueType<? super TYPE> type, @NotNull TYPE value) throws Exception {
        if (serializer == null) throw new UnsupportedOperationException("Serializer is not supported");
        return serializer.serialize(provider, type, value);
    }

    @Override
    public TYPE parse(@NotNull ConfigurationProvider<?> provider, @NotNull ValueType<? super TYPE> type, @NotNull Object value) throws Exception {
        if (deserializer == null) throw new UnsupportedOperationException("Deserializer is not supported");
        return deserializer.parse(provider, type, value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ValueAdapter)) return false;
        ValueAdapter<?> that = (ValueAdapter<?>) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }
}

