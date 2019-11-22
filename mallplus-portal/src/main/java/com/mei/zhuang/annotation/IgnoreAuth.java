package com.mei.zhuang.annotation;

/**
 * 忽略Token验证
 *
 * @author zscat
 * @email 951449465@qq.com
 * @date 2017-03-23 15:44
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {

}
