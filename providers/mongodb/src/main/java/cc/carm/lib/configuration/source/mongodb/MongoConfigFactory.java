package cc.carm.lib.configuration.source.mongodb;

import cc.carm.lib.configuration.source.ConfigurationFactory;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MongoConfigFactory extends ConfigurationFactory<MongoSource, ConfigurationHolder<MongoSource>, MongoConfigFactory> {

    public static MongoConfigFactory from(@NotNull Supplier<MongoCollection<Document>> collectionSupplier) {
        return new MongoConfigFactory(collectionSupplier);
    }

    public static MongoConfigFactory from(@NotNull MongoCollection<Document> collection) {
        return from(() -> collection);
    }

    public static MongoConfigFactory from(@NotNull MongoDatabase database, @NotNull String collectionName) {
        return from(() -> database.getCollection(collectionName));
    }

    protected @NotNull Supplier<MongoCollection<Document>> collectionSupplier;
    protected @NotNull String namespace = "config";

    public MongoConfigFactory(@NotNull Supplier<MongoCollection<Document>> collectionSupplier) {
        super();
        this.collectionSupplier = collectionSupplier;
    }

    public MongoConfigFactory collection(@NotNull Supplier<MongoCollection<Document>> collectionSupplier) {
        this.collectionSupplier = collectionSupplier;
        return this;
    }

    public MongoConfigFactory collection(@NotNull MongoCollection<Document> collection) {
        return collection(() -> collection);
    }

    public MongoConfigFactory namespace(@NotNull String namespace) {
        this.namespace = namespace;
        return this;
    }

    public MongoConfigFactory namespace(@NotNull Supplier<String> namespace) {
        return namespace(namespace.get());
    }


    @Override
    protected MongoConfigFactory self() {
        return this;
    }

    @Override
    public @NotNull ConfigurationHolder<MongoSource> build() {
        MongoCollection<Document> collection = this.collectionSupplier.get();
        if (collection == null) {
            throw new IllegalStateException("Failed to get MongoCollection<Document> from supplier");
        }

        return new ConfigurationHolder<MongoSource>(this.adapters, this.options, this.metadata, this.initializer) {
            final @NotNull MongoSource source = new MongoSource(this, System.currentTimeMillis(), collection, namespace);

            @Override
            public @NotNull MongoSource config() {
                return this.source;
            }
        };
    }

}
