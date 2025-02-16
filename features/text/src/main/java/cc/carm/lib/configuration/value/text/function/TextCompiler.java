package cc.carm.lib.configuration.value.text.function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;

public abstract class TextCompiler<MSG, RECEIVER, SELF extends TextCompiler<MSG, RECEIVER, SELF>> extends TextParser<RECEIVER, SELF> {

    protected BiFunction<RECEIVER, String, MSG> compiler = (receiver, value) -> null;

    /**
     * Set the text compiler.
     *
     * @param compiler The text compiler.
     * @return The current {@link TextCompiler} instance.
     */
    public SELF compiler(@NotNull BiFunction<RECEIVER, String, MSG> compiler) {
        this.compiler = compiler;
        return self();
    }

    /**
     * Compile the text for specific receiver.
     *
     * @param receiver The receiver.
     * @return The compiled text.
     * @see #parse(Object, BiFunction)
     */
    public @NotNull List<MSG> compile(@Nullable RECEIVER receiver) {
        return parse(receiver, this.compiler);
    }

    /**
     * Compile the singleton text for specific receiver.
     *
     * @param receiver The receiver.
     * @return The compiled text.
     * @see #parseLine(Object, BiFunction)
     */
    public @Nullable MSG compileLine(@Nullable RECEIVER receiver) {
        return parseLine(receiver, this.compiler);
    }
}
