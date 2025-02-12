package cc.carm.lib.configuration.source.json;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.file.FileConfigFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class JSONConfigFactory extends FileConfigFactory<JSONSource, ConfigurationHolder<JSONSource>, JSONConfigFactory> {

    public static JSONConfigFactory from(@NotNull File file) {
        return new JSONConfigFactory(file);
    }

    public static JSONConfigFactory from(@NotNull File parent, @NotNull String configName) {
        return new JSONConfigFactory(new File(parent, configName));
    }

    protected Supplier<Gson> gsonSupplier = () -> JSONSource.DEFAULT_GSON;

    public JSONConfigFactory(@NotNull File file) {
        super(file);
    }

    @Override
    protected JSONConfigFactory self() {
        return this;
    }

    public JSONConfigFactory gson(@NotNull Supplier<Gson> gsonSupplier) {
        this.gsonSupplier = gsonSupplier;
        return self();
    }

    public JSONConfigFactory gson(@NotNull Consumer<GsonBuilder> builder) {
        return gson(() -> {
            GsonBuilder gsonBuilder = new GsonBuilder();
            builder.accept(gsonBuilder);
            return gsonBuilder.create();
        });
    }

    public JSONConfigFactory gson(@NotNull Gson gson) {
        return gson(() -> gson);
    }

    @Override
    public @NotNull ConfigurationHolder<JSONSource> build() {
        Gson gson = gsonSupplier.get();
        if (gson == null) throw new NullPointerException("No Gson instance provided.");

        File configFile = this.file;
        String sourcePath = this.resourcePath;

        return new ConfigurationHolder<JSONSource>(this.adapters, this.options, new HashMap<>(), this.initializer) {
            final JSONSource source = new JSONSource(this, configFile, sourcePath, gson);

            @Override
            public @NotNull JSONSource config() {
                return source;
            }
        };
    }


}
