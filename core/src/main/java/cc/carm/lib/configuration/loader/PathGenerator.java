package cc.carm.lib.configuration.loader;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public interface PathGenerator {

    @Nullable String getFieldPath(@NotNull ConfigurationProvider<?> provider,
                                  @Nullable String parentPath, @NotNull Field field);

    @Nullable String getClassPath(@NotNull ConfigurationProvider<?> provider,
                                  @Nullable String parentPath, @NotNull Class<?> clazz, @Nullable Field clazzField);

    /**
     * Get the configuration name of the specified element.
     * Use the naming convention of all lowercase and "-" links.
     *
     * @param name source name
     * @return the final path
     */
    static String covertPathName(String name) {
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
