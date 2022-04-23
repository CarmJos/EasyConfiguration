package cc.carm.lib.configuration.yaml;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class YAMLConfigProvider extends FileConfigProvider<YAMLSectionWrapper> {

    protected static final char SEPARATOR = '.';

    protected final @NotNull YAMLComments comments = new YAMLComments();
    protected YamlConfiguration configuration;
    protected ConfigInitializer<YAMLConfigProvider> initializer;

    public YAMLConfigProvider(@NotNull File file) {
        super(file);
    }

    public void initializeConfig() {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.initializer = new ConfigInitializer<>(this);
    }

    @Override
    public @NotNull YAMLSectionWrapper getConfiguration() {
        return YAMLSectionWrapper.of(configuration);
    }

    @Override
    public void reload() throws Exception {
        configuration.load(getFile());
    }

    @Override
    public void save() throws Exception {
        configuration.save(getFile());


        // tchristofferson/ConfigUpdater start
        StringWriter writer = new StringWriter();
        this.comments.writeComments(configuration, new BufferedWriter(writer));
        String value = writer.toString(); // config contents

        Path toUpdatePath = getFile().toPath();
        if (!value.equals(new String(Files.readAllBytes(toUpdatePath), StandardCharsets.UTF_8))) {
            // if updated contents are not the same as current file contents, update
            Files.write(toUpdatePath, value.getBytes(StandardCharsets.UTF_8));
        }
        // tchristofferson/ConfigUpdater end

    }

    @Override
    public void setComment(@Nullable String path, @Nullable ConfigCommentInfo comments) {
        this.comments.set(path, comments);
    }

    @Override
    public @Nullable ConfigCommentInfo getComment(@Nullable String path) {
        return this.comments.get(path);
    }

    @Override
    public @NotNull ConfigInitializer<YAMLConfigProvider> getInitializer() {
        return this.initializer;
    }

}
