package com.mei.zhuang.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Base64;

public class SmsUtils {
    //生成MD5
    public static String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象
            byte[] messageByte = message.getBytes("UTF-8");
            byte[] md5Byte = md.digest(messageByte);              // 获得MD5字节数组,16*8=128位
            md5 = bytesToHex(md5Byte);                            // 转换为16进制字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    // 二进制转十六进制
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }

    public static void sendMsg(String apiUser, String apiPwd, String mobile, String content) {
        String url = "http://api.shgmnets.com/sms/send";//地址
        apiPwd = getMD5(apiPwd);
        URL targetUrl = null;
        BufferedReader reader = null;
        try {
            url += "?user=" + apiUser + "&pwd=" + apiPwd + "&mobile=" + mobile;

            content = Base64.getUrlEncoder().encodeToString(content.getBytes("UTF-8"));
            content = URLEncoder.encode(content, "utf-8");

            content = content.replace("=", "");
            url += "&msg=" + content;
            targetUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 获取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String responseLine;
            while ((responseLine = reader.readLine()) != null) {
                System.out.println(responseLine);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        String url = "http://api.shgmnets.com/sms/send";//地址
        String apiUser = "xxglgfscapi";//api账号名
        String apiPwd = "ehSuYy";//密码
        apiPwd = getMD5(apiPwd);
        URL targetUrl = null;
        BufferedReader reader = null;
        String content = "test发送内容";
        try {

            url += "?user=" + apiUser + "&pwd=" + apiPwd + "&mobile=13146587722";

            content = Base64.getUrlEncoder().encodeToString(content.getBytes("UTF-8"));
            content = URLEncoder.encode(content, "utf-8");

            content = content.replace("=", "");
            url += "&msg=" + content;
            targetUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 获取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String responseLine;
            while ((responseLine = reader.readLine()) != null) {
                System.out.println(responseLine);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
