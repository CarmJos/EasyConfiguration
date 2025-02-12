package cc.carm.lib.configuration.demo.tests.model;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractRecord {

    protected final @NotNull String name;

    public AbstractRecord(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }
}
