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
        ConfigPath sectionAnnotation = rootClazz.getAnnotation(ConfigPath.class);

        String rootSection = null;
        if (sectionAnnotation != null && sectionAnnotation.value().length() > 1) {
            rootSection = sectionAnnotation.value();
        }

        for (Class<?> innerClass : rootClazz.getDeclaredClasses()) {
            initSection(provider, rootSection, innerClass, saveDefault);
        }

        for (Field field : rootClazz.getFields()) {
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

        for (Field field : clazz.getFields()) initValue(provider, section, clazz, field, saveDefault);
        for (Class<?> innerClass : clazz.getDeclaredClasses()) initSection(provider, section, innerClass, saveDefault);

    }

    private static void initValue(ConfigurationProvider provider, String parentSection, Class<?> clazz, Field field, boolean saveDefault) {
        try {
            Object object = field.get(clazz);
            if (object instanceof ConfigValue<?>) {
                initializeValue(
                        provider, (ConfigValue<?>) object,
                        getSectionPath(field.getName(), parentSection, field.getAnnotation(ConfigPath.class)),
                        Optional.ofNullable(field.getAnnotation(ConfigComment.class))
                                .map(ConfigComment::value).orElse(new String[0]),
                        saveDefault);
            }
        } catch (IllegalAccessException ignored) {
        }
    }


    public static void initializeValue(@NotNull ConfigurationProvider provider, @NotNull ConfigValue<?> value,
                                       @NotNull String path, @NotNull String[] comments, boolean saveDefault) {
        value.initialize(provider, path, comments);
        if (saveDefault && value.getDefaultValue() != null && !provider.getConfiguration().contains(path)) {
            value.setDefault();
        }
    }

    public static String getSectionPath(@NotNull String name,
                                        @Nullable String parentSection,
                                        @Nullable ConfigPath pathAnnotation) {
        String parent = parentSection != null ? parentSection + "." : "";
        if (pathAnnotation != null && pathAnnotation.value().length() > 0) {
            return parent + pathAnnotation.value();
        } else {
            return parent + getSectionName(name);
        }
    }

    public static String getSectionName(String codeName) {
        return codeName.toLowerCase().replace("_", "-");
    }


}
