package com.mei.zhuang.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ImgBase64Util {


    public static String StringUtil(Long id, String wxUrl) {
        Map<String, Object> date = getToken();
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + date.get("access_token");
        HashMap<String, String> params = new HashMap<>();
        params.put("scene", "id=" + id + "");
        params.put("page", wxUrl);
        JSONObject json = JSONObject.parseObject(params.toString());
        String val = "";
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        wxPost(url, json, val);
        return GetImageStr("d:/" + val + ".png");
    }

    public static String StringUtil(Long id) {
        Map<String, Object> date = getToken();
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + date.get("access_token");
        HashMap<String, String> params = new HashMap<>();
        params.put("scene", "id=" + id + "");
        params.put("page", "pages/goods/detail/index");
        JSONObject json = JSONObject.parseObject(params.toString());
        String val = "";
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        wxPost(url, json, val);
        return GetImageStr("d:/" + val + ".png");
    }

    public static Map<String, Object> getToken() {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            StringBuilder urlSb = new StringBuilder();
            urlSb.append("https://api.weixin.qq.com/cgi-bin/token");
            urlSb.append("?");
            urlSb.append("grant_type=%s&appid=%s&secret=%s");
            String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx15ade215f1447bda&secret=12c7a7ae75571beaf4eb379d30962681";
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(tokenUrl);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String resultText = EntityUtils.toString(httpEntity, "utf-8");
            @SuppressWarnings("unchecked")
            Map<Object, Object> resultMap = JSON.parseObject(resultText, Map.class);
            if (resultMap != null) {
                if (resultMap != null && resultMap.size() > 0) {
                    // 合并2次调用结果
                    Set<Object> keySet = resultMap.keySet();
                    for (Object key : keySet) {
                        if (!data.containsKey(key)) {
                            data.put((String) key, resultMap.get(key));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    //第二步写入图片
    public static void wxPost(String uri, JSONObject paramJson, String fileName) {
        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            printWriter.write(paramJson.toString());
            // flush输出流的缓冲
            printWriter.flush();
            // 开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            File file = new File("d:/" + fileName + ".png");// /Users/shenzhuan/logs/
            OutputStream os = new FileOutputStream(file);
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                os.write(arr, 0, len);
                os.flush();
            }
            os.close();
            bis.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片转化成base64字符串
     *
     * @param imgFile - 转换的图片路径
     * @return imgStr --图片转换后的二进制字节
     */
    public static String GetImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    public static void main(String[] args) {

        //System.out.println( StringUtil((long) 1999));

    }
}
