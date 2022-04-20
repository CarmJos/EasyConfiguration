package config.model;

import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractModel implements ConfigurationSerializable {

    protected final @NotNull String name;

    public AbstractModel(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }
}
