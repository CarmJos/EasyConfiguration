package cc.carm.lib.configuration.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

}
