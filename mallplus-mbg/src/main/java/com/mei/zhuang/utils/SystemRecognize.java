package com.mei.zhuang.utils;

import java.util.Properties;

/**
 * @Auther: Tiger
 * @Date: 2019-06-14 14:01
 * @Description:
 */
public class SystemRecognize {
    public static Properties props;

    static{
        props = System.getProperties();
    }

    /**
     * 获取系统的名称
     * @return
     */
    public static String getOSName(){
        return props.getProperty("os.name");
    }

    /**
     * 获取系统的架构
     * @return
     */
    public static String getOSArch(){
        return props.getProperty("os.arch");
    }

    /**
     * 获取系统的文件分隔符
     * @return
     */
    public static String getFileSeparator(){
        return props.getProperty("file.separator");
    }

    /**
     * 获取系统的属性值
     * @return
     */
    public static String getValue(String propertyName){
        return props.getProperty(propertyName);
        // return System.getProperty(propertyName);
    }


}
