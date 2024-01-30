package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.loader.ConfigurationLoader;
import cc.carm.lib.easyoptions.OptionHolder;
import cc.carm.lib.easyoptions.OptionType;
import org.jetbrains.annotations.NotNull;

public class ConfigurationProvider<S extends ConfigurationSource<S, ?>> {

    protected final @NotNull S source;
    protected final @NotNull ConfigurationLoader loader;
    protected final @NotNull ValueAdapterRegistry adapters;
    protected final @NotNull OptionHolder options;

    public ConfigurationProvider(@NotNull S source, @NotNull ConfigurationLoader loader,
                                 @NotNull ValueAdapterRegistry adapters, @NotNull OptionHolder options) {
        this.source = source;
        this.loader = loader;
        this.adapters = adapters;
        this.options = options;
    }

    public @NotNull S source() {
        return source;
    }

    public void reload() throws Exception {
        source().reload();
    }

    public void save() throws Exception {
        source().save();
    }

    public OptionHolder options() {
        return options;
    }

    public <T> @NotNull T option(@NotNull OptionType<T> option) {
        return options.get(option);
    }

    public <T> void option(@NotNull OptionType<T> option, @NotNull T value) {
        options.set(option, value);
    }

    public ValueAdapterRegistry adapters() {
        return this.adapters;
    }


    public ConfigurationLoader loader() {
        return loader;
    }

    public void load(Class<? extends Configuration> configClass) {
        try {
            loader.load(this, configClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(@NotNull Configuration config) {
        try {
            loader.load(this, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
