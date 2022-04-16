package cc.carm.lib.configuration.core.source;

import cc.carm.lib.configuration.core.function.ConfigValueParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConfigurationWrapper extends ConfigurationReader{

    @Override
    default ConfigurationWrapper getWrapper() {
        return this;
    }

    @NotNull
    Set<String> getKeys(boolean deep);

    @NotNull
    Map<String, Object> getValues(boolean deep);

    void set(@NotNull String path, @Nullable Object value);

    boolean contains(@NotNull String path);

    default <T> boolean isType(@NotNull String path, @NotNull Class<T> typeClass) {
        return typeClass.isInstance(get(path));
    }

    @Nullable Object get(@NotNull String path);

    default @Nullable <T> T get(@NotNull String path, @NotNull Class<T> clazz) {
        return get(path, null, clazz);
    }

    default @Nullable <T> T get(@NotNull String path, @NotNull ConfigValueParser<Object, T> parser) {
        return get(path, null, parser);
    }

    @Contract("_,!null,_->!null")
    default @Nullable <T> T get(@NotNull String path, @Nullable T defaultValue, @NotNull Class<T> clazz) {
        return get(path, defaultValue, ConfigValueParser.castObject(clazz));
    }

    @Contract("_,!null,_->!null")
    default @Nullable <T> T get(@NotNull String path, @Nullable T defaultValue,
                                @NotNull ConfigValueParser<Object, T> parser) {
        Object value = get(path);
        if (value != null) {
            try {
                return parser.parse(value, defaultValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    boolean isList(@NotNull String path);

    @Nullable List<?> getList(@NotNull String path);

    boolean isConfigurationSection(@NotNull String path);

    @Nullable
    ConfigurationWrapper getConfigurationSection(@NotNull String path);


}
