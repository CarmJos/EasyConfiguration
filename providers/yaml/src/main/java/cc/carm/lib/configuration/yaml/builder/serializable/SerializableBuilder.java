package cc.carm.lib.configuration.yaml.builder.serializable;

import cc.carm.lib.configuration.yaml.builder.AbstractYAMLBuilder;
import cc.carm.lib.configuration.yaml.value.ConfiguredSerializable;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class SerializableBuilder<T extends ConfigurationSerializable>
        extends AbstractYAMLBuilder<T, SerializableBuilder<T>> {

    protected final @NotNull Class<T> valueClass;

    public SerializableBuilder(@NotNull Class<T> valueClass) {
        this.valueClass = valueClass;
    }

    @Override
    protected @NotNull SerializableBuilder<T> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredSerializable<T> build() {
        return new ConfiguredSerializable<>(buildManifest(), this.valueClass);
    }


}

