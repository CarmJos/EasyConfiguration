package cc.carm.lib.configuration.option;

import cc.carm.lib.easyoptions.OptionType;

import static cc.carm.lib.easyoptions.OptionType.of;

public class FileConfigOptions {

    /**
     * Whether to copy files from resource if exists.
     */
    OptionType<Boolean> COPY_DEFAULTS = of(true);


}
