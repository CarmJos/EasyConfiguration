package config.misc;

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

    @Override
    public String toString() {
        return "TestUser{" +
                "name='" + name + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}