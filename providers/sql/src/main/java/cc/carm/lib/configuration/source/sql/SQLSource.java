package cc.carm.lib.configuration.source.sql;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.section.ConfigureSource;
import cc.carm.lib.configuration.source.section.MemorySection;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLTable;
import cc.carm.lib.easysql.api.enums.IndexType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SQLSource extends ConfigureSource<MemorySection, Map<?, ?>, SQLSource> {

    protected static final @NotNull Gson DEFAULT_GSON = new GsonBuilder()
            .serializeNulls().disableHtmlEscaping().setPrettyPrinting()
            .create();

    protected final @NotNull Gson gson;
    protected final @NotNull SQLManager sqlManager;
    protected final @NotNull String namespace;
    protected final @NotNull SQLTable table;

    protected final @NotNull Set<String> updated = new HashSet<>();
    protected MemorySection rootSection;

    public SQLSource(@NotNull ConfigurationHolder<? extends SQLSource> holder, long lastUpdateMillis,
                     @NotNull Gson gson, @NotNull SQLManager sqlManager, @NotNull String tableName, @NotNull String namespace) {
        super(holder, lastUpdateMillis);
        this.gson = gson;
        this.sqlManager = sqlManager;
        this.namespace = namespace;
        this.table = SQLTable.of(tableName, builder -> {

            builder.addColumn("namespace", "VARCHAR(32) NOT NULL");
            builder.addColumn("path", "VARCHAR(96) NOT NULL");

            builder.addColumn("type", "TINYINT NOT NULL DEFAULT 0");
            builder.addColumn("value", "TEXT");

            builder.addColumn("inline_comment", "TEXT");
            builder.addColumn("header_comments", "MEDIUMTEXT");
            builder.addColumn("footer_comments", "MEDIUMTEXT");

            builder.addColumn("version", "MEDIUMINT UNSIGNED NOT NULL DEFAULT 0");

            builder.addColumn("create_time", "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");
            builder.addColumn("update_time", "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");

            builder.setIndex(
                    IndexType.PRIMARY_KEY, "pk_" + tableName.toLowerCase(),
                    "namespace", "path"
            );
            builder.setTableSettings("ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        });
    }

    @Override
    protected SQLSource self() {
        return this;
    }

    @Override
    public @NotNull Map<?, ?> original() {
        return section().data();
    }

    @Override
    public @NotNull MemorySection section() {
        return Objects.requireNonNull(this.rootSection, "Root section is not initialized.");
    }

    @Override
    public void save() throws Exception {
        if (this.updated.isEmpty()) return; // Nothing to save

        Date date = new Date(); // Update time
        List<Object[]> values = new ArrayList<>();

        for (String path : this.updated) {
            Object value = get(path);

//            if (value instanceof SQLConfigWrapper) {
//                value = getSourceMap(((SQLConfigWrapper) value).getSource());
//            }
//
//            SQLValueResolver<?> type = SQLValueTypes.get(value.getClass());
//            if (type != null) {
//                values.add(new Object[]{
//                        this.namespace, path, date,
//                        type.getID(), type.serializeObject(value),
//                        getComments().getInlineComment(path),
//                        GSON.toJson(getComments().getHeaderComment(path))
//                });
//            }
        }

        this.updated.clear();

        this.table.createReplaceBatch()
                .setColumnNames("namespace", "path", "update_time", "type", "value", "inline_comment", "header_comments")
                .setAllParams(values)
                .execute();
    }

    @Override
    protected void onReload() throws Exception {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();

//        try (SQLQuery query = this.table.createQuery()
//                .addCondition("namespace", namespace)
//                .build().execute()) {
//            ResultSet rs = query.getResultSet();
//            while (rs.next()) {
//                String path = rs.getString("path");
//                int type = rs.getInt("type");
//                try {
//                    SQLValueResolver<?> resolver = SQLValueTypes.get(type);
//                    if (resolver == null) throw new IllegalStateException("No resolver for type #" + type);
//                    String value = rs.getString("value");
//                    values.put(path, resolver.resolve(value));
//
//                    loadInlineComment(path, rs.getString("inline_comment"));
//                    loadHeaderComment(path, rs.getString("header_comments"));
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//
//        this.rootConfiguration = new SQLConfigWrapper(this, values);
    }

}
