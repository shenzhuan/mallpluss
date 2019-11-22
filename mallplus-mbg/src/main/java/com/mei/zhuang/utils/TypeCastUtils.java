package com.mei.zhuang.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: Tiger
 * @Date: 2019-05-07 10:33
 * @Description:
 */
public class TypeCastUtils {

    /**
     * String字符串转成List<Long>数据格式
     * String str = "1,2,3,4,5,6" -> List<Long> listLong [1,2,3,4,5,6];
     * 默认 ， 分隔
     *
     * @param strArr
     * @return
     */
    public static List<Long> stringToLongList(String strArr) {
        return Arrays.stream(strArr.split(","))
                .map(s -> Long.parseLong(s.trim()))
                .collect(Collectors.toList());
    }

    /**
     * String字符串转成List<Long>数据格式
     * String str = "1,2,3,4,5,6" -> List<Long> listLong [1,2,3,4,5,6];
     *
     * @param strArrm
     * @param format
     * @return
     */
    public static List<Long> stringToLongList(String strArrm, String format) {
        return Arrays.stream(strArrm.split(format))
                .map(s -> Long.parseLong(s.trim()))
                .collect(Collectors.toList());
    }

}
