package com.mei.zhuang.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUrlConnection {

   /* 以post或get方式调用对方接口方法，
    @param pathUrl*/

    public static JSONObject doPostOrGet(String pathUrl, String data){
        OutputStreamWriter out = null;
        BufferedReader br = null;
        String result = "";
        JSONObject jsonObject = null;
        try {
            URL url = new URL(pathUrl);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //请求方式
            conn.setRequestMethod("POST");
            //conn.setRequestMethod("GET");

            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            //DoOutput设置是否向httpUrlConnection输出，DoInput设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            conn.setDoOutput(true);
            conn.setDoInput(true);

            /**
             * 下面的三句代码，就是调用第三方http接口
             */
            //获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            //发送请求参数即数据
            out.write(data);
            //flush输出流的缓冲
            out.flush();

            /**
             * 下面的代码相当于，获取调用第三方http接口后返回的结果
             */
            //获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            //构造一个字符流缓存
            br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            StringBuffer response = new StringBuffer();
            while ((str = br.readLine()) != null){
                result += str;
            }
            response.append(result);
            System.out.println(result+"结果码");
            //关闭流
            is.close();
            //断开连接，disconnect是在底层tcp socket链接空闲时才切断，如果正在被其他线程使用就不切断。
            conn.disconnect();
            jsonObject=JSONObject.parseObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (out != null){
                    out.close();
                }
                if (br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }


    public static void main(String[] args) {
     /*   String url="https://mblws.acxiom.com.cn/mbl/member/getCustomer";
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = DateUtil.format(sdf2.format(new Date()), DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        System.out.println(startTime);
        String Message="op3-Dtzb122S1g-Ocu3OCpdBTKHI"+startTime+"maybelline";
        System.out.println(Message);
        System.out.println(startTime+"时间");
        String md5 = SmsUtils.getMD5(Message);
        System.out.println(md5.toLowerCase()+"加密數據");
        JSONObject obj=new JSONObject();
        obj.put("unionId","op3-Dtzb122S1g-Ocu3OCpdBTKHI");
        obj.put("channel","app");
        obj.put("sourceTag","arvato");
        obj.put("signature",md5);
        doPostOrGet(url,obj.toString());*/





    }
}
