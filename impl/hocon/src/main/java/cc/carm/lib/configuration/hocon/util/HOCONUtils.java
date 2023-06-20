package cc.carm.lib.configuration.hocon.util;

import cc.carm.lib.configuration.hocon.HOCONConfigWrapper;
import cc.carm.lib.configuration.hocon.exception.HOCONGetValueException;
import com.typesafe.config.*;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class HOCONUtils {
    public static String getSimplePath(String path, char separator) {
        int index = path.lastIndexOf(separator);
        return (index == -1) ? path : path.substring(index + 1);
    }

    public static HOCONConfigWrapper getObjectOn(@NotNull HOCONConfigWrapper parent, @NotNull String path, char separator) {
        String currentPath = path;
        HOCONConfigWrapper currentObject = parent;
        int index;
        while ((index = currentPath.indexOf(separator)) != -1) {
            HOCONConfigWrapper previousObject = currentObject;
            String pathName = currentPath.substring(0, index);
            try {
                currentObject = (HOCONConfigWrapper) previousObject.getDirect(pathName);
            } catch (ClassCastException e) {
                throw new HOCONGetValueException(e);
            }
            if (currentObject == null) {
                currentObject = new HOCONConfigWrapper(ConfigFactory.empty().root());
                previousObject.setDirect(pathName, currentObject);
            }
            currentPath = currentPath.substring(0, index);
        }

        return currentObject;
    }

    /**
     * 在 Object 中获取所有键
     * 思路：在第一次执行时 prefix 应该是 ""
     * 后续找到了更深层的键，将会变为 "deep."
     * 下一次键名就是 "deep.key"
     *
     * @param parent Object
     * @param deep 是否更深层获取
     * @param prefix 当前 Object 键名前缀
     * @return Object 中的所有键
     */
    public static LinkedHashSet<String> getKeysFromObject(HOCONConfigWrapper parent, boolean deep, String prefix) {
        return parent.getSource().entrySet().stream().collect(
                LinkedHashSet::new,
                (set, entry) -> {
                    Object value = entry.getValue();
                    if (value instanceof HOCONConfigWrapper && deep) {
                        set.addAll(HOCONUtils.getKeysFromObject((HOCONConfigWrapper) value, true, prefix + entry.getKey() + "."));
                    } else {
                        set.add(prefix + entry.getKey());
                    }
                },
                LinkedHashSet::addAll
        );
    }

    /**
     * 将 Object 保存为字符串
     * 并使用注释器打上注释
     *
     * @param object Object
     * @param commenter 注释器
     * @return 保存的字符串
     */
    public static @NotNull String renderWithComment(@NotNull HOCONConfigWrapper object, @NotNull Function<String, List<String>> commenter) {
        return HOCONUtils.makeConfigWithComment(object, "", commenter).root().render(
                ConfigRenderOptions.defaults()
                        .setJson(false)
                        .setOriginComments(false)
        );
    }

    public static @NotNull Config makeConfigWithComment(@NotNull HOCONConfigWrapper object, @NotNull String prefix, @NotNull Function<String, List<String>> commenter) {
        Config config = ConfigFactory.empty();
        for (Map.Entry<String, Object> entry : object.getSource().entrySet()) {
            String key = entry.getKey();
            String fullKey = prefix + key;
            Object value = entry.getValue();
            ConfigValue result;
            if (value instanceof Iterable) {
                result = ConfigValueFactory.fromIterable((Iterable<?>) value);
            } else if (value instanceof HOCONConfigWrapper) {
                result = makeConfigWithComment((HOCONConfigWrapper) value, fullKey + ".", commenter).root();
            } else {
                result = ConfigValueFactory.fromAnyRef(value);
            }
            result = result.withOrigin(
                    ConfigOriginFactory.newSimple()
                            .withComments(commenter.apply(fullKey))
            );
            config = config.withValue(key, result);
        }
        return config;
    }
}
