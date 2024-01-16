package cc.carm.lib.configuration.source.path;

import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public interface PathGenerator<P extends ConfigurationProvider<P>> {

    @Nullable String getFieldPath(@Nullable String parentPath, @NotNull Field field);

    @Nullable String getClassPath(@Nullable String parentPath,
                                  @NotNull Class<?> clazz, @Nullable Field clazzField);

    /**
     * Get the configuration name of the specified element.
     * Use the naming convention of all lowercase and "-" links.
     *
     * @param name source name
     * @return the final path
     */
    static String covertPathName(String name) {
        return name.replaceAll("[A-Z]", "-$0") // 将驼峰形转换为蛇形;
                .replaceAll("-(.*)", "$1") // 若首字母也为大写，则也会被转换，需要去掉第一个横线
                .replaceAll("_-([A-Z])", "_$1") // 因为命名中可能包含 _，因此需要被特殊处理一下
                .replaceAll("([a-z])-([A-Z])", "$1_$2") // 然后将非全大写命名的内容进行转换
                .replace("-", "") // 移除掉多余的横线
                .replace("_", "-") // 将下划线替换为横线
                .toLowerCase(); // 最后转为全小写
    }

}
