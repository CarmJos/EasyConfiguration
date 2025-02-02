package cc.carm.lib.configuration.adapter;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Value deserializer, convert base data to target value.
 *
 * @param <TYPE> The type of target value
 */
@FunctionalInterface
public interface ValueParser<TYPE> {

    TYPE deserialize(
            @NotNull ConfigurationProvider<?> provider,
            @NotNull ValueType<? super TYPE> type, @NotNull Object data
    ) throws Exception;

}
