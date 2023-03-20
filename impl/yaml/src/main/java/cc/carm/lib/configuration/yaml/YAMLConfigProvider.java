package cc.carm.lib.configuration.yaml;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigurationComments;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import cc.carm.lib.yamlcommentupdater.CommentedYAML;
import cc.carm.lib.yamlcommentupdater.CommentedYAMLWriter;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.file.FileConfiguration;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Set;

public class YAMLConfigProvider extends FileConfigProvider<YAMLSectionWrapper> implements CommentedYAML {

    protected final @NotNull ConfigurationComments comments = new ConfigurationComments();
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
    protected void onReload() throws Exception {
        configuration.load(getFile());
    }

    @Override
    public @NotNull ConfigurationComments getComments() {
        return this.comments;
    }

    @Override
    public void save() throws Exception {
        try {
            CommentedYAMLWriter.writeWithComments(this, this.file);
        } catch (Exception ex) {
            configuration.save(file);
            throw ex;
        }
    }

    @Override
    public @NotNull ConfigInitializer<YAMLConfigProvider> getInitializer() {
        return this.initializer;
    }

    @Override
    public String serializeValue(@NotNull String key, @NotNull Object value) {
        FileConfiguration temp = new YamlConfiguration();
        temp.set(key, value);
        return temp.saveToString();
    }

    @Override
    public Set<String> getKeys(@Nullable String sectionKey, boolean deep) {
        if (sectionKey == null) return configuration.getKeys(deep);

        ConfigurationSection section = configuration.getConfigurationSection(sectionKey);
        if (section == null) return null;

        return section.getKeys(deep);
    }

    @Override
    public @Nullable Object getValue(@NotNull String key) {
        return configuration.get(key);
    }

    @Override
    public @Nullable List<String> getHeaderComments(@Nullable String key) {
        return comments.getHeaderComment(key);
    }

}
