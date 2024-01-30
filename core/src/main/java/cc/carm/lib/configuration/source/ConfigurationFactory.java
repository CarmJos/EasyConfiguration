package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.function.ConfigDataFunction;
import cc.carm.lib.configuration.loader.ConfigurationLoader;
import cc.carm.lib.configuration.loader.PathGenerator;
import cc.carm.lib.easyoptions.OptionHolder;
import cc.carm.lib.easyoptions.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ConfigurationFactory<P extends ConfigurationSource<P, ?>, C> {

    protected Function<P, ConfigurationLoader> loaderFunction = p -> new ConfigurationLoader();
    protected Consumer<ConfigurationLoader> loaderConsumer = loader -> {
    };

    protected ValueAdapterRegistry adapters = new ValueAdapterRegistry();
    protected OptionHolder options = new OptionHolder();

    public abstract C getThis();

    public C loader(Function<P, ConfigurationLoader> loaderFunction) {
        this.loaderFunction = loaderFunction;
        return getThis();
    }

    public C loader(ConfigurationLoader loader) {
        return loader(p -> loader);
    }

    public C loader(Consumer<ConfigurationLoader> loaderConsumer) {
        this.loaderConsumer = this.loaderConsumer.andThen(loaderConsumer);
        return getThis();
    }

    public C pathGenerator(PathGenerator pathGenerator) {
        return loader(loader -> {
            loader.setPathGenerator(pathGenerator);
        });
    }

    public C adapters(ValueAdapterRegistry adapters) {
        this.adapters = adapters;
        return getThis();
    }

    public C adapter(Consumer<ValueAdapterRegistry> adapterRegistryConsumer) {
        adapterRegistryConsumer.accept(adapters);
        return getThis();
    }

    public C adapter(@NotNull ValueAdapter<?, ?> adapter) {
        return adapter(a -> a.register(adapter));
    }

    public <T> C adapter(Class<T> clazz, @NotNull ValueAdapter<?, T> adapter) {
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
