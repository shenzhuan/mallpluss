package com.mei.zhuang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 需要被忽略动态数据源的方法
 * john
 */
@Target({ElementType.METHOD})
@Retention(RUNTIME)
@Documented
public @interface IgnoreDynamicData {
    String value() default "";
}
