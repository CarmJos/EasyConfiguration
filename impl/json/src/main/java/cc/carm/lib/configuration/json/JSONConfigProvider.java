package cc.carm.lib.configuration.json;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

/**
 * Some code comes from BungeeCord's implementation of the JsonConfiguration.
 *
 * @author md_5, CarmJos
 */
public class JSONConfigProvider extends FileConfigProvider<JSONConfigWrapper> {

    protected final Gson gson = new GsonBuilder()
            .serializeNulls().disableHtmlEscaping().setPrettyPrinting()
            .registerTypeAdapter(
                    JSONConfigWrapper.class,
                    (JsonSerializer<JSONConfigWrapper>) (src, typeOfSrc, context) -> context.serialize(src.data)
            ).create();

    protected JSONConfigWrapper configuration;
    protected ConfigInitializer<JSONConfigProvider> initializer;

    public JSONConfigProvider(@NotNull File file) {
        super(file);
        this.initializer = new ConfigInitializer<>(this);
    }

    public void initializeConfig() {
        LinkedHashMap<?, ?> map = null;

        try (FileInputStream is = new FileInputStream(file)) {
            map = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), LinkedHashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (map == null) map = new LinkedHashMap<>();

        this.configuration = new JSONConfigWrapper(map);
        this.initializer = new ConfigInitializer<>(this);
    }

    @Override
    public @NotNull JSONConfigWrapper getConfiguration() {
        return this.configuration;
    }

    @Override
    public void reload() {
        initializeConfig();
    }

    @Override
    public void save() throws Exception {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(configuration.data, writer);
        }
    }

    @Override
    public void setComment(@Nullable String path, @Nullable ConfigCommentInfo comment) {
        // JSON doesn't support comments;
    }

    @Override
    public @Nullable ConfigCommentInfo getComment(@Nullable String path) {
        return null;
    }


    @Override
    public @NotNull ConfigInitializer<? extends ConfigurationProvider<JSONConfigWrapper>> getInitializer() {
        return this.initializer;
    }

}
