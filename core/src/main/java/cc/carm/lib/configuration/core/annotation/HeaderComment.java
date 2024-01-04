package cc.carm.lib.configuration.core.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Header Comments.
 * Add a comment to the top of the corresponding configuration for easy reading and viewing.
 * <p>e.g.
 * <blockquote><pre>
 * # The first line of the comment
 * # The second line of the comment
 * foo: "bar"
 * </pre></blockquote>
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderComment {

    /**
     * If the content of the note is 0, it will be treated as a blank line.
     * <p> e.g. <b>{"foo","","bar"}</b>
     * Will be set as
     * <blockquote><pre>
     * # foo
     *
     * # bar
     * foo: "bar"
     * </pre></blockquote>
     *
     * @return The content of this comment
     */
    @NotNull
    String[] value() default "";

}
