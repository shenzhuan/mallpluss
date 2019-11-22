package com.mei.zhuang.annotation;

import java.lang.annotation.*;

/**
 * @Auther: Tiger
 * @Date: 2019-06-10 10:58
 * @Description:
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldText {
    /**
     * <p>
     * 属性中文注释
     * </p>
     */
    String value() default "";

}
