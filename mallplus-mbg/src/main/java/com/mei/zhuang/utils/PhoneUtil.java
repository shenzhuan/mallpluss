package com.mei.zhuang.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 作者 zscat E-mail: 951449465@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 正则表达式手机号码校验类
 */
public class PhoneUtil {

    private static String REGEX = "^((13[0-9])|(14[0-9])|(15([0-9]))|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
    private static Pattern P = Pattern.compile(REGEX);

    /**
     * 校验手机号
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return Boolean.FALSE;
        }

        Matcher m = P.matcher(phone);
        return m.matches();
    }
}
