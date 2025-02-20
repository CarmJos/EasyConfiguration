package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ShadedSection extends MapSection<ShadedSection> {

    private final @Nullable Supplier<@Nullable ConfigureSection> templateSupplier;

    public ShadedSection(@Nullable ShadedSection parent, @Nullable Supplier<ConfigureSection> templateSupplier) {
        super(parent);
        this.templateSupplier = templateSupplier;
    }

    public ShadedSection(@NotNull Map<?, ?> data, @Nullable ShadedSection parent, @Nullable Supplier<ConfigureSection> templateSupplier) {
        super(parent);
        this.templateSupplier = templateSupplier;
        migrate(data);
    }

    @Override
    public @NotNull ShadedSection self() {
        return this;
    }

    @Override
    protected @NotNull ShadedSection createChild(@NotNull Map<?, ?> data) {
        return new ShadedSection(data, this, null);
    }

    protected @Nullable ConfigureSection template() {
        return templateSupplier != null ? templateSupplier.get() : null;
    }

    @Nullable
    protected <T> T template(@NotNull Function<ConfigureSection, T> function) {
        return template(null, function);
    }

    @Contract("!null, _ -> !null")
    protected <T> T template(@Nullable T defaults, @NotNull Function<ConfigureSection, T> function) {
        return Optional.ofNullable(template()).map(function).orElse(defaults);
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        // 优先从当前section获取
        Object value = super.get(path);
        if (value != null) {
            return wrapNestedValues(path, value);
        }

        // 当前section无数据时从模板获取
        if (templateSupplier != null) {
            Object templateValue = template((template) -> template.get(path));
            return wrapTemplateNestedValues(path, templateValue);
        }

        return null;
    }

    private Object wrapNestedValues(String path, Object value) {
        if (value instanceof ConfigureSection) {
            return new ShadedSection(
                    ((ConfigureSection) value).getValues(false),
                    this, () -> template(s -> s.getSection(path))
            );
        }
        return value;
    }

    private Object wrapTemplateNestedValues(String path, Object templateValue) {
        if (templateValue instanceof ConfigureSection) {
            // 模板子Section作为新Section的模板
            return new ShadedSection(
                    ((ConfigureSection) templateValue).getValues(false),
                    this,
                    ((ConfigureSection) templateValue) // 模板继承
            );
        } else if (templateValue instanceof List) {
            // 处理模板列表中的嵌套结构
            return processTemplateList(path, (List<?>) templateValue);
        }
        return templateValue;
    }

    private List<Object> processTemplateList(String path, List<?> list) {
        List<Object> processed = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof ConfigureSection) {
                processed.add(new ShadedSection(
                        ((ConfigureSection) item).getValues(false), this,
                        (ConfigureSection) item
                ));
            } else {
                processed.add(item);
            }
        }
        return processed;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        // 获取模板值并深度比较
        Object templateValue = templateSupplier != null ? templateSupplier.get(path) : null;
        Object processedValue = preprocessValue(path, value);

        if (isDeepEqual(processedValue, templateValue)) {
            return; // 与模板一致则跳过
        }

        super.set(path, processedValue);
    }

    private Object preprocessValue(String path, Object value) {
        if (value instanceof Map) {
            return new ShadedSection(
                    (Map<?, ?>) value, this,
                    () -> template(s -> s.getSection(path))
            );
        } else if (value instanceof List) {
            return processList(path, (List<?>) value);
        }
        return value;
    }

    private boolean isDeepEqual(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;

        if (a instanceof ConfigureSection && b instanceof ConfigureSection) {
            return ((ConfigureSection) a).getValues(true)
                    .equals(((ConfigureSection) b).getValues(true));
        }

        return Objects.equals(a, b);
    }

}