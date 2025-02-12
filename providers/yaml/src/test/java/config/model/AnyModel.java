package config.model;

import cc.carm.lib.configuration.demo.tests.model.AbstractRecord;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.bspfsystems.yamlconfiguration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("AnyModel")
public class AnyModel extends AbstractRecord implements ConfigurationSerializable {

    public final boolean bool;

    public AnyModel(@NotNull String name, boolean bool) {
        super(name);
        this.bool = bool;
    }

    @Override
    public String toString() {
        return "AnyModel{" +
                "name='" + name + '\'' +
                ", bool=" + bool +
                '}';
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("state", bool);
        return map;
    }

    public static AnyModel random() {
        return new AnyModel(UUID.randomUUID().toString().substring(0, 5), Math.random() > 0.5);
    }


    @TestOnly
    public static AnyModel deserialize(Map<String, ?> args) {
        return new AnyModel((String) args.get("name"), (Boolean) args.get("state"));
    }


}
