package cc.carm.lib.configuration.adapter;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Value serializer, convert target value to base data.
 *
 * @param <B> The type of base data
 * @param <V> The type of value
 */
@FunctionalInterface
public interface ValueSerializer<B, V> {

    B serialize(@NotNull ConfigurationProvider<?> provider, @NotNull V value) throws Exception;

}
