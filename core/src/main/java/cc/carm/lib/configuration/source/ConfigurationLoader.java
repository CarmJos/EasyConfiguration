package cc.carm.lib.configuration.source;

import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.configuration.source.path.PathGenerator;
import cc.carm.lib.configuration.source.path.StandardPathGenerator;
import cc.carm.lib.configuration.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * Configuration loader,
 * used to load configuration values from {@link cc.carm.lib.configuration.core.Configuration} classes.
 */
public class ConfigurationLoader<P extends ConfigurationProvider<P>> {

    protected final P provider;
    protected PathGenerator<P> pathGenerator;

    public ConfigurationLoader(P provider) {
        this(provider, StandardPathGenerator.of(provider));
    }

    public ConfigurationLoader(P provider, PathGenerator<P> pathGenerator) {
        this.provider = provider;
        this.pathGenerator = pathGenerator;
    }

    public void setPathGenerator(PathGenerator<P> pathGenerator) {
        this.pathGenerator = pathGenerator;
    }

    public PathGenerator<P> getPathGenerator() {
        return pathGenerator;
    }

    /**
     * 初始化指定类以及其子类的所有 {@link ConfigValue} 对象。
     *
     * @param clazz 配置文件类，须继承于 {@link Configuration} 。
     */
    public void initialize(@NotNull P provider, @NotNull Class<? extends Configuration> clazz) {
        initialize(clazz, saveDefaults, true);
    }

    /**
     * 初始化指定类的所有 {@link ConfigValue} 对象。
     *
     * @param clazz          配置文件类，须继承于 {@link Configuration} 。
     * @param saveDefaults   是否写入默认值(默认为 true)。
     * @param loadSubClasses 是否加载内部子类(默认为 true)。
     */
    public void initialize(@NotNull Class<? extends Configuration> clazz) {
        initializeStaticClass(
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

    /**
     * 初始化指定实例的所有 {@link ConfigValue} 与内部 {@link Configuration} 对象。
     *
     * @param config 配置文件实例类，须实现 {@link Configuration} 。
     */
    public void initialize(@NotNull Configuration config) {
        initialize(config, true);
    }

    /**
     * 初始化指定实例的所有 {@link ConfigValue} 与内部 {@link Configuration} 对象。
     *
     * @param config       配置文件实例类，须实现 {@link Configuration} 。
     * @param saveDefaults 是否写入默认值(默认为 true)。
     */
    public void initialize(@NotNull Configuration config, boolean saveDefaults) {
        initializeInstance(
                config, null, null,
                null, null, null,
                saveDefaults
        );
        if (saveDefaults) {
            try {
                provider.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 针对实例类的初始化方法
    private void initializeInstance(@NotNull Configuration root,
                                    @Nullable String parentPath, @Nullable String fieldName,
                                    @Nullable ConfigPath fieldPath,
                                    @Nullable HeaderComment fieldHeaderComments,
                                    @Nullable InlineComment fieldInlineComments,
                                    boolean saveDefaults) {
        String path = getClassPath(root.getClass(), parentPath, fieldName, fieldPath);
        this.provider.setHeaderComment(path, getClassHeaderComments(root.getClass(), fieldHeaderComments));
        if (path != null) this.provider.setInlineComment(path, readInlineComments(fieldInlineComments));

        for (Field field : root.getClass().getDeclaredFields()) {
            initializeField(root, field, path, saveDefaults, false);
        }
    }

    // 针对静态类的初始化方法
    private void initializeStaticClass(@NotNull Class<?> clazz,
                                       @Nullable String parentPath, @Nullable String fieldName,
                                       @Nullable ConfigPath fieldPath,
                                       @Nullable HeaderComment fieldHeaderComments,
                                       @Nullable InlineComment fieldInlineComments,
                                       boolean saveDefaults, boolean loadSubClasses) {
        if (!Configuration.class.isAssignableFrom(clazz)) return; // 只解析继承了 ConfigurationRoot 的类
        String path = getClassPath(clazz, parentPath, fieldName, fieldPath);
        this.provider.setHeaderComment(path, getClassHeaderComments(clazz, fieldHeaderComments));
        if (path != null) this.provider.setInlineComment(path, readInlineComments(fieldInlineComments));

        for (Field field : clazz.getDeclaredFields()) {
            initializeField(clazz, field, path, saveDefaults, loadSubClasses);
        }

        if (!loadSubClasses) return;
        Class<?>[] classes = clazz.getDeclaredClasses();
        for (int i = classes.length - 1; i >= 0; i--) {   // 逆向加载，保持顺序。
            initializeStaticClass(
                    classes[i], path, classes[i].getSimpleName(),
                    null, null, null,
                    saveDefaults, true
            );
        }
    }

    private void initializeField(@NotNull Object source, @NotNull Field field,
                                 @Nullable String parent, boolean saveDefaults, boolean loadSubClasses) {
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
            } else if (source instanceof Configuration && object instanceof Configuration) {
                // 当且仅当 源字段与字段 均为ConfigurationRoot实例时，才对目标字段进行下一步初始化加载。
                initializeInstance(
                        (Configuration) object, parent, field.getName(),
                        field.getAnnotation(ConfigPath.class),
                        field.getAnnotation(HeaderComment.class),
                        field.getAnnotation(InlineComment.class),
                        saveDefaults
                );
            } else if (source instanceof Class<?> && object instanceof Class<?>) {
                // 当且仅当 源字段与字段 均为静态类时，才对目标字段进行下一步初始化加载。
                initializeStaticClass(
                        (Class<?>) object, parent, field.getName(),
                        field.getAnnotation(ConfigPath.class),
                        field.getAnnotation(HeaderComment.class),
                        field.getAnnotation(InlineComment.class),
                        saveDefaults, loadSubClasses
                );
            }

            // 以上判断实现以下规范：
            // - 实例类中仅加载 ConfigValue实例 与 ConfigurationRoot实例
            // - 静态类中仅加载 静态ConfigValue实例 与 静态ConfigurationRoot类

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

}
