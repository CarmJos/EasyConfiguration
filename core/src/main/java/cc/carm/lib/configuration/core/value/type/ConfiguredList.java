package cc.carm.lib.configuration.core.value.type;

import cc.carm.lib.configuration.core.builder.list.ConfigListBuilder;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConfiguredList<V> extends CachedConfigValue<List<V>> {

    public static <V> @NotNull ConfigListBuilder<V> builder(@NotNull Class<V> valueClass) {
        return builder().asList(valueClass);
    }

    protected final @NotNull Class<V> valueClass;

    protected final @NotNull ConfigDataFunction<Object, V> parser;
    protected final @NotNull ConfigDataFunction<V, Object> serializer;

    public ConfiguredList(@Nullable ConfigurationProvider<?> provider,
                          @Nullable String sectionPath, @Nullable ConfigCommentInfo comments,
                          @NotNull Class<V> valueClass, @Nullable List<V> defaultValue,
                          @NotNull ConfigDataFunction<Object, V> parser,
                          @NotNull ConfigDataFunction<V, Object> serializer) {
        super(provider, sectionPath, comments, defaultValue);
        this.valueClass = valueClass;
        this.parser = parser;
        this.serializer = serializer;
    }

    @Override
    public @NotNull List<V> get() {
        if (isExpired()) { // 已过时的数据，需要重新解析一次。
            List<V> list = new ArrayList<>();
            List<?> data = getConfiguration().getList(getConfigPath());
            if (data == null || data.isEmpty()) return useOrDefault(list);
            for (Object dataVal : data) {
                if (dataVal == null) continue;
                try {
                    list.add(parser.parse(dataVal));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return updateCache(list);
        } else if (getCachedValue() != null) return getCachedValue();
        else if (getDefaultValue() != null) return getDefaultValue();
        else return new ArrayList<>();
    }

    @Override
    public void set(@Nullable List<V> value) {
        updateCache(value);
        if (value == null) setValue(null);
        else {
            List<Object> data = new ArrayList<>();
            for (V val : value) {
                if (val == null) continue;
                try {
                    data.add(serializer.parse(val));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            setValue(data);
        }
    }


}
