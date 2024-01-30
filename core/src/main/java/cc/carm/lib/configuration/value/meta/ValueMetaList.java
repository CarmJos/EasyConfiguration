package cc.carm.lib.configuration.value.meta;

public interface ValueMetaList {

    /**
     * The value path in configuration.
     * Also see {@link cc.carm.lib.configuration.option.ConfigurationOptions#PATH_SEPARATOR}
     */
    ValueMetaType<String> PATH = ValueMetaType.of();

}
