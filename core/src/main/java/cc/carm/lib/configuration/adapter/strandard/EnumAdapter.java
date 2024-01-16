package cc.carm.lib.configuration.adapter.strandard;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unchecked", "rawtypes"})
public class EnumAdapter<P extends ConfigurationProvider> extends ValueAdapter<P, String, Enum> {

    public EnumAdapter() {
        super(String.class, Enum.class);
    }

    @Override
    public String serialize(@NotNull P provider, @NotNull Enum value) throws Exception {
        return value.name();
    }

    @Override
    public Enum deserialize(@NotNull P provider, @NotNull Class<? extends Enum> clazz, @NotNull String data) throws Exception {
        return Enum.valueOf(clazz, data);
    }

    @Override
    public boolean isAdapterOf(Class<?> clazz) {
        return clazz.isEnum();
    }

}
