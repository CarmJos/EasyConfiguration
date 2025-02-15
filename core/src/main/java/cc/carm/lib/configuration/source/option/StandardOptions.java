package cc.carm.lib.configuration.source.option;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.source.ConfigurationHolder;

import static cc.carm.lib.configuration.source.option.ConfigurationOption.of;

public interface StandardOptions {

    /**
     * The configuration path separator.
     */
    ConfigurationOption<Character> PATH_SEPARATOR = of('.');

    /**
     * Whether to set and save default values if offered and not exists in configuration.
     */
    ConfigurationOption<Boolean> SET_DEFAULTS = of(true);

    /**
     * Whether to load subclasses of configuration class.
     */
    ConfigurationOption<Boolean> LOAD_SUB_CLASSES = of(true);

    /**
     * Whether to pre parse the config values.
     * <br> if false, the values will be parsed when calling
     * {@link cc.carm.lib.configuration.value.ConfigValue#get()}
     * <br> if true, the values will be parsed when
     * {@link ConfigurationHolder#initialize(Configuration)}.
     */
    ConfigurationOption<Boolean> PRELOAD = of(false);

}
