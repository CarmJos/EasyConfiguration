package cc.carm.lib.configuration.core.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 行内注释，用于给对应配置的所在行添加注释，便于使用者阅读查看。
 * 如：
 * <blockquote><pre>
 * foo: "bar" # 注释内容
 * </pre></blockquote>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InlineComment {

    /**
     * 注释内容，若内容长度为0则不会添加注释
     * <p> 如 <b>"foobar"</b> 将被设定为
     * <blockquote><pre>
     * foo: "bar" # foobar
     * </pre></blockquote>
     *
     * @return 注释内容
     */
    @NotNull
    String value() default "";

}
