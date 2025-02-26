package cc.carm.lib.configuration.source.mongodb;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.section.ConfigureSource;
import cc.carm.lib.configuration.source.section.SourcedSection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class MongoSource extends ConfigureSource<SourcedSection, Map<String, Object>, MongoSource> {

    protected final @NotNull MongoCollection<Document> collection;
    protected final @NotNull String namespace;

    protected SourcedSection rootSection;

    protected MongoSource(@NotNull ConfigurationHolder<? extends MongoSource> holder, long lastUpdateMillis,
                          @NotNull MongoCollection<Document> collection, @NotNull String namespace) {
        super(holder, lastUpdateMillis);
        this.collection = collection;
        this.namespace = namespace;
        try {
            onReload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected @NotNull MongoSource self() {
        return this;
    }

    @Override
    public @NotNull Map<String, Object> original() {
        return section().data();
    }

    @Override
    public @NotNull SourcedSection section() {
        return Objects.requireNonNull(rootSection, "RootSection is not initialized");
    }

    public @NotNull String namespace() {
        return this.namespace;
    }

    public @NotNull MongoCollection<Document> collection() {
        return this.collection;
    }

    @Override
    public void save() throws Exception {
        Map<String, Object> data = this.rootSection.rawMap();
        if (data.isEmpty()) return; // Skip saving if empty
        if (data.containsKey("_id") && data.size() == 1) return; // Skip saving if only contains _id

        ReplaceOptions options = new ReplaceOptions().upsert(true);
        Document storage = new Document(data).append("_id", this.namespace);
        this.collection.replaceOne(new Document("_id", this.namespace), storage, options);
    }

    @Override
    protected void onReload() throws Exception {
        Document storage = this.collection.find(new Document("_id", this.namespace)).first();
        if (storage == null) storage = new Document();
        else storage.remove("_id"); // Remove _id
        this.rootSection = SourcedSection.root(this, storage);
    }


}
