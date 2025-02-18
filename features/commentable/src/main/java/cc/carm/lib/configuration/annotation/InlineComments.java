package cc.carm.lib.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InlineComments {

    /**
     * Multiple inline comments support.
     *
     * @return inline comment contents.
     * @see InlineComment
     */
    InlineComment[] value();

}
