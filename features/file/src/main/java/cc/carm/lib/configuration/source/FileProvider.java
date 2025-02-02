package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.meta.PathMetadata;
import cc.carm.lib.configuration.source.loader.ConfigurationLoader;
import cc.carm.lib.configuration.source.option.ConfigurationOptionHolder;
import cc.carm.lib.configuration.source.section.ConfigurationSource;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class FileProvider<S extends ConfigurationSource<S, ?>> extends ConfigurationProvider<S> {

    public FileProvider(@NotNull S source, @NotNull ConfigurationLoader loader,
                        @NotNull ValueAdapterRegistry adapters, @NotNull ConfigurationOptionHolder options,
                        @NotNull Map<String, Map<PathMetadata<?>, Object>> pathMetadata) {
        super(source, loader, adapters, options, pathMetadata);
    }


}
