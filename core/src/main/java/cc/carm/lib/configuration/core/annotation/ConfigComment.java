package cc.carm.lib.configuration.core.annotation;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigComment {

    @NotNull
    String[] value() default "";

    /**
     * 首行换行，即会在注释开始前进行一次换行，与上方配置分离，优化观感。
     * 如：
     * <blockquote><pre>
     * some-key: "SomeValue"
     *
     * # 注释第一行
     * # 注释第二行
     * startWrap: true
     * </pre></blockquote>
     *
     * @return 是否在结尾添加换行符
     */
    boolean startWrap() default true;

    /**
     * 末尾换行，即会在注释结束后进行一次换行，如：
     * <blockquote><pre>
     * # 注释第一行
     * # 注释第二行
     *
     * endWrap: true
     * </pre></blockquote>
     * <p> 该功能可用于编写配置文件的顶部注释。
     *
     * @return 是否在结尾添加换行符
     */
    boolean endWrap() default false;
}
