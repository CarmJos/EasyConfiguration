package cc.carm.lib.configuration.sql;

import cc.carm.lib.configuration.source.comment.ConfigurationComments;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.SQLTable;
import cc.carm.lib.easysql.api.enums.IndexType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.*;

public class SQLConfigProvider extends ConfigurationProvider<SQLConfigWrapper> {

    public static Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();

    protected final @NotNull SQLManager sqlManager;
    protected final @NotNull SQLTable table;
    protected final @NotNull String namespace;

    protected ConfigInitializer<SQLConfigProvider> initializer;
    protected ConfigurationComments comments = new ConfigurationComments();
    protected SQLConfigWrapper rootConfiguration;

    protected final @NotNull Set<String> updated = new HashSet<>();

    public SQLConfigProvider(@NotNull SQLManager sqlManager, @NotNull String tableName, @NotNull String namespace) {
        this.sqlManager = sqlManager;
        this.table = SQLTable.of(tableName, builder -> {

            builder.addColumn("namespace", "VARCHAR(32) NOT NULL");
            builder.addColumn("path", "VARCHAR(96) NOT NULL");

            builder.addColumn("type", "TINYINT NOT NULL DEFAULT 0");
            builder.addColumn("value", "TEXT");

            builder.addColumn("inline_comment", "TEXT");
            builder.addColumn("header_comments", "MEDIUMTEXT");

            builder.addColumn("create_time", "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");
            builder.addColumn("update_time", "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");

            builder.setIndex(
                    IndexType.PRIMARY_KEY, "pk_" + tableName.toLowerCase(),
                    "namespace", "path"
            );
            builder.setTableSettings("ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        });
        this.namespace = namespace;
    }

    @Override
    public @NotNull SQLConfigWrapper getConfiguration() {
        return rootConfiguration;
    }

    public void initializeConfig() throws Exception {
        this.table.create(this.sqlManager);
        this.initializer = new ConfigInitializer<>(this);
        onReload();
    }

    @Override
    protected void onReload() throws Exception {
        this.comments = new ConfigurationComments();
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();

        try (SQLQuery query = this.table.createQuery()
                .addCondition("namespace", namespace)
                .build().execute()) {
            ResultSet rs = query.getResultSet();
            while (rs.next()) {
                String path = rs.getString("path");
                int type = rs.getInt("type");
                try {
                    SQLValueResolver<?> resolver = SQLValueTypes.get(type);
                    if (resolver == null) throw new IllegalStateException("No resolver for type #" + type);
                    String value = rs.getString("value");
                    values.put(path, resolver.resolve(value));

                    loadInlineComment(path, rs.getString("inline_comment"));
                    loadHeaderComment(path, rs.getString("header_comments"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        this.rootConfiguration = new SQLConfigWrapper(this, values);
    }

    @Override
    public void save() throws Exception {
        if (this.updated.isEmpty()) return;

        Date date = new Date();
        List<Object[]> values = new ArrayList<>();
        List<String> deletes = new ArrayList<>();

        for (String path : this.updated) {
            Object value = this.rootConfiguration.get(path);
            if (value == null) {
                deletes.add(path);
                continue;
            }

            if (value instanceof SQLConfigWrapper) {
                value = getSourceMap(((SQLConfigWrapper) value).getSource());
            }

            SQLValueResolver<?> type = SQLValueTypes.get(value.getClass());
            if (type != null) {
                values.add(new Object[]{
                        this.namespace, path, date,
                        type.getID(), type.serializeObject(value),
                        getComments().getInlineComment(path),
                        GSON.toJson(getComments().getHeaderComment(path))
                });
            }
        }

        this.updated.clear();

        this.table.createReplaceBatch()
                .setColumnNames("namespace", "path", "update_time", "type", "value", "inline_comment", "header_comments")
                .setAllParams(values)
                .execute();

        for (String path : deletes) {
            this.table.createDelete()
                    .addCondition("namespace", this.namespace)
                    .addCondition("path", path)
                    .build().execute();
        }
    }


    @Override
    public @NotNull ConfigurationComments getComments() {
        return this.comments;
    }

    @Override
    public @NotNull ConfigInitializer<? extends ConfigurationProvider<SQLConfigWrapper>> getInitializer() {
        return this.initializer;
    }


    protected void loadInlineComment(String path, String comment) {
        if (comment == null) return;
        comment = comment.trim();
        if (comment.isEmpty()) return;

        this.comments.setInlineComment(path, comment);
    }

    protected void loadHeaderComment(String path, String commentJson) {
        if (commentJson == null) return;
        commentJson = commentJson.trim();
        if (commentJson.isEmpty()) return;

        List<String> headerComments = GSON.fromJson(commentJson, new TypeToken<List<String>>() {
        }.getType());
        if (headerComments == null || headerComments.isEmpty()) return;

        this.comments.setHeaderComments(path, headerComments);
    }

    protected static Map<String, Object> getSourceMap(Map<String, Object> map) {
        Map<String, Object> source = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof SQLConfigWrapper) {
                source.put(entry.getKey(), getSourceMap(((SQLConfigWrapper) entry.getValue()).getSource()));
            } else {
                source.put(entry.getKey(), entry.getValue());
            }
        }
        return source;
    }

}
