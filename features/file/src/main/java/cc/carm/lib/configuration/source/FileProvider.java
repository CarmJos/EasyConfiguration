package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.source.meta.ConfigurationMetadata;
import cc.carm.lib.configuration.source.loader.ConfigurationInitializer;
import cc.carm.lib.configuration.source.option.ConfigurationOptionHolder;
import cc.carm.lib.configuration.source.section.ConfigurationSource;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class FileProvider<S extends ConfigurationSource<S, ?>> extends ConfigurationProvider<S> {

    public FileProvider(@NotNull S source, @NotNull ConfigurationInitializer loader,
                        @NotNull ValueAdapterRegistry adapters, @NotNull ConfigurationOptionHolder options,
                        @NotNull Map<String, Map<ConfigurationMetadata<?>, Object>> pathMetadata) {
        super(source, loader, adapters, options, pathMetadata);
    }


}
