package cc.carm.lib.configuration.source.yaml;

import cc.carm.lib.configuration.source.option.ConfigurationOption;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;

public interface YAMLOptions {


    ConfigurationOption<LoaderOptions> LOADER = ConfigurationOption.of(() -> {
        LoaderOptions loaderOptions = new LoaderOptions();
        // As we handle comments ourselves,
        // we don't want SnakeYAML to read them when loading the configs.
        loaderOptions.setProcessComments(false);
        loaderOptions.setMaxAliasesForCollections(100); // 100 aliases
        loaderOptions.setCodePointLimit(5 * 1024 * 1024); // 5MB
        return loaderOptions;
    });

    ConfigurationOption<DumperOptions> DUMPER = ConfigurationOption.of(() -> {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setWidth(120);
        options.setProcessComments(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return options;
    });


}
