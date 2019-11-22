package com.mei.zhuang.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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


    // 加密
    public static String encrypt(final String sSrc, final String sKey, final String ivStr) {
        if (sKey == null) {
            LOGGER.info("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            LOGGER.info("Key长度不是16位");
            return null;
        }
        final byte[] raw = sKey.getBytes();
        final SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        try {
            byte[] encrypted = new byte[0];

            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            encrypted = cipher.doFinal(sSrc.getBytes());
            return new BASE64Encoder().encode(encrypted)
                    .replaceAll("\r\n", "")
                    .replaceAll("\n", "");

        } catch (final Exception e) {
            LOGGER.error("encrypt is error", e);
            return null;
        }

    }


    // 解密
    public static String decrypt(final String sSrc, final String sKey, final String ivStr) {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                LOGGER.info("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                LOGGER.info("Key长度不是16位");
                return null;
            }
            final byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            final SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            final byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);

            final byte[] original = cipher.doFinal(encrypted1);
            return new String(original);

        } catch (final Exception ex) {
            LOGGER.error("decrypt is error", ex);
            return null;
        }
    }


    public static void main(final String[] args) {
        //String str = "{\"code\":\"1710121851401851\"}";
        String str = "{\"distributeCode\":\"0703111941451358\",\"distributeTarget\":\"18141914966\",\"num\":\"2\"}";
        String str1 = "{\"phone:\"15620732549\",ip:\"111.200.59.42\"}";
        String encrypt = encrypt(str1, "1234567890abcdef", "1234567890abcdef");
        System.out.println("加密:" + encrypt);
        //推送设备状态变化信息
        System.out.println("解密 = [" + decrypt("+vMJusBjgmSnXGVlrI5Q1IxDJJAtzcPN2RurzSkQTjrgqgSnBAx3numZe7hvmMqQYfHvuGXM5nXbN5Uwel1BsSZhJ2MrwJKAcdkJjD28ovpz2fDnINbVk0PZujWlxrGFePGV0QdOp8EckrxztpcZ9KTDxtBm8pdqKWGM6l76qVXRIMCHlQyEUHSlZmTwll1XviJ9ufrNssgl7Xb/hsIDoCWKp+T8NMNwWrwlJDHHW/byb7w6JyRkWCw/1CC7GCsrX49kJoyHKU2rRPNPJfVvew==", "1234567890abcdef", "1234567890abcdef") + "]");
        System.out.println("解密 = [" + decrypt(encrypt, "1234567890abcdef", "1234567890abcdef") + "]");

    }
}
