package cc.carm.lib.configuration.source.hocon;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.section.ConfigureSource;
import com.typesafe.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HOCONSource extends ConfigureSource<HOCONSection, Config, HOCONSource> {


    private HOCONSection rootSection;

    protected HOCONSource(@NotNull ConfigurationHolder<? extends HOCONSource> holder, long lastUpdateMillis) {
        super(holder, lastUpdateMillis);
    }

    @Override
    protected HOCONSource self() {
        return this;
    }

    @Override
    public @NotNull Config original() {
        return section().data();
    }

    @Override
    public @NotNull HOCONSection section() {
        return Objects.requireNonNull(rootSection, "RootSection is not initialized");
    }

    @Override
    public void save() throws Exception {

    }

    @Override
    protected void onReload() throws Exception {

    }

}
