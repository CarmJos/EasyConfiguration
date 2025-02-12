package cc.carm.lib.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The configuration path used to mark the corresponding class or parameter.
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigPath {

    /**
     * The path value of the current configuration.
     * If not set,will generate the path by {@link cc.carm.lib.configuration.source.loader.PathGenerator}.
     *
     * @return The path value of the current configuration
     */
    String value() default "";

    /**
     * Whether to start with the root node.
     * <br>If false, the previous path (if any) to the node is automatically added.
     * <br>If true, the path will be set directly from the root node.
     *
     * @return Whether to start directly from the root path without inheriting the previous path
     */
    boolean root() default false;

}
