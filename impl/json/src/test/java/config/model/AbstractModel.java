package config.model;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractModel {

    protected final @NotNull String name;

    public AbstractModel(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }
}
