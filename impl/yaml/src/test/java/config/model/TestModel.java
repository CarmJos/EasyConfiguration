package config.model;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.bspfsystems.yamlconfiguration.serialization.ConfigurationSerializable;
import org.bspfsystems.yamlconfiguration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("TestModel")
public class TestModel extends AbstractModel implements ConfigurationSerializable {

    public UUID uuid;

    public TestModel(String name, UUID uuid) {
        super(name);
        this.uuid = uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("uuid", uuid.toString());
        map.put("info", map2);
        return map;
    }

    public static TestModel deserialize(ConfigurationWrapper section) {
        String name = section.getString("name");
        if (name == null) throw new NullPointerException("name is null");
        String uuidString = section.getString("info.uuid");
        if (uuidString == null) throw new NullPointerException("uuid is null");
        return new TestModel(name, UUID.fromString(uuidString));
    }

    @TestOnly
    @SuppressWarnings("unchecked")
    public static TestModel deserialize(Map<String, ?> args) {
        String name = (String) args.get("name");
        if (name == null) throw new NullPointerException("name is null");
        Map<String, ?> map = (Map<String, ?>) args.get("info");
        String uuidString = (String) map.get("uuid");
        if (uuidString == null) throw new NullPointerException("uuid is null");
        return new TestModel(name, UUID.fromString(uuidString));
    }

    public static TestModel random() {
        return new TestModel(UUID.randomUUID().toString().substring(0, 5), UUID.randomUUID());
    }


    @Override
    public String toString() {
        return "TestUser{" +
                "name='" + name + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}