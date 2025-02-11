package cc.carm.lib.configuration.source.loader;

import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.option.StandardOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.function.UnaryOperator;

public class PathGenerator {

    public static PathGenerator of() {
        return of(PathGenerator::covertPathName);
    }

    public static PathGenerator of(UnaryOperator<String> pathConverter) {
        return new PathGenerator(pathConverter);
    }

    protected UnaryOperator<String> pathConverter;

    public PathGenerator(UnaryOperator<String> pathConverter) {
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

    public @Nullable String getFieldPath(@NotNull ConfigurationHolder<?> holder,
                                         @Nullable String parentPath, @NotNull Field field) {
        ConfigPath path = field.getAnnotation(ConfigPath.class);
        if (path == null) return link(holder, parentPath, false, field.getName()); // No annotation, use field name.
        else return link(holder, parentPath, path.root(), select(path.value(), field.getName()));
    }

    public @Nullable String getClassPath(@NotNull ConfigurationHolder<?> holder,
                                         @Nullable String parentPath, @NotNull Class<?> clazz, @Nullable Field clazzField) {
        // For standard path generator, we generate path following by:
        // 1. Check if the class has a ConfigPath annotation, if so, use the root and value as the path.
        // 2. If the class defined as a field, check if the field has a ConfigPath annotation,
        //    and use filed information.
        ConfigPath clazzPath = clazz.getAnnotation(ConfigPath.class);

        if (clazzPath != null) return link(holder, parentPath, clazzPath.root(), clazzPath.value());
        if (clazzField == null) {
            return link(holder, parentPath, false, clazz.getSimpleName()); // No field, use class name.
        }

        ConfigPath fieldPath = clazzField.getAnnotation(ConfigPath.class);
        if (fieldPath == null) return link(holder, parentPath, false, clazzField.getName());
        else return getFieldPath(holder, parentPath, clazzField);
    }

    protected String select(String path, String defaultValue) {
        if (path == null || path.isEmpty()) return defaultValue;
        else return isBlank(path) ? null : path;
    }

    protected @Nullable String link(@NotNull ConfigurationHolder<?> holder,
                                    @Nullable String parent, boolean root, @Nullable String path) {
        if (path == null || path.isEmpty()) return root ? null : parent;
        return root || parent == null ? covertPath(path) : parent + pathSeparator(holder) + covertPath(path);
    }

    public static boolean isBlank(String path) {
        return path == null || path.replace(" ", "").isEmpty();
    }

    public static char pathSeparator(ConfigurationHolder<?> holder) {
        return holder.options().get(StandardOptions.PATH_SEPARATOR);
    }

    /**
     * Get the configuration name of the specified element.
     * Use the naming convention of all lowercase and "-" links.
     * <p>
     * e.g. "SOME_NAME" -> "some-name"
     *
     * @param name source name
     * @return the final path
     */
    public static String covertPathName(String name) {
        return name
                // Replace all uppercase letters with dashes
                .replaceAll("[A-Z]", "-$0")
                // If the first letter is also capitalized,
                // it will also be converted and the first dash will need to be removed
                .replaceAll("-(.*)", "$1")
                // Because the name may contain _, it needs to be treated a little differently
                .replaceAll("_-([A-Z])", "_$1")
                // The content that is not named in all caps is then converted
                .replaceAll("([a-z])-([A-Z])", "$1_$2")
                // Remove any extra horizontal lines
                .replace("-", "")
                // Replace the underscore with a dash
                .replace("_", "-")
                // Finally, convert it to all lowercase
                .toLowerCase();
    }


}
