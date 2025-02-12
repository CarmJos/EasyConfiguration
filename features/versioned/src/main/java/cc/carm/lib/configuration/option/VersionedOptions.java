package cc.carm.lib.configuration.option;

import cc.carm.lib.configuration.source.option.ConfigurationOption;

public interface VersionedOptions {

    /**
     * Whether to set newer defaults when a {@link cc.carm.lib.configuration.value.ConfigValue}'s marked version
     * is newer than the current in storage.
     */
    ConfigurationOption<Boolean> RESET_NEWER_DEFAULTS = ConfigurationOption.of(true);

}
