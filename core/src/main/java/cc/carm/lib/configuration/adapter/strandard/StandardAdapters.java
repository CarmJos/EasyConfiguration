package cc.carm.lib.configuration.adapter.strandard;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.section.ConfigurationSection;

public interface StandardAdapters {

    ValueAdapter<ConfigurationSection> SECTION_ADAPTER = new ValueAdapter<>(
            ValueType.of(ConfigurationSection.class),
            (provider, type, value) -> value,
            (provider, type, value) -> {
                if (value instanceof ConfigurationSection) {
                    return (ConfigurationSection) value;
                } else throw new IllegalArgumentException("Value is not a ConfigurationSection");
            }
    );

}
