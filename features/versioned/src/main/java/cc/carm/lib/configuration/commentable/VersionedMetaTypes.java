package cc.carm.lib.configuration.commentable;

import cc.carm.lib.configuration.annotation.ConfigVersion;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.loader.ConfigurationInitializer;
import cc.carm.lib.configuration.source.meta.ConfigurationMetadata;
import org.jetbrains.annotations.NotNull;

public interface VersionedMetaTypes {

    /**
     * The version of specific {@link cc.carm.lib.configuration.value.ConfigValue}.
     * <br> Used for versioning target field for rewrite/upgrade.
     */
    ConfigurationMetadata<Integer> VERSION = ConfigurationMetadata.of(0);

    static void register(@NotNull ConfigurationHolder<?> provider) {
        register(provider.initializer());
    }

    static void register(@NotNull ConfigurationInitializer initializer) {
        initializer.registerAnnotation(ConfigVersion.class, VERSION, ConfigVersion::value);
    }

}
