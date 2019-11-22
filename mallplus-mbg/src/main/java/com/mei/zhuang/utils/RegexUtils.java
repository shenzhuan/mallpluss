package com.mei.zhuang.utils;

import java.util.regex.Pattern;

/**
 * @Auther: Tiger
 * @Date: 2019-06-12 11:58
 * @Description:
 */
public class RegexUtils {

    public static String BASE_REGEX = "^1\\d{10}$";//纯数字表达式
    public static String W_REGEX = "^\\w+$";//w表达式 字母数字下划线


    public RegexUtils() {

    }

    public RegexUtils(String regex) {
        this.BASE_REGEX = regex;
    }


    /**
     * 根据相应的表达式验证目标表达式的正确性
     *
     * @param target
     * @param regex
     * @return
     */
    public static boolean veryNum(String target, String regex) {
        return Pattern.compile(regex).matcher(target).matches();
    }

    /**
     * 验证数字正确性
     *
     * @return
     */
    public static boolean veryNum(String target) {
        return Pattern.compile(BASE_REGEX).matcher(target).matches();
    }

    public static boolean veryStr(String str, String regex) {
        return Pattern.compile(regex).matcher(str).matches();
    }


}
