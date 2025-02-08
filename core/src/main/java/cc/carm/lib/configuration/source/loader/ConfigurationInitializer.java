package cc.carm.lib.configuration.source.loader;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.source.meta.ConfigurationMetadata;
import cc.carm.lib.configuration.source.option.StandardOptions;
import cc.carm.lib.configuration.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Configuration initializer,
 * used to initialize {@link ConfigValue}s from {@link Configuration} classes.
 */
public class ConfigurationInitializer {

    protected @NotNull PathGenerator pathGenerator;
    protected @NotNull ConfigInitializeHandler<Field> fieldInitializer;
    protected @NotNull ConfigInitializeHandler<Class<? extends Configuration>> classInitializer;

    public ConfigurationInitializer() {
        this(PathGenerator.of(), ConfigInitializeHandler.start(), ConfigInitializeHandler.start());
    }

    public ConfigurationInitializer(@NotNull PathGenerator pathGenerator,
                                    @NotNull ConfigInitializeHandler<Field> fieldInitializer,
                                    @NotNull ConfigInitializeHandler<Class<? extends Configuration>> classInitializer) {
        this.pathGenerator = pathGenerator;
        this.fieldInitializer = fieldInitializer;
        this.classInitializer = classInitializer;
    }

    public void pathGenerator(@NotNull PathGenerator pathGenerator) {
        this.pathGenerator = pathGenerator;
    }

    public @NotNull PathGenerator pathGenerator() {
        return pathGenerator;
    }

    public ConfigInitializeHandler<Field> fieldInitializer() {
        return fieldInitializer;
    }

    public void fieldInitializer(@NotNull ConfigInitializeHandler<Field> fieldInitializer) {
        this.fieldInitializer = fieldInitializer;
    }

    public ConfigInitializeHandler<Class<? extends Configuration>> classInitializer() {
        return classInitializer;
    }

    public void classInitializer(@NotNull ConfigInitializeHandler<Class<? extends Configuration>> classInitializer) {
        this.classInitializer = classInitializer;
    }

    public void appendFieldInitializer(@NotNull ConfigInitializeHandler<Field> fieldInitializer) {
        this.fieldInitializer = this.fieldInitializer.andThen(fieldInitializer);
    }

    public void appendClassInitializer(@NotNull ConfigInitializeHandler<Class<? extends Configuration>> classInitializer) {
        this.classInitializer = this.classInitializer.andThen(classInitializer);
    }

    public <T, A extends Annotation> void registerAnnotation(@NotNull Class<A> annotation,
                                                             @NotNull ConfigurationMetadata<T> metadata,
                                                             @NotNull Function<A, T> extractor) {
        appendFieldInitializer((provider, path, field) -> {
            A data = field.getAnnotation(annotation);
            if (data == null) return;
            provider.metadata(path).setIfAbsent(metadata, extractor.apply(data));
        });
        appendClassInitializer((provider, path, clazz) -> {
            A data = clazz.getAnnotation(annotation);
            if (data == null) return;
            provider.metadata(path).setIfAbsent(metadata, extractor.apply(data));
        });
    }


    public @Nullable String getFieldPath(ConfigurationProvider<?> provider, @Nullable String parentPath, @NotNull Field field) {
        return pathGenerator.getFieldPath(provider, parentPath, field);
    }

    public @Nullable String getClassPath(ConfigurationProvider<?> provider, @Nullable String parentPath,
                                         @NotNull Class<?> clazz, @Nullable Field clazzField) {
        return pathGenerator.getClassPath(provider, parentPath, clazz, clazzField);
    }

    public void initialize(ConfigurationProvider<?> provider, @NotNull Configuration config) throws Exception {
        initializeInstance(provider, config, null, null);
        if (provider.options().get(StandardOptions.SET_DEFAULTS)) provider.save();
    }

    public void initialize(ConfigurationProvider<?> provider, @NotNull Class<? extends Configuration> clazz) throws Exception {
        initializeStaticClass(provider, clazz, null, null);
        if (provider.options().get(StandardOptions.SET_DEFAULTS)) provider.save();
    }


    // 针对实例类的初始化方法
    protected void initializeInstance(@NotNull ConfigurationProvider<?> provider,
                                      @NotNull Configuration root, @Nullable String parentPath, @Nullable Field configField) {
        String path = getClassPath(provider, parentPath, root.getClass(), configField);
        try {
            this.classInitializer.whenInitialize(provider, path, root.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Arrays.stream(root.getClass().getDeclaredFields()).forEach(field -> initializeField(provider, root, field, path));
    }

    // 针对静态类的初始化方法
    @SuppressWarnings("unchecked")
    protected void initializeStaticClass(@NotNull ConfigurationProvider<?> provider,
                                         @NotNull Class<?> clazz, @Nullable String parentPath, @Nullable Field configField) {
        if (!Configuration.class.isAssignableFrom(clazz)) return; // 只解析继承了 ConfigurationRoot 的类

        String path = getClassPath(provider, parentPath, clazz, configField);

        try {
            this.classInitializer.whenInitialize(provider, path, (Class<? extends Configuration>) clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Field field : clazz.getDeclaredFields()) {
            initializeField(provider, clazz, field, path);
        }

        if (provider.options().get(StandardOptions.LOAD_SUB_CLASSES)) {
            Class<?>[] classes = clazz.getDeclaredClasses();
            for (int i = classes.length - 1; i >= 0; i--) {   // 逆向加载，保持顺序。
                initializeStaticClass(provider, classes[i], path, null);
            }
        }
    }

    protected void initializeField(@NotNull ConfigurationProvider<?> provider,
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
                value.initialize(provider, path);
                try {
                    this.fieldInitializer.whenInitialize(provider, path, field);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

}
