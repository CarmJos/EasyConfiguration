package cc.carm.lib.configuration.adapter.strandard;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.section.ConfigureSection;

public interface StandardAdapters {

    ValueAdapter<ConfigureSection> SECTION_ADAPTER = new ValueAdapter<>(
            ValueType.of(ConfigureSection.class),
            (provider, type, value) -> value,
            (provider, type, value) -> {
                if (value instanceof ConfigureSection) {
                    return (ConfigureSection) value;
                } else throw new IllegalArgumentException("Value is not a ConfigurationSection");
            }
    );

}
