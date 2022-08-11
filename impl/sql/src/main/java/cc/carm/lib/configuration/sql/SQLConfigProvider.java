package cc.carm.lib.configuration.sql;

import cc.carm.lib.configuration.core.ConfigInitializer;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

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
    public void setHeaderComment(@Nullable String path, @Nullable List<String> comments) {

    }

    @Override
    public void setInlineComment(@NotNull String path, @Nullable String comment) {

    }

    @Override
    public @Nullable @Unmodifiable List<String> getHeaderComment(@Nullable String path) {
        return null;
    }

    @Override
    public @Nullable String getInlineComment(@NotNull String path) {
        return null;
    }

    @Override
    public @NotNull ConfigInitializer<? extends ConfigurationProvider<SQLSectionWrapper>> getInitializer() {
        return null;
    }


}
