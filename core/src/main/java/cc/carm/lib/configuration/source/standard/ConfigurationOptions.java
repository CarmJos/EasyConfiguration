package cc.carm.lib.configuration.source.standard;


import cc.carm.lib.easyoptions.OptionType;

import static cc.carm.lib.easyoptions.OptionType.of;

public interface ConfigurationOptions {

    /**
     * The configuration path separator.
     */
    OptionType<Character> PATH_SEPARATOR = of('.');

    /**
     * Whether to copy files from resource if exists.
     */
    OptionType<Boolean> COPY_DEFAULTS = of(true);

    /**
     * Whether to save default values if offered and not exists in configuration.
     */
    OptionType<Boolean> SAVE_DEFAULTS = of(true);

    /**
     * Whether to load subclasses of configuration class.
     */
    OptionType<Boolean> LOAD_SUB_CLASSES = of(true);

}
