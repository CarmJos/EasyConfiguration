package cc.carm.lib.configuration.spigot.builder.serializable;

import cc.carm.lib.configuration.spigot.builder.AbstractSpigotBuilder;
import cc.carm.lib.configuration.spigot.value.ConfiguredSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class SerializableBuilder<T extends ConfigurationSerializable>
        extends AbstractSpigotBuilder<T, SerializableBuilder<T>> {

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
        return new ConfiguredSerializable<>(this.provider, this.path, this.comments, this.valueClass, this.defaultValue);
    }


}

