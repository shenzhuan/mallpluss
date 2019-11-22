package com.mei.zhuang.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.service.order.MembersFegin;
import com.mei.zhuang.service.order.TrafficAnalysiService;
import com.mei.zhuang.utils.*;
import com.mei.zhuang.vo.EsMiniprogram;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import com.mei.zhuang.vo.data.trade.TrafficAnalysis;
import com.mei.zhuang.vo.data.trade.TrafficAnalysisParam;
import com.mei.zhuang.vo.data.trade.TrafficAnalysisParamVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TrafficAnalysisServiceImpl implements TrafficAnalysiService {


    //分享人数
    private String SharedUrl = "https://api.weixin.qq.com/datacube/getweanalysisappidvisitdistribution?access_token=%s";

    //商品浏览量
    private String ShopUrl = "https://api.weixin.qq.com/datacube/getweanalysisappidvisitpage?access_token=%s";

    //浏览量
    private String VisitUrl = "https://api.weixin.qq.com/datacube/getweanalysisappiddailyvisittrend?access_token=%s";

    @Resource
    private MembersFegin membersFegin;

    public void PublicDaView(TradeAnalyzeParam param, Map<String, TrafficAnalysisParam> dataMap, List<TrafficAnalysisParam> TrendList) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(param.getEndTime()));
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(param.getStartTime()));
        param.setEndTime(format.format(calEnd.getTime()));
        param.setStartTime(format.format(calBegin.getTime()));
        if (ValidatorUtils.notEmpty(param.getStartTime()) && ValidatorUtils.notEmpty(param.getEndTime())) {
            long len = DateUtil.getDaySub(param.getStartTime(), param.getEndTime(), DateUtil.YYYY_MM_DD);//相差天数
            len = Math.abs(len) + 1;//实际正常天数 + 1
            if (len != dataMap.size()) {
                String nextDay = param.getStartTime();
                for (int i = 0; i < len; i++) {
                    System.out.println(dataMap.toString());
                    if (!dataMap.containsKey(nextDay)) {
                        dataMap.put(nextDay, new TrafficAnalysisParam());
                        dataMap.get(nextDay).setDataTime(nextDay);
                    }
                    nextDay = DateUtils.addDay(DateUtils.toDate(nextDay), 1);
                }
            }
        }

        for (Map.Entry<String, TrafficAnalysisParam> obj : dataMap.entrySet()) {
            TrendList.add(obj.getValue());
        }
        Collections.sort(TrendList, new Comparator<TrafficAnalysisParam>() {
            public int compare(TrafficAnalysisParam m1, TrafficAnalysisParam m2) {
                //升序
                return new Long(DateUtil.getDaySub(m1.getDataTime(), m2.getDataTime(), DateUtil.YYYY_MM_DD)).intValue();
            }
        });
    }

    @Override
    public List<TrafficAnalysisParam> TrafficList(TradeAnalyzeParam param) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(param.getStartTime()));
        //设置结束时间
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(param.getEndTime()));
        List<String> Datelist = new ArrayList<String>();
        Datelist.add(format.format(calBegin.getTime()));
        // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
        while (format.parse(param.getEndTime()).after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Datelist.add(format.format(calBegin.getTime()));
        }
        Map<String, TrafficAnalysisParam> dataMap = new HashMap<>();
        for (String orderDate : Datelist) {
            dataMap.put(orderDate, new TrafficAnalysisParam());
            TrafficAnalysisParam dataTempByMap = dataMap.get(orderDate);
            //转化成微信所需时间
            JSONObject wxVisitedData = WxVisitData(VisitUrl, orderDate, param);
            JSONObject wxVisitedData2 = WxVisitData(ShopUrl, orderDate, param);
            JSONObject wxVisitedData3 = WxVisitData(SharedUrl, orderDate, param);
            if (wxVisitedData != null) {
                dataTempByMap.setViews((Integer) wxVisitedData.get("visit_pv"));
                dataTempByMap.setVisitors((Integer) wxVisitedData.get("visit_uv"));
            }
            if (wxVisitedData2 != null) {
                dataTempByMap.setShopViews((Integer) wxVisitedData2.get("page_visit_pv"));
                dataTempByMap.setShopVisitors((Integer) wxVisitedData2.get("page_visit_uv"));
            }
            if (wxVisitedData3 != null) {
                if (wxVisitedData3.getString("index").equals("access_source_session_cnt")) {
                    JSONArray itemlist = wxVisitedData3.getJSONArray("item_list");
                    List<Map<String, Integer>> listMaps = (List<Map<String, Integer>>) JSONArray.parse(itemlist.toString());
                    for (int i = 0; i < listMaps.size(); i++) {
                        int jsonObject = listMaps.get(i).get("value");
                        if (jsonObject == 4) {
                            dataTempByMap.setSharedVisits(listMaps.get(i).get("key"));
                            dataTempByMap.setSharedVisitors(listMaps.get(i).get("key"));
                        }
                    }
                }
            }
            dataTempByMap.setDataTime(orderDate);
        }
        List<TrafficAnalysisParam> TrendList = new ArrayList<>();
        PublicDaView(param, dataMap, TrendList);
        return TrendList;
    }

    @Override
    public TrafficAnalysisParam HeadParam(TradeAnalyzeParam param) throws Exception {
        TrafficAnalysisParam Tap = new TrafficAnalysisParam();
        int visitors = 0;//访客数
        int views = 0;//浏览量
        int shopVisitors = 0;//商品访客数
        int shopViews = 0;//商品浏览量
        int SharedVisitors = 0;//分享访问人数
        int SharedVisits = 0;//分享访问次数
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(param.getStartTime()));
        //设置结束时间
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(param.getEndTime()));
        List<String> Datelist = new ArrayList<String>();
        Datelist.add(format.format(calBegin.getTime()));
        // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
        while (format.parse(param.getEndTime()).after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Datelist.add(format.format(calBegin.getTime()));
        }
        //转化成微信所需时间
        for (String orderDate : Datelist) {
            JSONObject wxVisitedData = WxVisitData(VisitUrl, orderDate, param);
            JSONObject wxVisitedData2 = WxVisitData(ShopUrl, orderDate, param);
            JSONObject wxVisitedData3 = WxVisitData(SharedUrl, orderDate, param);

            if (wxVisitedData != null) {
                visitors += (Integer) wxVisitedData.get("visit_pv");
                views += (Integer) wxVisitedData.get("visit_uv");
            }
            if (wxVisitedData2 != null) {
                JSONArray itemlist = new JSONArray();
                itemlist.add(wxVisitedData2);
                List<Map<String, Integer>> listMaps = (List<Map<String, Integer>>) JSONArray.parse(itemlist.toString());
                System.out.println(listMaps.toString());
                for (int i = 0; i < listMaps.size(); i++) {
                    shopVisitors += listMaps.get(i).get("page_visit_pv");
                    shopViews += listMaps.get(i).get("page_visit_uv");
                }
            }
            if (wxVisitedData3 != null) {
                if (wxVisitedData3.getString("index").equals("access_source_session_cnt")) {
                    JSONArray itemlist = wxVisitedData3.getJSONArray("item_list");
                    List<Map<String, Integer>> listMaps = (List<Map<String, Integer>>) JSONArray.parse(itemlist.toString());
                    for (int i = 0; i < listMaps.size(); i++) {
                        int jsonObject = listMaps.get(i).get("value");
                        if (jsonObject == 4) {
                            SharedVisitors += listMaps.get(i).get("key");
                            SharedVisits += listMaps.get(i).get("key");
                        }
                    }
                }
            }
        }

        //昨天的数据
        if (DateCalendarUtils.isYestaday(param.getStartTime()) && DateCalendarUtils.isYestaday(param.getEndTime())) {
            Tap.setDisplayBeforeData(true);
            TradeAnalyzeParam paramBefore = new TradeAnalyzeParam();
            paramBefore.setStartTime(DateUtils.addDay(DateUtils.toDate(param.getStartTime()), -1));
            paramBefore.setEndTime(DateUtils.addDay(DateUtils.toDate(param.getEndTime()), -1));
            paramBefore.setSource(param.getSource());
            System.out.println(paramBefore.getEndTime() + "昨天时间");
            JSONObject wxVisitedData = WxVisitData(VisitUrl, paramBefore.getEndTime(), paramBefore);
            JSONObject wxVisitedData2 = WxVisitData(ShopUrl, paramBefore.getEndTime(), paramBefore);
            JSONObject wxVisitedData3 = WxVisitData(SharedUrl, paramBefore.getEndTime(), paramBefore);
            int visitors2 = 0;//访客数
            int views2 = 0;//浏览量
            int shopVisitors2 = 0;//商品访客数
            int shopViews2 = 0;//商品浏览量
            int SharedVisitors2 = 0;//分享访问人数
            int SharedVisits2 = 0;//分享访问次数
            if (wxVisitedData != null) {
                visitors2 += (Integer) wxVisitedData.get("visit_pv");
                views2 += (Integer) wxVisitedData.get("visit_uv");
            }
            if (wxVisitedData2 != null) {
                JSONArray itemlist = new JSONArray();
                itemlist.add(wxVisitedData2);
                List<Map<String, Integer>> listMaps = (List<Map<String, Integer>>) JSONArray.parse(itemlist.toString());
                for (int i = 0; i < listMaps.size(); i++) {
                    shopVisitors2 += listMaps.get(i).get("page_visit_pv");
                    shopViews2 += listMaps.get(i).get("page_visit_uv");
                }
            }
            if (wxVisitedData3 != null) {
                if (wxVisitedData3.getString("index").equals("access_source_session_cnt")) {
                    JSONArray itemlist = wxVisitedData3.getJSONArray("item_list");
                    List<Map<String, Integer>> listMaps = (List<Map<String, Integer>>) JSONArray.parse(itemlist.toString());
                    for (int i = 0; i < listMaps.size(); i++) {
                        int jsonObject = listMaps.get(i).get("value");
                        if (jsonObject == 4) {
                            SharedVisitors2 += listMaps.get(i).get("key");
                            SharedVisits2 += listMaps.get(i).get("key");
                        }
                    }
                }
            }
            double visitorsvo = visitors2 != 0 ? (double) (visitors / visitors2) * 100 : 0.0;
            double viewsvo = views2 != 0 ? (double) (views / views2) * 100 : 0.0;
            double shopVisitorsvo = shopVisitors2 != 0 ? (double) (shopVisitors / shopVisitors2) * 100 : 0.0;
            double shopViewsvo = shopViews2 != 0 ? (double) (shopViews / shopViews2) * 100 : 0.0;
            double SharedVisitorsvo = SharedVisitors2 != 0 ? (double) (SharedVisitors / SharedVisitors2) * 100 : 0.0;
            double SharedVisitsvo = SharedVisits2 != 0 ? (double) (SharedVisits / SharedVisits2) * 100 : 0.0;
            Tap.setParamVo(new TrafficAnalysisParamVo(visitorsvo, viewsvo, shopVisitorsvo, shopViewsvo, SharedVisitorsvo, SharedVisitsvo));
        }
        Tap.setVisitors(visitors);
        Tap.setViews(views);
        Tap.setShopViews(shopViews);
        Tap.setShopVisitors(shopVisitors);
        Tap.setSharedVisits(SharedVisits);
        Tap.setSharedVisitors(SharedVisitors);
        return Tap;
    }

    @Override
    public Map<String, Object> TrSelect(TradeAnalyzeParam param) throws Exception {
        //合计
        int shopVisitors = 0;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(param.getStartTime()));
        //设置结束时间
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(param.getEndTime()));
        List<String> Datelist = new ArrayList<String>();
        Datelist.add(format.format(calBegin.getTime()));
        TrafficAnalysis view = new TrafficAnalysis();
        // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
        while (format.parse(param.getEndTime()).after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Datelist.add(format.format(calBegin.getTime()));
        }
        Map<String, TrafficAnalysis> dataMap = new HashMap<>();
        for (String orderDate : Datelist) {
            String str = orderDate.substring(5, 10);
            dataMap.put(orderDate, new TrafficAnalysis());
            dataMap.get(orderDate).setDataTime(orderDate);
            dataMap.get(orderDate).setTime(str);
            TrafficAnalysis dataTempByMap = dataMap.get(orderDate);

            //转化成微信所需时间
            JSONObject wxVisitedData = WxVisitData(ShopUrl, orderDate, param);
            if (wxVisitedData != null) {
                JSONArray itemlist = new JSONArray();
                itemlist.add(wxVisitedData);
                List<Map<String, Integer>> listMaps = (List<Map<String, Integer>>) JSONArray.parse(itemlist.toString());
                for (int i = 0; i < listMaps.size(); i++) {
                    dataTempByMap.setBounceRate(dataTempByMap.getBounceRate() + (double) listMaps.get(i).get("exitpage_pv"));
                    shopVisitors += listMaps.get(i).get("exitpage_pv");
                }
            }
        }
       /* long subDay = DateUtil.getDaySub(param.getEndTime(),param.getStartTime(), DateUtil.YYYY_MM_DD);
        subDay = Math.abs(subDay);//相差的天数
        String start=null;
        String end=null;
        if(subDay<=31&&subDay>=30){//自然月
            calBegin.add(Calendar.MONTH, -1);
            calEnd.add(Calendar.MONTH,-1);
            end=format.format(calEnd.getTime())+" 23:59:59.999";
            start=format.format(calBegin.getTime());
            numerical(start,end,shopVisitors,param,view);
        }else if(subDay == 6 && DateCalendarUtils.isMonday(param.getStartTime()) && DateCalendarUtils.isSunday(param.getEndTime())){//自然周
            calBegin.add(Calendar.DATE, -1);
            calEnd.add(Calendar.DATE,-1);
            end=format.format(calEnd.getTime())+" 23:59:59.999";
            start=format.format(calBegin.getTime());
            numerical(start,end,shopVisitors,param,view);
        }*/
        long subDay = DateUtil.getDaySub(param.getEndTime(), param.getStartTime(), DateUtil.YYYY_MM_DD);
        subDay = Math.abs(subDay);//相差的天数
        view.setBounceTotal(shopVisitors);
        BigDecimal bd = BigDecimal.valueOf(shopVisitors / subDay);
        double number = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        view.setBounceAvg(number);
        List<TrafficAnalysis> TrendList = new ArrayList<>();
        for (Map.Entry<String, TrafficAnalysis> obj : dataMap.entrySet()) {
            TrendList.add(obj.getValue());
        }
        Collections.sort(TrendList, new Comparator<TrafficAnalysis>() {
            public int compare(TrafficAnalysis m1, TrafficAnalysis m2) {
                //升序
                return new Long(DateUtil.getDaySub(m1.getDataTime(), m2.getDataTime(), DateUtil.YYYY_MM_DD)).intValue();
            }
        });
        Map<String, Object> Map = new HashMap<>();
        Map.put("data", view);
        Map.put("excle", TrendList);
        return Map;
    }

    @Override
    public List<TrafficAnalysis> TrafficData(TradeAnalyzeParam param) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(param.getStartTime()));
        //设置结束时间
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(param.getEndTime()));
        List<String> Datelist = new ArrayList<String>();
        Datelist.add(format.format(calBegin.getTime()));
        TrafficAnalysis view = new TrafficAnalysis();
        // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
        while (format.parse(param.getEndTime()).after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Datelist.add(format.format(calBegin.getTime()));
        }
        Map<String, TrafficAnalysis> dataMap = new HashMap<>();
        for (String orderDate : Datelist) {
            String str = orderDate.substring(5, 10);
            dataMap.put(orderDate, new TrafficAnalysis());
            dataMap.get(orderDate).setDataTime(orderDate);
            dataMap.get(orderDate).setTime(str);
            TrafficAnalysis dataTempByMap = dataMap.get(orderDate);

            //转化成微信所需时间
            JSONObject wxVisitedData = WxVisitData(ShopUrl, orderDate, param);
            if (wxVisitedData != null) {
                JSONArray itemlist = new JSONArray();
                itemlist.add(wxVisitedData);
                List<Map<String, Integer>> listMaps = (List<Map<String, Integer>>) JSONArray.parse(itemlist.toString());
                for (int i = 0; i < listMaps.size(); i++) {
                    dataTempByMap.setBounceRate(dataTempByMap.getBounceRate() + (double) listMaps.get(i).get("entrypage_pv"));
                }
            }
        }
        List<TrafficAnalysis> TrendList = new ArrayList<>();
        for (Map.Entry<String, TrafficAnalysis> obj : dataMap.entrySet()) {
            TrendList.add(obj.getValue());
        }
        Collections.sort(TrendList, new Comparator<TrafficAnalysis>() {
            public int compare(TrafficAnalysis m1, TrafficAnalysis m2) {
                //升序
                return new Long(DateUtil.getDaySub(m1.getDataTime(), m2.getDataTime(), DateUtil.YYYY_MM_DD)).intValue();
            }
        });
        return TrendList;
    }

    public void numerical(String start, String end, int shopVisitors, TradeAnalyzeParam param, TrafficAnalysis view) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //环比（本期数-上期数）/上期数×100%。 今年
        double SeNumber;
        //同比
        double SameNumber;//按今年-去年
        Calendar calBegin2 = Calendar.getInstance();
        calBegin2.setTime(format.parse(param.getStartTime()));
        Calendar calEnd2 = Calendar.getInstance();
        calEnd2.setTime(format.parse(param.getEndTime()));
        calBegin2.add(Calendar.YEAR, -1);
        calEnd2.add(Calendar.YEAR, -1);
        end = format.format(calEnd2.getTime()) + " 23:59:59.999";
        start = format.format(calBegin2.getTime());

    }

    public JSONObject WxVisitData(String Url, String orderDate, TradeAnalyzeParam param) throws ParseException {
        String wxNeedStartTime = DateUtil.format(orderDate, DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        String wxNeedEndTime = DateUtil.format(orderDate, DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        //微信 获取token
        EsMiniprogram miniprogram = membersFegin.getByShopId(param.getShopId() == null ? 1 : param.getShopId());
        String code = WX_HttpsUtil.wxGetQrcode(miniprogram.getAppid(), miniprogram.getAppSecret());
        JSONObject tokenObj = WX_HttpsUtil.httpsRequest(code, "GET", null);
        String token = tokenObj.getString("access_token");
        JSONObject wxVisitedData = getWxVisitedData(Url, token, wxNeedStartTime, wxNeedEndTime);

        return wxVisitedData;

    }

    private JSONObject getWxVisitedData(String dateUrl, String accessToken, String wxNeedStartTime, String wxNeedEndTime) {
        JSONObject result = null;
        try {
            JSONObject jsonParamData = new JSONObject();
            jsonParamData.put("begin_date", wxNeedStartTime);
            jsonParamData.put("end_date", wxNeedEndTime);
            JSONObject resultByMonthData = WX_HttpsUtil.httpsRequest(String.format(dateUrl, accessToken), "POST", jsonParamData.toString());
            if (resultByMonthData.getString("errcode") != null) {
                return null;
            }
            result = JSON.parseArray(resultByMonthData.getString("list")).getJSONObject(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
