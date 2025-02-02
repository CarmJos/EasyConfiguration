package cc.carm.lib.configuration.option;

import cc.carm.lib.configuration.source.option.ConfigurationOption;

public class FileConfigOptions {

    /**
     * Whether to copy files from resource if exists.
     */
    ConfigurationOption<Boolean> COPY_DEFAULTS = ConfigurationOption.of(true);

}
