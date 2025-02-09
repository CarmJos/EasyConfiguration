package cc.carm.lib.configuration.json;

import cc.carm.lib.configuration.source.comment.ConfigurationComments;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;

/**
 * Some code comes from BungeeCord's implementation of the JsonConfiguration.
 *
 * @author md_5, CarmJos
 */
public class JSONConfigProvider extends FileConfigProvider<JSONConfigWrapper> {



    protected JSONConfigWrapper configuration;
    protected ConfigInitializer<JSONConfigProvider> initializer;

    public JSONConfigProvider(@NotNull File file) {
        super(file);
        this.initializer = new ConfigInitializer<>(this);
    }

    public void initializeConfig() {
        onReload();
    }

    @Override
    public @NotNull JSONConfigWrapper getConfiguration() {
        return this.configuration;
    }

    @Override
    protected void onReload() {
        LinkedHashMap<?, ?> map = null;

        try (FileInputStream is = new FileInputStream(file)) {
            map = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), LinkedHashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (map == null) map = new LinkedHashMap<>();

        this.configuration = new JSONConfigWrapper(map);
    }

    @Override
    public @Nullable ConfigurationComments getComments() {
        return null;
    }

    @Override
    public void save() throws Exception {
        try (Writer writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
            gson.toJson(configuration.data, writer);
        }
    }

    @Override
    public @NotNull ConfigInitializer<? extends ConfigurationProvider<JSONConfigWrapper>> getInitializer() {
        return this.initializer;
    }

}
