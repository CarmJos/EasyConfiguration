package cc.carm.lib.configuration.value.text.function;

import cc.carm.lib.configuration.value.text.data.TextContents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public abstract class TextParser<RECEIVER, SELF extends TextParser<RECEIVER, SELF>> {

    /**
     * Used to match the message insertion
     * <p>
     * format:
     * <br>- to insert parsed line {prefix}#content-id#{offset-above,offset-down}
     * <br>- to insert original line {prefix}@content-id@{offset-above,offset-down}
     * <br> example:
     * <ul>
     *     <li>{- }#content-id#{1,1}</li>
     *     <li>@content-id@{1,1}</li>
     * </ul>
     */
    public static final @NotNull Pattern INSERT_PATTERN = Pattern.compile(
            "^(?:\\{(?<prefix>.*)})?(?<type>[#@])(?<id>.*)[#@](?:\\{(?<above>-?\\d+)(?:,(?<down>-?\\d+))?})?$"
    );

    protected final @NotNull TextContents texts;

    protected BiFunction<RECEIVER, String, String> parser = (receiver, value) -> value;
    protected String lineSeparator = System.lineSeparator();

    /**
     * Used to store the placeholders of the message
     */
    protected @NotNull Map<String, Object> placeholders = new HashMap<>();
    protected @NotNull UnaryOperator<String> paramBuilder = s -> "%(" + s + ")";
    protected @NotNull String[] params;

    /**
     * Used to store the insertion of the message
     */
    protected @NotNull Map<String, List<String>> insertion = new HashMap<>();
    protected boolean disableInsertion = false;

    protected TextParser(@NotNull TextContents texts, @NotNull String... params) {
        this.texts = texts;
        this.params = params;
    }

    public abstract SELF self();

    /**
     * Disable the insertion of the text.
     * <br> If the insertion is disabled, the text will be parsed directly.
     *
     * @return the current {@link TextParser} instance
     */
    public SELF disableInsertion() {
        this.disableInsertion = true;
        return self();
    }

    /**
     * Enable the insertion of the text.
     *
     * @return the current {@link TextParser} instance
     */
    public SELF enableInsertion() {
        this.disableInsertion = false;
        return self();
    }

    /**
     * Set the line separator for the text.
     *
     * @param lineSeparator the line separator, default is {@link System#lineSeparator()}
     * @return the current {@link TextParser} instance
     */
    public SELF lineSeparator(@NotNull String lineSeparator) {
        this.lineSeparator = lineSeparator;
        return self();
    }

    /**
     * Set all the placeholders for the text.
     * <br> Will override the previous placeholders modifications.
     *
     * @param placeholders the placeholders
     * @return the current {@link TextParser} instance
     */
    public SELF placeholders(@NotNull Map<String, Object> placeholders) {
        this.placeholders = placeholders;
        return self();
    }

    /**
     * Set the placeholders for the text.
     *
     * @param consumer the placeholders
     * @return the current {@link TextParser} instance
     */
    public SELF placeholders(@NotNull Consumer<Map<String, Object>> consumer) {
        consumer.accept(this.placeholders);
        return self();
    }

    /**
     * Set the placeholders for the text.
     *
     * @param values The values to replace the {@link #params(String...)}.
     * @return the current {@link TextParser} instance
     */
    public SELF placeholders(@Nullable Object... values) {
        return placeholders(map -> map.putAll(buildParams(this.paramBuilder, this.params, values)));
    }

    /**
     * Set the placeholder for the text.
     *
     * @param key   the key of the placeholder
     * @param value the value of the placeholder
     * @return the current {@link TextParser} instance
     */
    public SELF placeholder(@NotNull String key, @Nullable Object value) {
        this.placeholders.put(paramBuilder.apply(key), value);
        return self();
    }

    /**
     * Set the params for the text,
     * used for {@link #placeholders(Object...)} to build the placeholders.
     *
     * @param params the params
     * @return the current {@link TextParser} instance
     */
    public SELF params(@NotNull String... params) {
        this.params = params;
        return self();
    }

    /**
     * Insert the specific contents by the id.
     *
     * @param id    the id of the insertion text
     * @param lines the lines to insert
     * @return the current {@link TextParser} instance
     */
    public SELF insert(@NotNull String id, @NotNull String... lines) {
        this.insertion.put(id, Arrays.asList(lines));
        return self();
    }

    /**
     * Insert the specific contents by the id.
     *
     * @param id    the id of the insertion text
     * @param lines the lines to insert
     * @return the current {@link TextParser} instance
     */
    public SELF insert(@NotNull String id, @NotNull List<String> lines) {
        this.insertion.put(id, lines);
        return self();
    }

    /**
     * Insert the specific contents by the id.
     * <br> If the text not found in {@link TextContents#optionalLines()},
     * nothing will happen.
     *
     * @param id the id of the insertion text
     * @return the current {@link TextParser} instance
     */
    public SELF insert(@NotNull String id) {
        List<String> lines = this.texts.optionalLines().get(id);
        if (lines == null) return self();
        else return insert(id, lines);
    }

    /**
     * Set the parser for the text.
     *
     * @param parser The parser
     * @return The current {@link TextParser} instance
     */
    public SELF parser(@NotNull BiFunction<RECEIVER, String, String> parser) {
        this.parser = parser;
        return self();
    }

    /**
     * Parse the texts for the receiver.
     *
     * @param receiver the receiver
     * @return the parsed line
     */
    public List<String> parse(@Nullable RECEIVER receiver) {
        List<String> result = new ArrayList<>();
        handleTexts(receiver, result::add);
        return result;
    }

    /**
     * Parse the texts for the receiver.
     *
     * @param receiver the receiver
     * @param compiler the compiler
     * @param <V>      the type of the message
     * @return the parsed line
     */
    public <V> List<V> parse(@Nullable RECEIVER receiver, @NotNull BiFunction<RECEIVER, String, V> compiler) {
        List<V> result = new ArrayList<>();
        handleTexts(receiver, s -> result.add(compiler.apply(receiver, s)));
        return result;
    }

    /**
     * Parse the texts as a single line for the receiver.
     *
     * @param receiver the receiver
     * @return the parsed line
     */
    public @Nullable String parseLine(@Nullable RECEIVER receiver) {
        if (this.texts.isEmpty()) return null;
        StringBuilder builder = new StringBuilder();
        handleTexts(receiver, s -> builder.append(s).append(this.lineSeparator));
        // Remove the last line separator, if it exists
        if (builder.length() > 0) builder.delete(builder.length() - this.lineSeparator.length(), builder.length());
        return builder.toString();
    }

    /**
     * Parse the texts as a single line for the receiver.
     *
     * @param receiver the receiver
     * @param <V>      the type of the message
     * @return the parsed line
     */
    public <V> @Nullable V parseLine(@Nullable RECEIVER receiver, @NotNull BiFunction<RECEIVER, String, V> compiler) {
        return Optional.ofNullable(parseLine(receiver)).map(s -> compiler.apply(receiver, s)).orElse(null);
    }


    /**
     * Parse the supplied single text for the receiver.
     *
     * @param receiver the receiver
     * @param text     the text to parse
     * @return the parsed text
     */
    protected @Nullable String parseText(@Nullable RECEIVER receiver, @NotNull String text) {
        return this.parser.apply(receiver, setPlaceholders(text, this.placeholders));
    }

    public void handleTexts(@Nullable RECEIVER receiver, @NotNull Consumer<String> lineConsumer) {
        if (this.texts.isEmpty()) return; // Nothing to parse

        if (this.disableInsertion) {
            this.texts.lines().forEach(line -> lineConsumer.accept(parseText(receiver, line)));
            return; // Simple parsed
        }

        for (String line : this.texts.lines()) {
            Matcher matcher = INSERT_PATTERN.matcher(line);
            if (!matcher.matches()) {
                lineConsumer.accept(parseText(receiver, line));
                continue;
            }

            String id = matcher.group("id");
            List<String> values = this.insertion.get(id);
            if (values == null) continue;

            String prefix = matcher.group("prefix");
            String type = matcher.group("type");
            boolean original = type.equals("@");
            int offsetAbove = Optional.ofNullable(matcher.group("above"))
                    .map(Integer::parseInt).orElse(0);
            int offsetDown = Optional.ofNullable(matcher.group("down"))
                    .map(Integer::parseInt).orElse(offsetAbove); // If offsetDown is not set, use offsetAbove

            IntStream.range(0, Math.max(0, offsetAbove)).mapToObj(i -> "").forEach(lineConsumer);
            String prefixContent = Optional.ofNullable(prefix).map(p -> parseText(receiver, p)).orElse("");
            if (original) {
                values.stream().map(value -> prefixContent + value).forEach(lineConsumer);
            } else {
                values.stream().map(value -> prefixContent + parseText(receiver, value)).forEach(lineConsumer);
            }
            IntStream.range(0, Math.max(0, offsetDown)).mapToObj(i -> "").forEach(lineConsumer);
        }
    }

    public static String setPlaceholders(@NotNull String messages,
                                         @NotNull Map<String, Object> placeholders) {
        if (messages.isEmpty()) return messages;
        String parsed = messages;
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            parsed = parsed.replace(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
        }
        return parsed;
    }

    public static Map<String, Object> buildParams(@NotNull UnaryOperator<String> paramBuilder,
                                                  @Nullable String[] params, @Nullable Object[] values) {
        Map<String, Object> map = new HashMap<>();
        if (params == null || params.length == 0) return map;
        for (int i = 0; i < params.length; i++) {
            map.put(paramBuilder.apply(params[i]), (values != null && values.length > i) ? values[i] : "?");
        }
        return map;
    }

}
