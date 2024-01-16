package cc.carm.lib.configuration.adapter;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Value serializer, convert target value to base data.
 *
 * @param <P> Configuration provider
 * @param <B> The type of base data
 * @param <V> The type of value
 */
@FunctionalInterface
public interface ValueSerializer<P extends ConfigurationProvider, B, V> {

    B serialize(@NotNull P provider, @NotNull V value) throws Exception;

}
