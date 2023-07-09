package cc.carm.lib.configuration.hocon;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.hocon.util.HOCONUtils;
import com.typesafe.config.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HOCONConfigWrapper implements ConfigurationWrapper<Map<String, Object>> {
    private static final char SEPARATOR = '.';
    protected final Map<String, Object> data;

    public HOCONConfigWrapper(ConfigObject config) {
        this.data = new LinkedHashMap<>();

        config.forEach((key, value) -> {
            Config cfg = config.toConfig();
            ConfigValue cv = cfg.getValue(key);
            if (cv.valueType() == ConfigValueType.OBJECT) {
                HOCONConfigWrapper.this.data.put(key, new HOCONConfigWrapper((ConfigObject) cv));
            } else {
                HOCONConfigWrapper.this.data.put(key, value.unwrapped());
            }
        });
    }

    @Override
    public @NotNull Map<String, Object> getSource() {
        return this.data;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return this.getValues(deep).keySet();
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return HOCONUtils.getKeysFromObject(this, deep, "").stream().collect(
                LinkedHashMap::new,
                (map, key) -> map.put(key, get(key)),
                LinkedHashMap::putAll
        );
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        // TEST start
        System.out.println(path);
        // TEST end
        if (value instanceof Map) {
            //noinspection unchecked
            value = new HOCONConfigWrapper(ConfigFactory.parseMap((Map<String, ?>) value).root());
        }

        HOCONConfigWrapper section = HOCONUtils.getObjectOn(this, path, SEPARATOR);
        String simplePath = HOCONUtils.getSimplePath(path, SEPARATOR);

        if (value == null) {
            section.data.remove(simplePath);
        } else {
            section.setDirect(simplePath, value);
        }
    }

    /**
     * 只能设置当前路径下的内容
     * 避免环回
     *
     * @param path 路径
     */
    public void setDirect(@NotNull String path, @Nullable Object value) {
        this.data.put(path, value);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return this.get(path) != null;
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        HOCONConfigWrapper section = HOCONUtils.getObjectOn(this, path, SEPARATOR);
        return section.getDirect(HOCONUtils.getSimplePath(path, SEPARATOR));
    }

    /**
     * 只能获取当前路径下的内容
     * 避免环回
     *
     * @param path 路径
     */
    public Object getDirect(@NotNull String path) {
        return this.data.get(path);
    }

    @Override
    public boolean isList(@NotNull String path) {
        return this.get(path) instanceof List<?>;
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        Object val = this.get(path);
        return (val instanceof List<?>) ? (List<?>) val : null;
    }

    @Override
    public boolean isConfigurationSection(@NotNull String path) {
        return this.get(path) instanceof HOCONConfigWrapper;
    }

    @Override
    public @Nullable ConfigurationWrapper<Map<String, Object>> getConfigurationSection(@NotNull String path) {
        Object val = get(path);
        return (val instanceof HOCONConfigWrapper) ? (HOCONConfigWrapper) val : null;
    }
}
