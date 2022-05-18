package cc.carm.lib.configuration.core;

import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.annotation.InlineComment;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * 配置文件类初始化方法
 * 用于初始化 {@link ConfigurationRoot} 中的每个 {@link ConfigValue} 对象
 *
 * @param <T> {@link ConfigurationProvider} 配置文件的数据来源
 * @author CarmJos
 */
public class ConfigInitializer<T extends ConfigurationProvider<?>> {

    protected final @NotNull T provider;

    public ConfigInitializer(@NotNull T provider) {
        this.provider = provider;
    }

    /**
     * 初始化指定类以及其子类的所有 {@link ConfigValue} 对象。
     *
     * @param clazz        配置文件类，须继承于 {@link ConfigurationRoot} 。
     * @param saveDefaults 是否写入默认值(默认为 true)。
     */
    public void initialize(@NotNull Class<? extends ConfigurationRoot> clazz, boolean saveDefaults) {
        initialize(clazz, saveDefaults, true);
    }

    /**
     * 初始化指定类的所有 {@link ConfigValue} 对象。
     *
     * @param clazz          配置文件类，须继承于 {@link ConfigurationRoot} 。
     * @param saveDefaults   是否写入默认值(默认为 true)。
     * @param loadSubClasses 是否加载内部子类(默认为 true)。
     */
    public void initialize(@NotNull Class<? extends ConfigurationRoot> clazz,
                           boolean saveDefaults, boolean loadSubClasses) {
        initializeClass(
                clazz, null, null,
                null, null, null,
                saveDefaults, loadSubClasses
        );
        if (saveDefaults) {
            try {
                provider.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void initializeClass(@NotNull Class<?> clazz,
                                   @Nullable String parentPath, @Nullable String fieldName,
                                   @Nullable ConfigPath fieldPath,
                                   @Nullable HeaderComment fieldHeaderComments,
                                   @Nullable InlineComment fieldInlineComments,
                                   boolean saveDefaults, boolean loadSubClasses) {
        String path = getClassPath(clazz, parentPath, fieldName, fieldPath);
        this.provider.setHeaderComment(path, getClassHeaderComments(clazz, fieldHeaderComments));
        if (path != null) this.provider.setInlineComment(path, readInlineComments(fieldInlineComments));

        for (Field field : clazz.getDeclaredFields()) {
            initializeField(clazz, field, path, saveDefaults, loadSubClasses);
        }

        if (!loadSubClasses) return;
        Class<?>[] classes = clazz.getDeclaredClasses();
        for (int i = classes.length - 1; i >= 0; i--) {   // 逆向加载，保持顺序。
            initializeClass(
                    classes[i], path, classes[i].getSimpleName(),
                    null, null, null,
                    saveDefaults, true
            );
        }
    }

    private void initializeField(@NotNull Class<?> source, @NotNull Field field, @Nullable String parent,
                                 boolean saveDefaults, boolean loadSubClasses) {
        try {
            field.setAccessible(true);
            Object object = field.get(source);
            if (object instanceof ConfigValue<?>) {
                initializeValue(
                        (ConfigValue<?>) object, getFieldPath(field, parent),
                        field.getAnnotation(HeaderComment.class),
                        field.getAnnotation(InlineComment.class),
                        saveDefaults
                );
            } else if (object instanceof Class<?>) {
                initializeClass(
                        (Class<?>) object, parent, field.getName(),
                        field.getAnnotation(ConfigPath.class),
                        field.getAnnotation(HeaderComment.class),
                        field.getAnnotation(InlineComment.class),
                        saveDefaults, loadSubClasses
                );
            }
        } catch (IllegalAccessException ignored) {
        }
    }

    protected void initializeValue(@NotNull ConfigValue<?> value, @NotNull String path,
                                   @Nullable HeaderComment fieldHeaderComment,
                                   @Nullable InlineComment fieldInlineComment,
                                   boolean saveDefaults) {
        value.initialize(
                provider, saveDefaults, path,
                readHeaderComments(fieldHeaderComment),
                readInlineComments(fieldInlineComment)
        );
    }

    protected static @Nullable List<String> getClassHeaderComments(@NotNull Class<?> clazz,
                                                                   @Nullable HeaderComment fieldAnnotation) {
        List<String> classComments = readHeaderComments(clazz.getAnnotation(HeaderComment.class));
        if (classComments != null) return classComments;
        else return readHeaderComments(fieldAnnotation);
    }


    protected static List<String> readHeaderComments(@Nullable HeaderComment annotation) {
        if (annotation == null) return null;
        String[] value = annotation.value();
        return value.length > 0 ? Arrays.asList(value) : null;
    }


    protected static @Nullable String readInlineComments(@Nullable InlineComment annotation) {
        if (annotation == null) return null;
        String value = annotation.value();
        return value.length() > 0 ? value : null;
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
