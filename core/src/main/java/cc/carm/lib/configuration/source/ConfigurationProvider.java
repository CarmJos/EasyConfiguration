package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.easyoptions.OptionHolder;
import cc.carm.lib.easyoptions.OptionType;
import org.jetbrains.annotations.NotNull;

public abstract class ConfigurationProvider<P extends ConfigurationProvider<P>> {

    protected @NotNull ConfigurationLoader<P> loader = new ConfigurationLoader<>();
    protected @NotNull ValueAdapterRegistry<P> adapters = new ValueAdapterRegistry<>();
    protected @NotNull OptionHolder options = new OptionHolder();


    public OptionHolder options() {
        return options;
    }

    public <T> @NotNull T option(@NotNull OptionType<T> option) {
        return options.get(option);
    }

    public <T> void option(@NotNull OptionType<T> option, @NotNull T value) {
        options.set(option, value);
    }

    public ConfigurationLoader<P> loader() {
        return loader;
    }

    public void load(Configuration configuration) {
        loader().load(configuration);
    }


}
