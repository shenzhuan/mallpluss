package com.mei.zhuang.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.dao.order.EsAppletTemplatesMapper;
import com.mei.zhuang.entity.order.EsAppletTemplates;
import com.mei.zhuang.redis.template.RedisRepository;
import com.mei.zhuang.service.order.MembersFegin;
import com.mei.zhuang.utils.JsonUtils;
import com.mei.zhuang.utils.MyX509TrustManager;
import com.mei.zhuang.utils.WX_HttpsUtil;
import com.mei.zhuang.vo.EsMiniprogram;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * 对接微信接口服务
 * Created by fei on 2017/4/24.
 */
@Service
public class WechatApiService {

    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String WECHAT_API = "https://api.weixin.qq.com/cgi-bin";
    private static final String WECHAT_API_TOKEN = WECHAT_API + "/token";
    private static final String WECHAT_API_TICKET = WECHAT_API + "/ticket/getticket?type=jsapi&access_token=";
    private final HttpClient httpclient;
    @Resource
    private EsAppletTemplatesMapper templatesMapper;
    @Resource
    private MembersFegin membersFegin;

    /*@Resource
    private DistributedLock lock;*/
    @Resource
    private RedisRepository redisRepository;

    public WechatApiService() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(20000)
                .setConnectionRequestTimeout(3000)
                .build();
        httpclient = HttpClients.custom().setDefaultRequestConfig(config).build();
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject handleRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;

        try {
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            SSLContext ctx = SSLContext.getInstance("SSL", "SunJSSE");
            TrustManager[] tm = {new MyX509TrustManager()};
            ctx.init(null, tm, new SecureRandom());
            SSLSocketFactory sf = ctx.getSocketFactory();
            conn.setSSLSocketFactory(sf);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod(requestMethod);
            conn.setUseCaches(false);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                conn.connect();
            }

