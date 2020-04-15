package com.zscat.mallplus;


import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zscat.mallplus.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/7/3.
 */
public class T {
    public static void main(String[] args) {
        String card = "6217730700961582";
        String json1 = HttpUtil.get("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=" + card + "&cardBinCheck=true");
        // 转为 Json 对象
        JSONObject json = JSON.parseObject(json1);
        //获取到 bank 代码
        String bank = String.valueOf(json.get("bank"));
        System.out.printf(bank);
        //爬取支付宝银行合作商页面
        String listContent = HttpUtil.get("http://ab.alipay.com/i/yinhang.htm");
        System.out.println(listContent);
        //过滤得到需要的银行名称
        List<String> titles = ReUtil.findAll("<span title=\"(.*?)\" class=\"icon " + bank + "\">(.*?)</span>", listContent, 2);
        for (String title : titles) {
            //打印银行名称
            System.out.println(title);
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        Date da = new Date();
        String format = sdf2.format(da);
        long nowT = DateUtil.strToDate(format, "HH:mm:ss").getTime();
        System.out.println(json);


    }
}
