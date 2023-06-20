package cc.carm.lib.configuration.hocon.source;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * 暂时未实现，原因是如果要实现，就需要修改部分代码
 * @see ConfigurationProvider#save()
 * @see ConfigurationProvider#reload()
 * 等一系列代码
 */
public abstract class StringConfigProvider<W extends ConfigurationWrapper<?>> extends ConfigurationProvider<W> {
    protected final @NotNull String source;

    protected StringConfigProvider(@NotNull String source) {
        this.source = source;
    }

    public @NotNull String getSource() {
        return this.source;
    }
}
