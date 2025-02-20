package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShadedSection extends MapSection<ShadedSection> {
    private final ConfigureSection template;

    // 构造方法
    public ShadedSection(@Nullable ShadedSection parent, @Nullable ConfigureSection template) {
        super(parent);
        this.template = template;
    }

    public ShadedSection(@NotNull Map<?, ?> data, @Nullable ShadedSection parent, @Nullable ConfigureSection template) {
        super(parent);
        this.template = template;
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

    @Override
    public @Nullable Object get(@NotNull String path) {
        // 优先从当前section获取
        Object value = super.get(path);
        if (value != null) {
            return wrapNestedValues(path, value);
        }

        // 当前section无数据时从模板获取
        if (template != null) {
            Object templateValue = template.get(path);
            return wrapTemplateNestedValues(path, templateValue);
        }

        return null;
    }

    private Object wrapNestedValues(String path, Object value) {
        if (value instanceof ConfigureSection) {
            // 包装子Section，继承当前模板的对应子Section
            ConfigureSection templateChild = template != null ? template.getSection(path) : null;
            return new ShadedSection(
                    ((ConfigureSection) value).getValues(false),
                    this,
                    templateChild
            );
        } else if (value instanceof List) {
            // 处理列表中的嵌套结构
            return processList(path, (List<?>) value);
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

    private List<Object> processList(String path, List<?> list) {
        List<Object> processed = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof Map) {
                processed.add(new ShadedSection(
                        (Map<?, ?>) item, this,
                        template != null ? template.getSection(path) : null
                ));
            } else if (item instanceof ConfigureSection) {
                processed.add(new ShadedSection(
                        ((ConfigureSection) item).getValues(false), this,
                        template != null ? template.getSection(path) : null
                ));
            } else {
                processed.add(item);
            }
        }
        return processed;
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
        Object templateValue = template != null ? template.get(path) : null;
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
                    template != null ? template.getSection(path) : null
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
        } else if (a instanceof List && b instanceof List) {
            List<?> listA = (List<?>) a;
            List<?> listB = (List<?>) b;
            if (listA.size() != listB.size()) return false;
            for (int i = 0; i < listA.size(); i++) {
                if (!isDeepEqual(listA.get(i), listB.get(i))) return false;
            }
            return true;
        }
        return a.equals(b);
    }
}