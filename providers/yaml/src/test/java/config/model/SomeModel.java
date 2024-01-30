package config.model;

import cc.carm.lib.configuration.demo.tests.model.AbstractModel;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.bspfsystems.yamlconfiguration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("SomeModel")
public class SomeModel extends AbstractModel implements ConfigurationSerializable {

    public final int num;

    public SomeModel(@NotNull String name, int num) {
        super(name);
        this.num = num;
    }

    @Override
    public String toString() {
        return "SomeModel{" +
                "name='" + name + '\'' +
                ", num=" + num +
                '}';
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("num", num);
        return map;
    }

    public static SomeModel random() {
        return new SomeModel(UUID.randomUUID().toString().substring(0, 5), (int) (Math.random() * 1000));
    }


    @TestOnly
    public static SomeModel deserialize(Map<String, ?> args) {
        return new SomeModel((String) args.get("name"), (Integer) args.get("num"));
    }


}
