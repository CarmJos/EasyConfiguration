package cc.carm.lib.configuration.yaml.builder;

import cc.carm.lib.configuration.core.builder.ConfigBuilder;
import cc.carm.lib.configuration.yaml.builder.serializable.SerializableBuilder;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class YAMLConfigBuilder extends ConfigBuilder {

    public <V extends ConfigurationSerializable> @NotNull SerializableBuilder<V> ofSerializable(@NotNull Class<V> valueClass) {
        return new SerializableBuilder<>(valueClass);
    }

}
