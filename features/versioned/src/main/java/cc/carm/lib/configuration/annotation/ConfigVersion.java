package cc.carm.lib.configuration.annotation;

import org.jetbrains.annotations.Range;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The version of specific {@link cc.carm.lib.configuration.value.ConfigValue}.
 * <br> Used for versioning target field for rewrite/upgrade.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigVersion {

    /**
     * The version of the configuration field.
     *
     * @return the version of the configuration field
     */
    @Range(from = 0, to = 65535)
    int value() default 0;

}
