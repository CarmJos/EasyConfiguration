package cc.carm.lib.configuration.source.meta;

public interface StandardMeta {

    /**
     * To mark the {@link cc.carm.lib.configuration.value.ConfigValue} as a minimal unit path.
     */
    ConfigurationMetadata<Boolean> UNIT = ConfigurationMetadata.of(false);

}