            if (StringUtils.isNotEmpty(outputStr)) {
                OutputStream out = conn.getOutputStream();
                out.write(outputStr.getBytes("utf-8"));
                out.close();
            }

            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = null;

            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }

            in.close();
            conn.disconnect();

            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static void main(String[] args) {


        List<EsAppletTemplates> listWeiXin = new ArrayList<>();

        EsAppletTemplates a2 = new EsAppletTemplates();
        EsAppletTemplates a3 = new EsAppletTemplates();

        a2.setTemplateId("22");
        a3.setTemplateId("33");

        listWeiXin.add(a2);
        listWeiXin.add(a3);

        List<EsAppletTemplates> templatesList = new ArrayList<>();
        EsAppletTemplates q1 = new EsAppletTemplates();
        EsAppletTemplates q2 = new EsAppletTemplates();

        q1.setId(1L);
        q1.setTemplateId("11");
        q2.setId(2L);
        q2.setTemplateId("22");

        templatesList.add(q1);
        templatesList.add(q2);


        List<EsAppletTemplates> addList = listWeiXin.stream()
                .filter(item -> !templatesList.stream()
                        .map(e -> e.getTemplateId())
                        .collect(toList())
                        .contains(item.getTemplateId()))
                .collect(toList());
        List<EsAppletTemplates> delList = templatesList.stream()
                .filter(item -> !listWeiXin.stream()
                        .map(e -> e.getTemplateId())
                        .collect(toList())
                        .contains(item.getTemplateId()))
                .collect(toList());

        List<String> list1 = new ArrayList();
        list1.add("1111");
        list1.add("2222");


        List<String> list2 = new ArrayList();
        list2.add("3333");
        list2.add("1111");
        list2.add("2222");


        // 差集 (list1 - list2)
        List<String> reduce1 = list1.stream().filter(item -> !list2.contains(item)).collect(toList());
        System.out.println("---得到差集 reduce1 (list1 - list2)---");
        reduce1.parallelStream().forEach(System.out::println);

        // 差集 (list2 - list1)
        List<String> reduce2 = list2.stream().filter(item -> !list1.contains(item)).collect(toList());
        System.out.println("---得到差集 reduce2 (list2 - list1)---");
        reduce2.parallelStream().forEach(System.out::println);


    }

    /**
     * 获取  access_token
     * https://mp.weixin.qq.com/wiki?action=doc&id=mp1421140183
     *
     * @return access_token
     * @throws Exception
     */
    public String getAccessToken(String appid, String appSecret) throws Exception {

        String key = "access_token:" + appid;
        if (redisRepository.willExpire(key) > 30) {
            return redisRepository.get(key).toString();

        }

        //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        String lockKey = "lock_" + key;

       /* boolean acquired = lock.lock(lockKey);
        if (!acquired) {
            throw new Exception("acquired lock: " + lockKey + " timeout");
        }*/
        try {
            if (redisRepository.willExpire(key) > 30) {
                return redisRepository.get(key).toString();
            }

            HttpGet get = new HttpGet(WECHAT_API_TOKEN + "?grant_type=client_credential&appid=" + appid + "&secret=" + appSecret);
            HttpResponse response = httpclient.execute(get);
            String text = EntityUtils.toString(response.getEntity());
            Map<String, Object> resultMap = JsonUtils.readJsonToMap(text);
            String accessToken = (String) resultMap.get("access_token");
            int expiresIn = (int) resultMap.get("expires_in");

            //redisRepository.set(key, accessToken);
            redisRepository.setExpire(key, accessToken, expiresIn);
            return accessToken;
        } finally {
            //   lock.releaseLock(lockKey);
        }
    }

    /**
     * 获取 jsapi_ticket
     * https://mp.weixin.qq.com/wiki?action=doc&id=mp1421141115
     *
     * @param appid
     * @param appSecret
     * @return ticket
     * @throws Exception
     */
    public String getJsTicket(String appid, String appSecret) throws Exception {

        String key = "jsapi_ticket:" + appid;

        if (redisRepository.willExpire(key) > 30) {
            return redisRepository.get(key).toString();
        }

        String lockKey = "lock_" + key;

       /* boolean acquired = lock.lock(lockKey);
        if (!acquired) {
            throw new Exception("acquired lock: " + lockKey + " timeout");
        }*/

        try {
            if (redisRepository.willExpire(key) > 30) {
                return redisRepository.get(key).toString();
            }

            HttpGet get = new HttpGet(WECHAT_API_TICKET + getAccessToken(appid, appSecret));
            HttpResponse response = httpclient.execute(get);
            String text = EntityUtils.toString(response.getEntity());
            Map<String, Object> resultMap = JsonUtils.readJsonToMap(text);
            String ticket = (String) resultMap.get("ticket");
            int expiresIn = (int) resultMap.get("expires_in");

            redisRepository.setExpire(key, ticket, expiresIn);

            return ticket;
        } finally {
            //   lock.releaseLock(lockKey);

        }
    }

    public JSONObject synTemplates(Long shopId) throws Exception {
        EsMiniprogram miniprogram = membersFegin.getByShopId(shopId);
        String code = getAccessToken(miniprogram.getAppid(), miniprogram.getAppSecret());
        // String code = getAccessToken("wxf5b847b162d8fbf8", "ac1fbc7256c08f69d2a42e02b27c478c");
        String url = "https://api.weixin.qq.com/cgi-bin/wxopen/template/list?access_token=" + code;
        JSONObject json = new JSONObject();
        json.put("access_token", code);
        json.put("offset", 0);
        json.put("count", 20);
        JSONObject resultJson = WX_HttpsUtil.httpsRequest(url, "POST", json.toString());
        if ("ok".equals(resultJson.getString("errmsg"))) {
            List<EsAppletTemplates> listWeiXin = resultJson.getJSONArray("list").toJavaList(EsAppletTemplates.class);
            List<EsAppletTemplates> templatesList = templatesMapper.selectList(new QueryWrapper<>());
            List<EsAppletTemplates> addList = new ArrayList<>();
            List<EsAppletTemplates> delList = new ArrayList<>();
            if (templatesList != null && templatesList.size() > 0) {
                if (listWeiXin != null && listWeiXin.size() > 0) {
                    addList = listWeiXin.stream()
                            .filter(item -> !templatesList.stream()
                                    .map(e -> e.getTemplateId())
                                    .collect(toList())
                                    .contains(item.getTemplateId()))
                            .collect(toList());
                    delList = templatesList.stream()
                            .filter(item -> !listWeiXin.stream()
                                    .map(e -> e.getTemplateId())
                                    .collect(toList())
                                    .contains(item.getTemplateId()))
                            .collect(toList());
                } else {
                    delList = templatesList;
                }
            } else {
                addList = listWeiXin;
            }
            for (EsAppletTemplates add : addList) {

                add.setStatus(1);
                add.setCreateTime(new Date());
                templatesMapper.insert(add);
            }
            for (EsAppletTemplates del : delList) {
                del.setStatus(2);
                templatesMapper.updateById(del);
            }


        } else {
            System.out.println(resultJson.getString("errmsg"));
        }
        return resultJson;
    }

}

