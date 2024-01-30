package cc.carm.lib.configuration.adapter.strandard;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unchecked", "rawtypes"})
public class EnumAdapter extends ValueAdapter<String, Enum> {

    public EnumAdapter() {
        super(String.class, Enum.class);
    }

    @Override
    public String serialize(@NotNull ConfigurationProvider<?> provider, @NotNull Enum value) throws Exception {
        return value.name();
    }

    @Override
    public Enum deserialize(@NotNull ConfigurationProvider<?> provider, @NotNull Class<? extends Enum> clazz, @NotNull String data) throws Exception {
        return Enum.valueOf(clazz, data);
    }

    @Override
    public boolean isAdaptedTo(Class<?> clazz) {
        return clazz.isEnum();
    }

}
