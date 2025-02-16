package cc.carm.lib.configuration.value.text;

import cc.carm.lib.configuration.value.text.data.TextContents;
import cc.carm.lib.configuration.value.text.function.TextDispatcher;
import org.jetbrains.annotations.NotNull;

public class PreparedText<MSG, RECEIVER> extends TextDispatcher<MSG, RECEIVER, PreparedText<MSG, RECEIVER>> {

    public PreparedText(@NotNull TextContents texts, @NotNull String... params) {
        super(texts, params);
    }

    @Override
    public PreparedText<MSG, RECEIVER> self() {
        return this;
    }
}
