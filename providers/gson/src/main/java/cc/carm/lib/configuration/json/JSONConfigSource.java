package cc.carm.lib.configuration.json;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.FileConfigSource;
import cc.carm.lib.configuration.source.section.ConfigurationSection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JSONConfigSource extends FileConfigSource<JSONConfigSource, JsonObject> {

    public static final @NotNull Gson DEFAULT_GSON = new GsonBuilder()
            .serializeNulls().disableHtmlEscaping().setPrettyPrinting()
            .registerTypeAdapter(
                    JSONConfigWrapper.class,
                    (JsonSerializer<JSONConfigWrapper>) (src, typeOfSrc, context) -> context.serialize(src.data)
            ).create();

    protected final @NotNull Gson gson;

    protected @Nullable JsonObject original;

    protected JSONConfigSource(@NotNull ConfigurationHolder<? extends JSONConfigSource> holder, long lastUpdateMillis,
                               @NotNull File file, @Nullable String resourcePath, @NotNull Gson gson) {
        super(holder, lastUpdateMillis, file, resourcePath);
        this.gson = gson;
    }

    public void initialize() {
        try {
            initializeFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeJson() {
        this.original = fileReader(reader -> gson.fromJson(reader, JsonObject.class));

    }

    @Override
    protected JSONConfigSource self() {
        return this;
    }

    @Override
    public @NotNull JsonObject original() {
        return null;
    }

    @Override
    public void save() throws Exception {

    }

    @Override
    protected void onReload() throws Exception {

    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return Collections.emptyMap();
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {

    }

    @Override
    public boolean contains(@NotNull String path) {
        return false;
    }

    @Override
    public boolean isList(@NotNull String path) {
        return false;
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        return Collections.emptyList();
    }

    @Override
    public boolean isSection(@NotNull String path) {
        return false;
    }

    @Override
    public @Nullable ConfigurationSection getSection(@NotNull String path) {
        return null;
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return null;
    }
}
