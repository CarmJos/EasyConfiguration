package cc.carm.lib.configuration.adapter.strandard;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.source.section.ConfigureSection;
import org.jetbrains.annotations.NotNull;

import static cc.carm.lib.configuration.adapter.strandard.PrimitiveAdapter.*;

public interface StandardAdapters {

    @NotNull PrimitiveAdapter<?>[] PRIMITIVES = new PrimitiveAdapter[]{
            ofString(), ofBoolean(), ofBooleanType(), ofCharacter(), ofCharacterType(),
            ofInteger(), ofIntegerType(), ofLong(), ofLongType(), ofDouble(), ofDoubleType(),
            ofFloat(), ofFloatType(), ofShort(), ofShortType(), ofByte(), ofByteType()
    };

    @NotNull ValueAdapter<Enum<?>> ENUMS = PrimitiveAdapter.ofEnum();

    @NotNull ValueAdapter<ConfigureSection> SECTIONS = new ValueAdapter<>(
            ValueType.of(ConfigureSection.class),
            (provider, type, value) -> value,
            (provider, type, value) -> {
                if (value instanceof ConfigureSection) {
                    return (ConfigureSection) value;
                } else throw new IllegalArgumentException("Value is not a ConfigurationSection");
            }
    );

}
