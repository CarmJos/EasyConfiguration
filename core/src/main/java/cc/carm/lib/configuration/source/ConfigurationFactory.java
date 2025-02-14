package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.adapter.*;
import cc.carm.lib.configuration.adapter.strandard.StandardAdapters;
import cc.carm.lib.configuration.function.DataFunction;
import cc.carm.lib.configuration.source.loader.ConfigurationInitializer;
import cc.carm.lib.configuration.source.loader.PathGenerator;
import cc.carm.lib.configuration.source.meta.ConfigurationMetaHolder;
import cc.carm.lib.configuration.source.meta.ConfigurationMetadata;
import cc.carm.lib.configuration.source.option.ConfigurationOption;
import cc.carm.lib.configuration.source.option.ConfigurationOptionHolder;
import cc.carm.lib.configuration.source.section.ConfigureSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ConfigurationFactory<
        SOURCE extends ConfigureSource<?, ?, SOURCE>,
        HOLDER extends ConfigurationHolder<SOURCE>,
        SELF
        > {

    protected ValueAdapterRegistry adapters = new ValueAdapterRegistry();
    protected ConfigurationOptionHolder options = new ConfigurationOptionHolder();
    protected @NotNull Map<String, ConfigurationMetaHolder> metadata = new HashMap<>();
    protected ConfigurationInitializer initializer = new ConfigurationInitializer();

    public ConfigurationFactory() {
        this.adapters.register(StandardAdapters.PRIMITIVES);
        this.adapters.register(StandardAdapters.SECTIONS);
        this.adapters.register(StandardAdapters.ENUMS);
    }

    protected abstract SELF self();

    public SELF adapters(ValueAdapterRegistry adapters) {
        this.adapters = adapters;
        return self();
    }

    public SELF adapter(Consumer<ValueAdapterRegistry> adapterRegistryConsumer) {
        adapterRegistryConsumer.accept(adapters);
        return self();
    }

    public <T> SELF adapter(@NotNull ValueAdapter<T> adapter) {
        return adapter(a -> a.register(adapter));
    }

    public <T> SELF adapter(@NotNull ValueType<T> type, @NotNull ValueSerializer<T> serializer) {
        return adapter(a -> a.register(type, serializer));
    }

    public <T> SELF adapter(@NotNull ValueType<T> type, @NotNull ValueParser<T> parser) {
        return adapter(a -> a.register(type, parser));
    }

    public <FROM, TO> SELF adapter(@NotNull Class<FROM> from, @NotNull Class<TO> to,
                                   @NotNull DataFunction<FROM, TO> parser,
                                   @NotNull DataFunction<TO, FROM> serializer) {
        return adapter(a -> a.register(from, to, parser, serializer));
    }

    public <FROM, TO> SELF adapter(@NotNull ValueType<FROM> from, @NotNull ValueType<TO> to,
                                   @NotNull DataFunction<FROM, TO> parser,
                                   @NotNull DataFunction<TO, FROM> serializer) {
        return adapter(a -> a.register(from, to, parser, serializer));
    }

    public <T> SELF adapter(@NotNull ValueType<T> type, @NotNull ValueSerializer<T> serializer, @NotNull ValueParser<T> parser) {
        return adapter(a -> a.register(type, serializer, parser));
    }

    public <T> SELF adapter(@NotNull Class<T> type, @NotNull ValueSerializer<T> serializer, @NotNull ValueParser<T> parser) {
        return adapter(ValueType.of(type), serializer, parser);
    }

    public SELF options(ConfigurationOptionHolder options) {
        this.options = options;
        return self();
    }

    public SELF option(Consumer<ConfigurationOptionHolder> modifier) {
        modifier.accept(options);
        return self();
    }

    public <O> SELF option(ConfigurationOption<O> type, O value) {
        return option(o -> o.set(type, value));
    }

    public <O> SELF option(ConfigurationOption<O> type, Supplier<O> value) {
        return option(type, value.get());
    }

    public <O> SELF option(ConfigurationOption<O> type, Consumer<O> modifier) {
        return option(holder -> {
            O current = holder.get(type);
            modifier.accept(current);
            holder.set(type, current);
        });
    }

    public SELF metadata(@NotNull Map<String, ConfigurationMetaHolder> metadata) {
        this.metadata = metadata;
        return self();
    }

    public SELF metadata(@NotNull Consumer<Map<String, ConfigurationMetaHolder>> handler) {
        handler.accept(this.metadata);
        return self();
    }

    public SELF metadata(@Nullable String path, @NotNull ConfigurationMetaHolder meta) {
        return metadata(m -> m.put(path, meta));
    }

    public SELF metadata(@Nullable String path, @NotNull Consumer<ConfigurationMetaHolder> handler) {
        return metadata(map -> {
            ConfigurationMetaHolder meta = map.computeIfAbsent(path, k -> new ConfigurationMetaHolder());
            handler.accept(meta);
        });
    }


    public SELF initializer(ConfigurationInitializer initializer) {
        this.initializer = initializer;
        return self();
    }

    public SELF initializer(Consumer<ConfigurationInitializer> initializerConsumer) {
        initializerConsumer.accept(initializer);
        return self();
    }

    public SELF pathGenerator(PathGenerator generator) {
        return initializer(loader -> {
            loader.pathGenerator(generator);
        });
    }

    public <M, A extends Annotation> SELF metaAnnotation(@NotNull Class<A> annotation,
                                                         @NotNull ConfigurationMetadata<M> metadata,
                                                         @NotNull Function<A, M> extractor) {
        return initializer(loader -> loader.registerAnnotation(annotation, metadata, extractor));
    }

    public abstract @NotNull HOLDER build();

}
