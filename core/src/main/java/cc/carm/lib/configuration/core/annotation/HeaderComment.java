package cc.carm.lib.configuration.core.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 顶部注释，用于给对应配置的顶部添加注释，便于使用者阅读查看。
 * <p>如：
 * <blockquote><pre>
 * # 注释第一行
 * # 注释第二行
 * foo: "bar"
 * </pre></blockquote>
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderComment {

    /**
     * 注释内容，若内容长度为0则会视为一个空行。
     * <p> 如 <b>{"foo","","bar"}</b>
     * 会被添加为
     * <blockquote><pre>
     * # foo
     *
     * # bar
     * foo: "bar"
     * </pre></blockquote>
     *
     * @return 注释内容
     */
    @NotNull
    String[] value() default "";

}
