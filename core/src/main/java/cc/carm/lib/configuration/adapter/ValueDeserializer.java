package cc.carm.lib.configuration.adapter;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Value deserializer, convert base data to target value.
 *
 * @param <B> The type of base data
 * @param <V> The type of target value
 */
@FunctionalInterface
public interface ValueDeserializer<B, V> {

    V deserialize(@NotNull ConfigurationProvider<?> provider, @NotNull Class<? extends V> clazz, @NotNull B data) throws Exception;

}
