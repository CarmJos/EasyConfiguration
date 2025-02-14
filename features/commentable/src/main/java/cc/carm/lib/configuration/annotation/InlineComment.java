package cc.carm.lib.configuration.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Inline comments,
 * add comments to the rows of the corresponding configurations for easy reading and viewing.
 * e.g.
 * <blockquote><pre>
 * foo: "bar" # Comment Contents
 * </pre></blockquote>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(InlineComments.class)
public @interface InlineComment {

    /**
     * If the content length is 0, the comment will not be added.
     * <p> e.g. <b>"foobar"</b> will be set
     * <blockquote><pre>
     * foo: "bar" # foobar
     * </pre></blockquote>
     *
     * @return The content of this comment
     */
    @NotNull
    String value() default "";

    /**
     * If the regex is not empty, the comment will be added to
     * all sub paths if the regex matches the value.
     * If the regex is empty, the comment will be added to the current path.
     * <p> e.g. for section, set <b>{"^foo", "*", "bar"}</b> will be set like
     * <blockquote><pre>
     *   section:
     *     foo:
     *       some:
     *         lover: "bar" <- not matched so no comments
     *         bar: "foobar" # Comment Contents
     *       other:
     *         bar: "foobar" # Comment Contents
     * </pre></blockquote>
     *
     * @return The path regexes of this comment
     */
    @NotNull
    String[] regex() default {};

}
