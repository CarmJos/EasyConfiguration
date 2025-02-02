package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.source.loader.ConfigurationLoader;
import cc.carm.lib.configuration.source.loader.PathGenerator;
import cc.carm.lib.configuration.source.option.ConfigurationOption;
import cc.carm.lib.configuration.source.option.ConfigurationOptionHolder;
import cc.carm.lib.configuration.source.section.ConfigurationSource;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ConfigurationFactory<SOURCE extends ConfigurationSource<SOURCE, ?>, PROVIDER extends ConfigurationProvider<SOURCE>, SELF> {

    protected Function<PROVIDER, ConfigurationLoader> loaderFunction = PROVIDER -> new ConfigurationLoader();
    protected Consumer<ConfigurationLoader> loaderConsumer = loader -> {
    };

    protected ValueAdapterRegistry adapters = new ValueAdapterRegistry();
    protected ConfigurationOptionHolder options = new ConfigurationOptionHolder();

    public abstract SELF self();

    public SELF loader(Function<PROVIDER, ConfigurationLoader> loaderFunction) {
        this.loaderFunction = loaderFunction;
        return self();
    }

    public SELF loader(ConfigurationLoader loader) {
        return loader(PROVIDER -> loader);
    }

    public SELF loader(Consumer<ConfigurationLoader> loaderConsumer) {
        this.loaderConsumer = this.loaderConsumer.andThen(loaderConsumer);
        return self();
    }

    public SELF pathGenerator(PathGenerator pathGenerator) {
        return loader(loader -> {
            loader.setPathGenerator(pathGenerator);
        });
    }

    public SELF adapters(ValueAdapterRegistry adapters) {
        this.adapters = adapters;
        return self();
    }

    public SELF adapter(Consumer<ValueAdapterRegistry> adapterRegistryConsumer) {
        adapterRegistryConsumer.accept(adapters);
        return self();
    }

//    public SELF adapter(@NotNull ValueAdapter<?, ?> adapter) {
//        return adapter(a -> a.register(adapter));
//    }
//
//    public <T> SELF adapter(Class<T> clazz, @NotNull ValueAdapter<?, T> adapter) {
//        return adapter(a -> a.register(clazz, adapter));
//    }
//
//    public <B, V> SELF adapter(Class<B> baseClass, Class<V> valueClass,
//                               ConfigDataFunction<B, V> parser, ConfigDataFunction<V, B> serializer) {
//        return adapter(a -> a.register(baseClass, valueClass, parser, serializer));
//    }

    public SELF options(ConfigurationOptionHolder options) {
        this.options = options;
        return self();
    }

    public SELF option(Consumer<ConfigurationOptionHolder> optionsConsumer) {
        optionsConsumer.accept(options);
        return self();
    }

    public <O> SELF option(ConfigurationOption<O> option, O value) {
        return option(o -> o.set(option, value));
    }

    public abstract @NotNull PROVIDER build();

}
