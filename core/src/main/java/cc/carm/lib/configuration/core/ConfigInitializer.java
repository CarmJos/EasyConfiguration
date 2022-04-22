package cc.carm.lib.configuration.core;

import cc.carm.lib.configuration.core.annotation.ConfigComment;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class ConfigInitializer<T extends ConfigurationProvider<?>> {

    protected final @NotNull T provider;

    public ConfigInitializer(@NotNull T provider) {
        this.provider = provider;
    }

    public void initialize(@NotNull Class<? extends ConfigurationRoot> clazz, boolean saveDefaults) {
        initialize(clazz, saveDefaults, true);
    }

    public void initialize(@NotNull Class<? extends ConfigurationRoot> clazz,
                           boolean saveDefaults, boolean loadSubClasses) {
        initializeClass(clazz, null, null, null, null, saveDefaults, loadSubClasses);
    }

    protected void initializeClass(@NotNull Class<?> clazz,
                                   @Nullable String parentPath, @Nullable String fieldName,
                                   @Nullable ConfigPath fieldPath, @Nullable ConfigComment filedComments,
                                   boolean saveDefaults, boolean loadSubClasses) {
        String path = getClassPath(clazz, parentPath, fieldName, fieldPath);
        if (path != null) setComments(path, getClassComments(clazz, filedComments));
        for (Field field : clazz.getDeclaredFields()) {
            initializeField(clazz, field, path, saveDefaults, loadSubClasses);
        }

        Class<?>[] classes = clazz.getDeclaredClasses();
        if (loadSubClasses && classes.length > 0) {
            // 逆向加载，保持顺序。
            for (int i = classes.length - 1; i >= 0; i--) {
                initializeClass(
                        classes[i], path, classes[i].getSimpleName(),
                        null, null,
                        saveDefaults, true
                );
            }
        }
    }


    protected void initializeValue(@NotNull ConfigValue<?> value, @NotNull String path,
                                   @NotNull String[] comments, boolean saveDefaults) {
        value.initialize(provider, saveDefaults, path, comments);
    }

    private void initializeField(@NotNull Class<?> source, @NotNull Field field, @Nullable String parent,
                                 boolean saveDefaults, boolean loadSubClasses) {

        try {
            field.setAccessible(true);
            Object object = field.get(source);
            if (object instanceof ConfigValue<?>) {
                String path = getFieldPath(field, parent);
                String[] comments = readComments(field.getAnnotation(ConfigComment.class));
                initializeValue((ConfigValue<?>) object, path, comments, saveDefaults);
                setComments(path, comments);
            } else if (object instanceof Class<?>) {
                initializeClass(
                        (Class<?>) object, parent, field.getName(),
                        field.getAnnotation(ConfigPath.class),
                        field.getAnnotation(ConfigComment.class),
                        saveDefaults, loadSubClasses
                );
            }
        } catch (IllegalAccessException ignored) {
        }
    }

    protected void setComments(@NotNull String path, @Nullable ConfigComment filedComments) {
        setComments(path, readComments(filedComments));
    }

    protected void setComments(@NotNull String path, @NotNull String[] comments) {
        if (comments.length <= 0) return;
        this.provider.setComments(path, comments);
    }

    protected static @NotNull String[] readComments(@Nullable ConfigComment filedComments) {
        if (filedComments == null) return new String[0];
        if (String.join("", filedComments.value()).length() <= 0) return new String[0];
        return filedComments.value();
    }

    protected static @NotNull String[] getClassComments(@NotNull Class<?> clazz,
                                                        @Nullable ConfigComment fieldAnnotation) {
        String[] clazzComments = readComments(clazz.getAnnotation(ConfigComment.class));
        if (clazzComments.length > 0) return clazzComments;
        else return readComments(fieldAnnotation);
    }

    protected static @Nullable String getClassPath(@NotNull Class<?> clazz,
                                                   @Nullable String parentPath,
                                                   @Nullable String filedName,
                                                   @Nullable ConfigPath fieldAnnotation) {
        @NotNull String parent = parentPath != null ? parentPath + "." : "";
        boolean fromRoot = false;

        // 先获取 Class 对应的路径注解 其优先度最高。
        ConfigPath clazzAnnotation = clazz.getAnnotation(ConfigPath.class);
        if (clazzAnnotation != null) {
            fromRoot = clazzAnnotation.root();
            if (clazzAnnotation.value().length() > 0) {
                return (fromRoot ? "" : parent) + clazzAnnotation.value();
            }
        }

        if (fieldAnnotation != null) {
            fromRoot = fromRoot || fieldAnnotation.root();
            if (fieldAnnotation.value().length() > 0) {
                return (fromRoot ? "" : parent) + fieldAnnotation.value();
            }
        }

        // 再由 fieldName 获取路径
        if (filedName != null) return (fromRoot ? "" : parent) + getPathFromName(filedName);
        else return null; // 不满足上述条件 且 无 fieldName，则说明是根路径。
    }

    protected static @NotNull String getFieldPath(@NotNull Field field, @Nullable String parentPath) {
        @NotNull String parent = parentPath != null ? parentPath + "." : "";
        boolean fromRoot = false;

        // 先获取 Field 对应的路径注解 其优先度最高。
        ConfigPath pathAnnotation = field.getAnnotation(ConfigPath.class);
        if (pathAnnotation != null) {
            fromRoot = pathAnnotation.root();
            if (pathAnnotation.value().length() > 0) {
                return (fromRoot ? "" : parent) + pathAnnotation.value();
            }
        }

        // 最后再通过 fieldName 自动生成路径
        return (fromRoot ? "" : parent) + getPathFromName(field.getName());
    }

    /**
     * 得到指定元素的配置名称。
     * 采用 全小写，以“-”链接 的命名规则。
     *
     * @param name 源名称
     * @return 全小写，以“-”链接 的 路径名称
     */
    public static String getPathFromName(String name) {
        return name.replaceAll("[A-Z]", "-$0") // 将驼峰转换为蛇形;
                .replaceAll("-(.*)", "$1") // 若首字母也为大写，则也会被转换，需要去掉第一个横线
                .replaceAll("_-([A-Z])", "_$1") // 因为命名中可能包含 _，因此需要被特殊处理一下
                .replaceAll("([a-z])-([A-Z])", "$1_$2") // 然后将非全大写命名的内容进行转换
                .replace("-", "") // 移除掉多余的横线
                .replace("_", "-") // 将下划线替换为横线
                .toLowerCase(); // 最后转为全小写
    }


}
