package cc.carm.lib.configuration.value.text;

import cc.carm.lib.configuration.value.text.data.TextContents;
import cc.carm.lib.configuration.value.text.function.TextDispatcher;
import org.jetbrains.annotations.NotNull;

public class PreparedText<MSG, RECEIVER> extends TextDispatcher<MSG, RECEIVER, PreparedText<MSG, RECEIVER>> {

    protected final @NotNull TextContents texts;

    public PreparedText(@NotNull TextContents texts, @NotNull String... params) {
        this.params = params;
        this.texts = texts;
    }

    @Override
    public TextContents texts() {
        return this.texts;
    }

    @Override
    public PreparedText<MSG, RECEIVER> self() {
        return this;
    }

    public PreparedText<MSG, RECEIVER> insert(@NotNull String key,
                                              @NotNull ConfiguredText<?, RECEIVER> message,
                                              @NotNull Object... values) {
        return insert(key, receiver -> message.parse(receiver, values));
    }

}
