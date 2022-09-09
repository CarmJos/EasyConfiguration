package cc.carm.lib.configuration.sql;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigurationComments;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SQLConfigProvider extends ConfigurationProvider<SQLSectionWrapper> {


    @Override
    public @NotNull SQLSectionWrapper getConfiguration() {
        return null;
    }

    @Override
    public void save() throws Exception {

    }

    @Override
    protected void onReload() throws Exception {

    }

    @Override
    public @Nullable ConfigurationComments getComments() {
        return null;
    }

    @Override
    public @NotNull ConfigInitializer<? extends ConfigurationProvider<SQLSectionWrapper>> getInitializer() {
        return null;
    }


}
