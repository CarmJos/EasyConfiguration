package cc.carm.lib.configuration.source.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RawMapSection extends MapSection<RawMapSection> {
    
    protected RawMapSection(@NotNull Map<?, ?> raw, @Nullable RawMapSection parent) {
        super(parent);
        migrate(raw);
    }

    @Override
    public @NotNull RawMapSection self() {
        return this;
    }

    @Override
    protected @NotNull RawMapSection createChild(@NotNull Map<?, ?> data) {
        return new RawMapSection(data, this);
    }

}
