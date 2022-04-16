package cc.carm.lib.configuration.commented;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

/**
 * A yaml file with comments on certain properties, as returned by the given {@link CommentsProvider}.
 * Unlike {@link YamlConfiguration}, this class does not provide a header support.
 */
public class CommentedYamlConfiguration extends YamlConfiguration {

    private final DumperOptions yamlOptions = new DumperOptions();
    private final Representer yamlRepresenter = new YamlRepresenter();
    private final CommentedYaml yaml;

    public CommentedYamlConfiguration(CommentsProvider commentsProvider) {
        this.yaml = new CommentedYaml(new YamlConstructor(), this.yamlRepresenter, this.yamlOptions, commentsProvider);
    }

    public static CommentedYamlConfiguration loadConfiguration(CommentsProvider commentsProvider, File file) {
        CommentedYamlConfiguration config = new CommentedYamlConfiguration(commentsProvider);

        try {
            config.load(file);
        } catch (FileNotFoundException ignored) {
        } catch (IOException | InvalidConfigurationException var4) {
            var4.printStackTrace();
        }

        return config;
    }

    public static CommentedYamlConfiguration loadConfiguration(CommentsProvider commentsProvider, Reader reader) {
        CommentedYamlConfiguration config = new CommentedYamlConfiguration(commentsProvider);

        try {
            config.load(reader);
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }

        return config;
    }

    @Override
    public @NotNull String saveToString() {
        this.yamlOptions.setIndent(this.options().indent());
        this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        String dump = this.yaml.dump(this.getValues(false));
        if (dump.equals("{}\n")) {
            dump = "";
        }

        // No header support.

        return dump;
    }
}