package com.mei.zhuang.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/*******************************************************************************
 * AES加解密算法
 * 2016.07.22
 * @author 王学明
 * aes 128位 cbc 算法
 * HTML的&lt; &gt;&amp;&quot;&copy;&nbsp;分别是<，>，&，"，©;空格的转义字符
 */

public class AESUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtil.class);




    public static void main(final String[] args) {
        //String str = "{\"code\":\"1710121851401851\"}";
        String str = "{\"distributeCode\":\"0703111941451358\",\"distributeTarget\":\"18141914966\",\"num\":\"2\"}";
        String str1 = "{\"phone:\"15620732549\",ip:\"111.200.59.42\"}";


    }
}
