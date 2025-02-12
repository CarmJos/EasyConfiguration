package cc.carm.test.config;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.section.ConfigureSource;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TestSource extends ConfigureSource<TestSection, Map<String, String>, TestSource> {


    public TestSource(@NotNull ConfigurationHolder<? extends TestSource> holder, long lastUpdateMillis) {
        super(holder, lastUpdateMillis);
    }

    @Override
    protected TestSource self() {
        return this;
    }

    @Override
    public void save() throws Exception {

    }

    @Override
    protected void onReload() throws Exception {

    }

    @Override
    public @NotNull Map<String, String> original() {
        return null;
    }

    @Override
    public @NotNull TestSection section() {
        return null;
    }

    @Override
    public @NotNull ConfigureSource<?, ?, ?> source() {
        return null;
    }
}
