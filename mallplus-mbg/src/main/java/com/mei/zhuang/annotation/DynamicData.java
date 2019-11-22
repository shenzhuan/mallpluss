package com.mei.zhuang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 动态数据源注解出现这个注解的方法。
 * john
 */
@Target({ElementType.METHOD})
@Retention(RUNTIME)
@Documented
public @interface DynamicData {
    String value() default "";

    String dbname() default "";

    String schema() default "";
}
