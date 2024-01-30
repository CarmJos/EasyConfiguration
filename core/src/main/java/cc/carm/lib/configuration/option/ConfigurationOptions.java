package cc.carm.lib.configuration.option;


import cc.carm.lib.easyoptions.OptionType;

import static cc.carm.lib.easyoptions.OptionType.of;

public interface ConfigurationOptions {

    /**
     * The configuration path separator.
     */
    OptionType<Character> PATH_SEPARATOR = of('.');

    /**
     * Whether to set & save default values if offered and not exists in configuration.
     */
    OptionType<Boolean> SET_DEFAULTS = of(true);

    /**
     * Whether to load subclasses of configuration class.
     */
    OptionType<Boolean> LOAD_SUB_CLASSES = of(true);

}
