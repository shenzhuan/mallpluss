package com.mei.zhuang.util;

import java.util.regex.Pattern;

/**
 * Created by john on 2017/9/10.
 */
public class StringHelper {
    public static String getObjectValue(Object obj){
        return obj==null?"":obj.toString();
    }

    /** 正则表达式：验证手机号 */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";

    /** 正则表达式：验证邮箱 */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**正则表达式：平台账号管理密码校验（6-16位字符，不能包含空格、中文）*/
    public static final String REGEX_PASSWORD="^[^\\s\\u4e00-\\u9fa5]{6,16}$";



    /**
    * @Description:   <p>验证手机号 </p >
    * @author           Demon.Yan
    * @params           mobile：校验手机号
    * @return           校验通过返回true，否则返回false
    * @exception
    * @date             2018/11/27 1:39
    */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
    * @Description:   <p>校验邮箱</p >
    * @author           Demon.Yan
    * @params           email：邮箱
    * @return           校验通过返回true，否则返回false
    * @exception
    * @date             2018/11/27 1:40
    */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }


    /**
     * <p>平台账号管理密码校验</p >
     * @param password
     * @return  校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password){
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    public static boolean isNotBlank(Object obj){
		if(obj==null)
			return false;
		return true;
	}

	public static String toString(Object obj){
		if(obj==null){
			return null;
		}
		return obj.toString();
	}

	public static byte[] getBytes(String obj){
		if(obj==null){
			return null;
		}
		return obj.getBytes();
	}

}
