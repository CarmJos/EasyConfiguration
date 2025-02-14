package cc.carm.lib.configuration.source.yaml;

import cc.carm.lib.configuration.source.option.ConfigurationOption;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;

public interface YAMLOptions {

    /**
     * The {@link LoaderOptions} for SnakeYAML.
     *
     * @see LoaderOptions
     */
    ConfigurationOption<LoaderOptions> LOADER = ConfigurationOption.of(() -> {
        LoaderOptions opt = new LoaderOptions();

        // As we handle comments ourselves,
        // we don't want SnakeYAML to read them when loading the configs.
        opt.setProcessComments(false);

        opt.setMaxAliasesForCollections(100); // 100 aliases
        opt.setCodePointLimit(5 * 1024 * 1024); // 5MB
        return opt;
    });

    /**
     * The {@link DumperOptions} for SnakeYAML.
     *
     * @see DumperOptions
     */
    ConfigurationOption<DumperOptions> DUMPER = ConfigurationOption.of(() -> {
        DumperOptions opt = new DumperOptions();

        // As we handle comments ourselves,
        // we don't want SnakeYAML to read them when saving the configs.
        opt.setProcessComments(false);

        opt.setIndent(2);
        opt.setWidth(120);
        opt.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return opt;
    });


}
