package cc.carm.lib.configuration.hocon;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigurationComments;
import cc.carm.lib.configuration.core.source.impl.FileConfigProvider;
import cc.carm.lib.configuration.hocon.exception.HOCONGetValueException;
import cc.carm.lib.configuration.hocon.util.HOCONUtils;
import com.typesafe.config.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

public class HOCONFileConfigProvider extends FileConfigProvider<HOCONConfigWrapper> {
    protected final @NotNull ConfigurationComments comments = new ConfigurationComments();
    protected HOCONConfigWrapper configuration;
    protected ConfigInitializer<HOCONFileConfigProvider> initializer;

    public HOCONFileConfigProvider(@NotNull File file) {
        super(file);
        this.initializer = new ConfigInitializer<>(this);
    }

    public void initializeConfig() {
        try {
            this.configuration = new HOCONConfigWrapper(ConfigFactory.parseFile(this.file, ConfigParseOptions.defaults()
                    .setSyntax(ConfigSyntax.CONF)
                    .setAllowMissing(false)).root());
        } catch (ConfigException e) {
            e.printStackTrace();
        }
    }

    @Override
    public @NotNull HOCONConfigWrapper getConfiguration() {
        return this.configuration;
    }

    @Override
    public void save() throws IOException {
        Files.write(this.file.toPath(), HOCONUtils.renderWithComment(configuration, comments::getHeaderComment).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void onReload() throws ConfigException {
        ConfigObject conf = ConfigFactory.parseFile(this.file, ConfigParseOptions.defaults()
                .setSyntax(ConfigSyntax.CONF)
                .setAllowMissing(false)).root();
        this.configuration = new HOCONConfigWrapper(conf);
    }

    @Override
    public @NotNull ConfigurationComments getComments() {
        return this.comments;
    }

    @Override
    public @NotNull ConfigInitializer<HOCONFileConfigProvider> getInitializer() {
        return this.initializer;
    }

    public String serializeValue(@NotNull String key, @NotNull Object value) {
        // 带有 key=value 的新空对象
        return ConfigFactory.empty()
                .withValue(key, ConfigValueFactory.fromAnyRef(value))
                .root().render();
    }

    public @NotNull Set<String> getKeys() {
        return getKeys(null, true);
    }

    @Contract("null,_->!null")
    public @Nullable Set<String> getKeys(@Nullable String sectionKey, boolean deep) {
        if (sectionKey == null) { // 当前路径
            return HOCONUtils.getKeysFromObject(this.configuration, deep, "");
        }

        HOCONConfigWrapper section;
        try {
            // 获取目标字段所在路径
            section = (HOCONConfigWrapper) this.configuration.get(sectionKey);
        } catch (ClassCastException e) {
            // 值和类型不匹配
            throw new HOCONGetValueException(e);
        }
        if (section == null) {
            return null;
        }
        return HOCONUtils.getKeysFromObject(section, deep, "");
    }

    public @Nullable Object getValue(@NotNull String key) {
        return this.configuration.get(key);
    }

    public @Nullable List<String> getHeaderComments(@Nullable String key) {
        return this.comments.getHeaderComment(key);
    }
}
