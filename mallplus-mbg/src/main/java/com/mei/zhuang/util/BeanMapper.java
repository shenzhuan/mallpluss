package com.mei.zhuang.util;

import org.apache.commons.compress.utils.Lists;
import org.dozer.DozerBeanMapper;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 * <p>
 * 1. 持有Mapper的单例.
 * 2. 返回值类型转换.
 * 3. 批量转换Collection中的所有对象.
 * 4. 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.
 */
public final class BeanMapper {

    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * 基于Dozer转换对象的类型.
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        if (source == null) {
            return null;
        }
        return dozer.map(source, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    public static <T> List<T> mapList(Collection<?> sourceList, Class<T> destinationClass) {
        if (sourceList == null) return null;
        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中.
     */
    public static void copy(Object source, Object destinationObject) {
        if (source == null || destinationObject == null) {
            return;
        }
        dozer.map(source, destinationObject);
    }

    public static Map<String, Object> toMap(Object o) {
        Map<String, Object> result = new HashMap<>();
        try {
            Field[] declaredFields = o.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.getName().equals("serialVersionUID")) {
                    continue;
                }
                field.setAccessible(true);
                if (field.get(o) != null) {
                    result.put(field.getName(), field.get(o));
                }
            }
            return result;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
