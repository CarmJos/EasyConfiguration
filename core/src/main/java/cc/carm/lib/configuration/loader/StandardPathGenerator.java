package cc.carm.lib.configuration.loader;

import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.option.ConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.function.UnaryOperator;

public class StandardPathGenerator implements PathGenerator {

    public static StandardPathGenerator of() {
        return of(PathGenerator::covertPathName);
    }

    public static StandardPathGenerator of(UnaryOperator<String> pathConverter) {
        return new StandardPathGenerator(pathConverter);
    }

    protected UnaryOperator<String> pathConverter;

    public StandardPathGenerator(UnaryOperator<String> pathConverter) {
        this.pathConverter = pathConverter;
    }

    public @NotNull UnaryOperator<String> getPathConverter() {
        return pathConverter;
    }

    public void setPathConverter(UnaryOperator<String> pathConverter) {
        this.pathConverter = pathConverter;
    }

    public String covertPath(String name) {
        return pathConverter.apply(name);
    }

    public char pathSeparator(ConfigurationProvider<?> provider) {
        return provider.option(ConfigurationOptions.PATH_SEPARATOR);
    }

    @Override
    public @Nullable String getFieldPath(@NotNull ConfigurationProvider<?> provider,
                                         @Nullable String parentPath, @NotNull Field field) {
        ConfigPath path = field.getAnnotation(ConfigPath.class);
        if (path == null) return link(provider, parentPath, false, field.getName()); // No annotation, use field name.
        else return link(provider, parentPath, path.root(), select(path.value(), field.getName()));
    }

    @Override
    public @Nullable String getClassPath(@NotNull ConfigurationProvider<?> provider,
                                         @Nullable String parentPath, @NotNull Class<?> clazz, @Nullable Field clazzField) {
        // For standard path generator, we generate path following by:
        // 1. Check if the class has a ConfigPath annotation, if so, use the root and value as the path.
        // 2. If the class defined as a field, check if the field has a ConfigPath annotation,
        //    and use filed information.
        ConfigPath clazzPath = clazz.getAnnotation(ConfigPath.class);

        if (clazzPath != null) return link(provider, parentPath, clazzPath.root(), clazzPath.value());
        if (clazzField == null) {
            return link(provider, parentPath, false, clazz.getSimpleName()); // No field, use class name.
        }

        ConfigPath fieldPath = clazzField.getAnnotation(ConfigPath.class);
        if (fieldPath == null) return link(provider, parentPath, false, clazzField.getName());
        else return getFieldPath(provider, parentPath, clazzField);
    }

    protected String select(String path, String defaultValue) {
        if (path == null || path.isEmpty()) return defaultValue;
        else return isBlank(path) ? null : path;
    }

    protected boolean isBlank(String path) {
        return path == null || path.replace(" ", "").isEmpty();
    }

    protected @Nullable String link(@NotNull ConfigurationProvider<?> provider, @Nullable String parent, boolean root, @Nullable String path) {
        if (path == null || path.isEmpty()) return root ? null : parent;
        return root || parent == null ? covertPath(path) : parent + pathSeparator(provider) + covertPath(path);
    }


}
