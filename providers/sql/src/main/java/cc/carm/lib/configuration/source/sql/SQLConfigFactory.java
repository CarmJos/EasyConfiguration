package cc.carm.lib.configuration.source.sql;

import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.commentable.Commentable;
import cc.carm.lib.configuration.function.DataFunction;
import cc.carm.lib.configuration.source.ConfigurationFactory;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.easysql.api.SQLManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SQLConfigFactory extends ConfigurationFactory<SQLSource, ConfigurationHolder<SQLSource>, SQLConfigFactory> {

    public static SQLConfigFactory from(@NotNull Supplier<@NotNull SQLManager> managerSupplier) {
        return new SQLConfigFactory(managerSupplier);
    }

    public static SQLConfigFactory from(@NotNull SQLManager manager) {
        return from(() -> manager);
    }

    protected @NotNull Supplier<SQLManager> managerSupplier;
    protected Supplier<Gson> gsonSupplier = () -> SQLSource.DEFAULT_GSON;

    protected HashMap<Integer, SQLValueResolver<?>> resolvers = new HashMap<>(SQLValueResolver.STANDARD_RESOLVERS);
    protected String tableName = "configs";
    protected String namespace = "default";

    public SQLConfigFactory(@NotNull Supplier<SQLManager> managerSupplier) {
        this.managerSupplier = managerSupplier;
    }

    @Override
    protected SQLConfigFactory self() {
        return this;
    }

    public SQLConfigFactory manager(@NotNull Supplier<SQLManager> managerSupplier) {
        this.managerSupplier = managerSupplier;
        return self();
    }

    public SQLConfigFactory manager(@NotNull SQLManager manager) {
        return manager(() -> manager);
    }

    public SQLConfigFactory gson(@NotNull Supplier<Gson> gsonSupplier) {
        this.gsonSupplier = gsonSupplier;
        return self();
    }

    public SQLConfigFactory gson(@NotNull Consumer<GsonBuilder> builder) {
        return gson(() -> {
            GsonBuilder gsonBuilder = new GsonBuilder();
            builder.accept(gsonBuilder);
            return gsonBuilder.create();
        });
    }

    public SQLConfigFactory gson(@NotNull Gson gson) {
        return gson(() -> gson);
    }

    public SQLConfigFactory resolver(@Range(from = 0, to = 255) int type, @NotNull SQLValueResolver<?> resolver) {
        this.resolvers.put(type, resolver);
        return self();
    }

    public <T> SQLConfigFactory resolver(@Range(from = 0, to = 255) int id, @NotNull Class<T> clazz,
                                         @NotNull DataFunction<String, T> parser) {
        return resolver(id, ValueType.of(clazz), parser);
    }

    public <T> SQLConfigFactory resolver(@Range(from = 0, to = 255) int id, @NotNull ValueType<T> type,
                                         @NotNull DataFunction<String, T> parser) {
        return resolver(id, new SQLValueResolver<T>(type) {
            @Override
            public @NotNull T resolve(@NotNull ConfigurationHolder<? extends SQLSource> holder, String data) throws Exception {
                return parser.handle(data);
            }
        });
    }

    public <T> SQLConfigFactory resolver(@Range(from = 0, to = 255) int id, @NotNull Class<T> clazz,
                                         @NotNull DataFunction<String, T> parser,
                                         @NotNull DataFunction<Object, String> serializer) {
        return resolver(id, ValueType.of(clazz), parser, serializer);
    }

    public <T> SQLConfigFactory resolver(@Range(from = 0, to = 255) int id, @NotNull ValueType<T> type,
                                         @NotNull DataFunction<String, T> parser,
                                         @NotNull DataFunction<Object, String> serializer) {
        return resolver(id, new SQLValueResolver<T>(type) {
            @Override
            public @NotNull T resolve(@NotNull ConfigurationHolder<? extends SQLSource> holder, String data) throws Exception {
                return parser.handle(data);
            }

            @Override
            public @NotNull String serialize(@NotNull ConfigurationHolder<? extends SQLSource> holder, Object value) throws Exception {
                return serializer.handle(value);
            }
        });
    }

    public SQLConfigFactory tableName(@NotNull String tableName) {
        this.tableName = tableName;
        return self();
    }

    public SQLConfigFactory namespace(@NotNull String namespace) {
        this.namespace = namespace;
        return self();
    }

    @Override
    public @NotNull ConfigurationHolder<SQLSource> build() {
        Gson gson = gsonSupplier.get();
        if (gson == null) throw new NullPointerException("No Gson instance provided.");

        SQLManager manager = this.managerSupplier.get();
        if (manager == null) throw new NullPointerException("No SQLManager instance provided.");

        Commentable.registerMeta(this.initializer);

        return new ConfigurationHolder<SQLSource>(this.adapters, this.options, this.metadata, this.initializer) {
            final SQLSource source = new SQLSource(
                    this, System.currentTimeMillis(),
                    gson, manager, resolvers, tableName, namespace
            );

            @Override
            public @NotNull SQLSource config() {
                return source;
            }
        };
    }


}
