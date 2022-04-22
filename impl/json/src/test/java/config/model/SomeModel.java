package config.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.Map;

public class SomeModel extends AbstractModel {

    int num;

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

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("num", num);
        return map;
    }

    @TestOnly
    public static SomeModel deserialize(Map<String, ?> args) {
        return new SomeModel((String) args.get("name"), (Integer) args.get("num"));
    }


}
