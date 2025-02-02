package cc.carm.lib.configuration.source.loader;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.source.option.ConfigurationOptions;
import cc.carm.lib.configuration.value.ConfigValue;
import cc.carm.lib.configuration.value.ValueManifest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Configuration loader,
 * used to load configuration values from {@link Configuration} classes.
 */
public class ConfigurationLoader {

    public static final Field PATH_FIELD;
    public static final Field PROVIDER_FIELD;

    static {
        try {
            PATH_FIELD = ValueManifest.class.getDeclaredField("path");
            PATH_FIELD.setAccessible(true);
            PROVIDER_FIELD = ValueManifest.class.getDeclaredField("provider");
            PROVIDER_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    protected PathGenerator pathGenerator;

    public ConfigurationLoader() {
        this(StandardPathGenerator.of());
    }

    public ConfigurationLoader(PathGenerator pathGenerator) {
        this.pathGenerator = pathGenerator;
    }

    public void setPathGenerator(PathGenerator pathGenerator) {
        this.pathGenerator = pathGenerator;
    }

    public PathGenerator getPathGenerator() {
        return pathGenerator;
    }

    public @Nullable String getFieldPath(ConfigurationProvider<?> provider, @Nullable String parentPath, @NotNull Field field) {
        return pathGenerator.getFieldPath(provider, parentPath, field);
    }

    public @Nullable String getClassPath(ConfigurationProvider<?> provider, @Nullable String parentPath,
                                         @NotNull Class<?> clazz, @Nullable Field clazzField) {
        return pathGenerator.getClassPath(provider, parentPath, clazz, clazzField);
    }

    public void load(ConfigurationProvider<?> provider, @NotNull Configuration config) throws Exception {
        initializeInstance(provider, config, null, null);
        if (provider.option(ConfigurationOptions.SET_DEFAULTS)) provider.save();
    }

    public void load(ConfigurationProvider<?> provider, @NotNull Class<? extends Configuration> clazz) throws Exception {
        initializeStaticClass(provider, clazz, null, null);
        if (provider.option(ConfigurationOptions.SET_DEFAULTS)) provider.save();
    }


    // 针对实例类的初始化方法
    private void initializeInstance(@NotNull ConfigurationProvider<?> provider,
                                    @NotNull Configuration root, @Nullable String parentPath, @Nullable Field configField) {
        String path = getClassPath(provider, parentPath, root.getClass(), configField);
        Arrays.stream(root.getClass().getDeclaredFields()).forEach(field -> initializeField(provider, root, field, path));
    }

    // 针对静态类的初始化方法
    private void initializeStaticClass(@NotNull ConfigurationProvider<?> provider,
                                       @NotNull Class<?> clazz, @Nullable String parentPath, @Nullable Field configField) {
        if (!Configuration.class.isAssignableFrom(clazz)) return; // 只解析继承了 ConfigurationRoot 的类
        String path = getClassPath(provider, parentPath, clazz, configField);

        for (Field field : clazz.getDeclaredFields()) {
            initializeField(provider, clazz, field, path);
        }

        if (!provider.option(ConfigurationOptions.LOAD_SUB_CLASSES)) return;
        Class<?>[] classes = clazz.getDeclaredClasses();
        for (int i = classes.length - 1; i >= 0; i--) {   // 逆向加载，保持顺序。
            initializeStaticClass(provider, classes[i], path, null);
        }
    }

    private void initializeField(@NotNull ConfigurationProvider<?> provider,
                                 @NotNull Object source, @NotNull Field field, @Nullable String parent) {
        try {
            field.setAccessible(true);
            Object object = field.get(source);
//
            if (object instanceof ConfigValue<?>) {
                // 目标是 ConfigValue 实例，进行具体的初始化注入
                ConfigValue<?> value = (ConfigValue<?>) object;
                String path = getFieldPath(provider, parent, field);
                if (path == null) return;
                insertIfAbsent(value, PATH_FIELD, path);
                insertIfAbsent(value, PROVIDER_FIELD, provider);
            } else if (source instanceof Configuration && object instanceof Configuration) {
                // 当且仅当 源字段与字段 均为Configuration实例时，才对目标字段进行下一步初始化加载。
                initializeInstance(provider, (Configuration) object, parent, field);
            } else if (source instanceof Class<?> && object instanceof Class<?>) {
                // 当且仅当 源字段与字段 均为静态类时，才对目标字段进行下一步初始化加载。
                initializeStaticClass(provider, (Class<?>) object, parent, field);
            }

            // 以上判断实现以下规范：
            // - 实例类中仅加载 ConfigValue实例 与 Configuration实例
            // - 静态类中仅加载 静态ConfigValue实例 与 静态Configuration类

        } catch (IllegalAccessException ignored) {
        }
    }

    private void insertIfAbsent(@NotNull ValueManifest<?> value, @NotNull Field field, @NotNull Object obj) {
        try {
            if (field.get(obj) == null) field.set(obj, value);
        } catch (IllegalAccessException ignored) {
        }
    }

}
