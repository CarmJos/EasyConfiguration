package cc.carm.lib.configuration.core;

import cc.carm.lib.configuration.core.annotation.ConfigComment;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

public class ConfigInitializer {

    public static void initialize(ConfigurationProvider source, Class<? extends ConfigurationRoot> rootClazz) {
        initialize(source, rootClazz, true);
    }

    public static void initialize(ConfigurationProvider provider, Class<? extends ConfigurationRoot> rootClazz, boolean saveDefault) {
        String rootSection = null;

        ConfigPath sectionAnnotation = rootClazz.getAnnotation(ConfigPath.class);
        if (sectionAnnotation != null && sectionAnnotation.value().length() > 1) {
            rootSection = sectionAnnotation.value();
        }

        if (rootSection != null) {
            //Not usable for null section.
            ConfigComment comments = rootClazz.getAnnotation(ConfigComment.class);
            if (comments != null && comments.value().length > 0) {
                provider.setComments(rootSection, comments.value());
            }
        }

        for (Class<?> innerClass : rootClazz.getDeclaredClasses()) {
            initSection(provider, rootSection, innerClass, saveDefault);
        }

        for (Field field : rootClazz.getDeclaredFields()) {
            initValue(provider, rootSection, rootClazz, field, saveDefault);
        }

        if (saveDefault) {
            try {
                provider.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static void initSection(ConfigurationProvider provider, String parentSection, Class<?> clazz, boolean saveDefault) {
        if (!Modifier.isStatic(clazz.getModifiers()) || !Modifier.isPublic(clazz.getModifiers())) return;

        String section = getSectionPath(clazz.getSimpleName(), parentSection, clazz.getAnnotation(ConfigPath.class));
        ConfigComment comments = clazz.getAnnotation(ConfigComment.class);
        if (comments != null && comments.value().length > 0) {
            provider.setComments(parentSection, comments.value());
        }

        for (Field field : clazz.getDeclaredFields()) initValue(provider, section, clazz, field, saveDefault);
        for (Class<?> innerClass : clazz.getDeclaredClasses()) initSection(provider, section, innerClass, saveDefault);

    }

    private static void initValue(ConfigurationProvider provider, String parentSection, Class<?> clazz, Field field, boolean saveDefault) {
        try {
            field.setAccessible(true);
            Object object = field.get(clazz);
            if (object instanceof ConfigValue<?>) {
                initializeValue(
                        provider, (ConfigValue<?>) object, saveDefault,
                        getSectionPath(field.getName(), parentSection, field.getAnnotation(ConfigPath.class)),
                        Optional.ofNullable(field.getAnnotation(ConfigComment.class))
                                .map(ConfigComment::value).orElse(new String[0])
                );
            }
        } catch (IllegalAccessException ignored) {
        }
    }


    public static void initializeValue(@NotNull ConfigurationProvider provider, @NotNull ConfigValue<?> value,
                                       boolean saveDefault, @NotNull String path, @NotNull String[] comments) {
        value.initialize(provider, path, comments);
        if (saveDefault && value.getDefaultValue() != null && !provider.getConfiguration().contains(path)) {
            value.setDefault();
        }
    }

    public static String getSectionPath(@NotNull String name,
                                        @Nullable String parentSection,
                                        @Nullable ConfigPath pathAnnotation) {
        @NotNull String parent = parentSection != null ? parentSection + "." : "";
        @NotNull String path = getSectionName(name);
        boolean root = false;
        if (pathAnnotation != null) {
            if (pathAnnotation.value().length() > 0) path = pathAnnotation.value();
            root = pathAnnotation.root();
        }
        return (root ? "" : parent) + path;
    }

    /**
     * 得到指定元素的配置名称。
     * 采用 全小写，以“-”链接 的命名规则。
     *
     * @param name 源名称
     * @return 全小写，以“-”链接 的 路径名称
     */
    public static String getSectionName(String name) {
        return name.replaceAll("[A-Z]", "-$0") // 将驼峰转换为蛇形;
                .replaceAll("-(.*)", "$1") // 若首字母也为大写，则也会被转换，需要去掉第一个横线
                .replaceAll("_-([A-Z])", "_$1") // 因为命名中可能包含 _，因此需要被特殊处理一下
                .replaceAll("([a-z])-([A-Z])", "$1_$2") // 然后将非全大写命名的内容进行转换
                .replace("-", "") // 移除掉多余的横线
                .replace("_", "-") // 将下划线替换为横线
                .toLowerCase(); // 最后转为全小写
    }


}
