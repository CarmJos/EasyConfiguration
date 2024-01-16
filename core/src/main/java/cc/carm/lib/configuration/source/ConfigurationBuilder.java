package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.source.path.PathGenerator;
import cc.carm.lib.easyoptions.OptionHolder;
import cc.carm.lib.easyoptions.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ConfigurationBuilder<P extends ConfigurationProvider<P>, C> {


    protected Function<P, ConfigurationLoader<P>> loaderFunction = ConfigurationLoader::new;
    protected Consumer<ConfigurationLoader<P>> loaderConsumer = loader -> {
    };

    protected ValueAdapterRegistry<P> adapters = new ValueAdapterRegistry<>();
    protected OptionHolder options = new OptionHolder();

    public abstract C getThis();

    public C loader(Function<P, ConfigurationLoader<P>> loaderFunction) {
        this.loaderFunction = loaderFunction;
        return getThis();
    }

    public C loader(ConfigurationLoader<P> loader) {
        return loader(p -> loader);
    }

    public C loader(Consumer<ConfigurationLoader<P>> loaderConsumer) {
        this.loaderConsumer = this.loaderConsumer.andThen(loaderConsumer);
        return getThis();
    }

    public C pathGenerator(PathGenerator<P> pathGenerator) {
        return loader(loader -> {
            loader.setPathGenerator(pathGenerator);
        });
    }

    public C adapters(ValueAdapterRegistry<P> adapters) {
        this.adapters = adapters;
        return getThis();
    }

    public C adapter(Consumer<ValueAdapterRegistry<P>> adapterRegistryConsumer) {
        adapterRegistryConsumer.accept(adapters);
        return getThis();
    }

    public C adapter(@NotNull ValueAdapter<P, ?, ?> adapter) {
        return adapter(a -> a.register(adapter));
    }

    public <T> C adapter(Class<T> clazz, @NotNull ValueAdapter<P, ?, T> adapter) {
        return adapter(a -> a.register(clazz, adapter));
    }

    public <B, V> C adapter(Class<B> baseClass, Class<V> valueClass,
                            ConfigDataFunction<B, V> parser, ConfigDataFunction<V, B> serializer) {
        return adapter(a -> a.register(baseClass, valueClass, parser, serializer));
    }

    public C options(OptionHolder options) {
        this.options = options;
        return getThis();
    }

    public C option(Consumer<OptionHolder> optionsConsumer) {
        optionsConsumer.accept(options);
        return getThis();
    }

    public <O> C option(OptionType<O> option, O value) {
        return option(o -> o.set(option, value));
    }

    public abstract @NotNull P build();

}
