package cc.carm.lib.configuration.core.annotation;

import cc.carm.lib.configuration.core.ConfigInitializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记对应类或参数的配置路径
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigPath {

    /**
     * 指定路径的值。
     * 若不指定，则会通过 {@link ConfigInitializer#getPathFromName(String)} 自动生成当前路径的值。
     *
     * @return 路径的值
     */
    String value() default "";

    /**
     * 是否从根节点开始。
     * <br>若为 false，则会自动添加上一个路径(如果有)到本节点的路径。
     * <br>若为 true，则会从根节点开始直接设置本路径。
     *
     * @return 是否不继承上一路径，直接从根路径为开始
     */
    boolean root() default false;

}
