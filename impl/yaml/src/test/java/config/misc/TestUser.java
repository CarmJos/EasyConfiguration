package config.misc;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestUser {

    public String name;
    public UUID uuid;

    public TestUser(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
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

    public static TestUser deserialize(ConfigurationWrapper section) throws Exception {
        String name = section.getString("name");
        if (name == null) throw new NullPointerException("name is null");
        String uuidString = section.getString("info.uuid");
        if (uuidString == null) throw new NullPointerException("uuid is null");
        return new TestUser(name, UUID.fromString(uuidString));
    }

    @Override
    public String toString() {
        return "TestUser{" +
                "name='" + name + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}