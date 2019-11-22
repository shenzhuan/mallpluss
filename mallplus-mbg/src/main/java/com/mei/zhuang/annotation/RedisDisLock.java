package com.mei.zhuang.annotation;

import java.lang.annotation.*;

/**
 * 锁的注解
 *
 * @author QIPE001
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisDisLock {

    /**
     * 方法的名称
     */
    String value() default "未定义方法描述";

    /**
     * 加锁的字段的key值
     */
    String key() default "";

    /**
     * 加锁字段的前缀
     */
    String prefix() default "";

    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待
     */
    int expireMsecs() default 60 * 1000;

    /**
     * 锁等待时间，防止线程饥饿
     */
    int timeoutMsecs() default 10 * 1000;
}
