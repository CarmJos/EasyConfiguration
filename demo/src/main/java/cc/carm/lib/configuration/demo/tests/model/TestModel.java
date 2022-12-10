package cc.carm.lib.configuration.demo.tests.model;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestModel extends AbstractModel {

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

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("uuid", uuid.toString());
        map.put("info", map2);
        return map;
    }

    public static TestModel deserialize(ConfigurationWrapper<?> section) {
        String name = section.getString("name");
        if (name == null) throw new NullPointerException("name is null");
        String uuidString = section.getString("info.uuid");
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