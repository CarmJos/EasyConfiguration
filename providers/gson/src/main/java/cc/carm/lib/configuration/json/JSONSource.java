package cc.carm.lib.configuration.json;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.FileConfigSource;
import cc.carm.lib.configuration.source.section.ConfigureSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

public class JSONSource extends FileConfigSource<JSONSection, Map<?, ?>, JSONSource> {

    public static final @NotNull Gson DEFAULT_GSON = new GsonBuilder()
            .serializeNulls().disableHtmlEscaping().setPrettyPrinting()
            .registerTypeAdapter(
                    JSONSection.class,
                    (JsonSerializer<JSONSection>) (src, typeOfSrc, context) -> context.serialize(src.data)
            ).create();

    protected final @NotNull Gson gson;
    protected @Nullable JSONSection section;

    protected JSONSource(@NotNull ConfigurationHolder<? extends JSONSource> holder, long lastUpdateMillis,
                         @NotNull File file, @Nullable String resourcePath) {
        this(holder, lastUpdateMillis, file, resourcePath, DEFAULT_GSON);
    }

    protected JSONSource(@NotNull ConfigurationHolder<? extends JSONSource> holder, long lastUpdateMillis,
                         @NotNull File file, @Nullable String resourcePath, @NotNull Gson gson) {
        super(holder, lastUpdateMillis, file, resourcePath);
        this.gson = gson;
        initialize();
    }

    public void initialize() {
        try {
            initializeFile();
            onReload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONSource self() {
        return this;
    }

    @Override
    public @NotNull ConfigureSource<?, ?, ?> source() {
        return this;
    }

    @Override
    public @NotNull Map<?, ?> original() {
        return section().data();
    }

    @Override
    public @NotNull JSONSection section() {
        return Objects.requireNonNull(this.section, "Section is not initialized");
    }

    @Override
    public void save() throws Exception {
        fileWriter(writer -> gson.toJson(original(), writer));
    }

    @Override
    protected void onReload() throws Exception {
        Map<?, ?> data = fileReader(reader -> gson.fromJson(reader, LinkedHashMap.class));
        if (data == null) data = new LinkedHashMap<>();
        this.section = new JSONSection(this, data);
        this.lastUpdateMillis = System.currentTimeMillis(); // 更新时间
    }
}
