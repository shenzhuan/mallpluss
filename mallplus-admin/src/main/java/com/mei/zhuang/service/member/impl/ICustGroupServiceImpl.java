package com.mei.zhuang.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arvato.ec.common.utils.DateCalendarUtils;
import com.arvato.ec.common.utils.WX_HttpsUtil;
import com.arvato.ec.common.vo.EsMiniprogram;
import com.arvato.ec.common.vo.data.customer.*;
import com.arvato.ec.common.vo.data.trade.OrderCustTotalVo;
import com.arvato.service.member.api.feigin.OrderFeigin;
import com.arvato.service.member.api.service.EsMemberService;
import com.arvato.service.member.api.service.ICustGroupService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.date.DateUtil;
import com.arvato.utils.date.DateUtils;
import com.arvato.utils.util.ValidatorUtils;
import com.mei.zhuang.entity.order.EsShopOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 14:02
 * @Description:
 */
@Service
public class ICustGroupServiceImpl implements ICustGroupService {

//    POST https://api.weixin.qq.com/datacube/getweanalysisappidmonthlyvisittrend?access_token=ACCESS_TOKEN
//POST https://api.weixin.qq.com/datacube/getweanalysisappiduserportrait?access_token=ACCESS_TOKEN

    //获取用户访问小程序数据   {月}    趋势
    String wxMonthVisitedUrl = "https://api.weixin.qq.com/datacube/getweanalysisappidmonthlyvisittrend?access_token=%s";
    //获取用户访问小程序数据   {周}    趋势
    String wxWeekVisitedUrl = "https://api.weixin.qq.com/datacube/getweanalysisappidweeklyvisittrend?access_token=%s";
    //获取用户访问小程序数据   {日}    趋势
    String wxDayVisitedUrl = "https://api.weixin.qq.com/datacube/getweanalysisappiddailyvisittrend?access_token=%s";

    //获取微信用户画布{年龄，性别， 地区等}  (获取小程序新增或活跃用户的画像分布数据)
    String wxUserAnalyUrl = "https://api.weixin.qq.com/datacube/getweanalysisappiduserportrait?access_token=%s";


    @Resource
    private EsMemberService memberService;

    @Resource
    private OrderFeigin orderFeigin;


    @Override
    public Object getVisitedData(CustGroupIndexParam param) throws Exception{

        CustVisitedDataVo dataVo = new CustVisitedDataVo();

//所需数据字段
        int uvCount = 0;//访客数
        int newAddCustCount = 0;//新增客户数
        int totalCustCount = 0;//累计客户数
        int newAddMemberCount = 0;//新增会员数
        int totalMemberCount = 0;//累计会员数
        CustDataBeforeVo custDataBeforeVo = null;//客户数据前一日比例数据

        Map<String, CustTradeSuccessInfoVo> tradedMap = null;//成交客户分析


        totalCustCount = this.memberService.selTotalMember(param);//总客户数

//参数处理
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calBegin = Calendar.getInstance();
        System.out.println(format.parse(param.getStartTime()));
        calBegin.setTime(format.parse(param.getStartTime()));
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(param.getEndTime()));
        String startTime =format.format(calBegin.getTime());
        String endTime = format.format(calEnd.getTime());
        long subDay = DateUtil.getDaySub(startTime, endTime, DateUtil.YYYY_MM_DD);
        subDay = Math.abs(subDay);//相差的天数
//转化成微信所需时间
        String wxNeedStartTime = DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        String wxNeedEndTime = DateUtil.format(param.getEndTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);

//微信 获取token

        EsMiniprogram miniprogram = memberService.getByShopId(param.getShopId() == null ? 1l : param.getShopId());
        String code = WX_HttpsUtil.wxGetQrcode(miniprogram.getAppid(), miniprogram.getAppSecret());
        JSONObject tokenObj = WX_HttpsUtil.httpsRequest(code, "GET", null);
        String token = tokenObj.getString("access_token");



//业务赋值

        JSONObject wxVisitedData = null;//赋值访问数 和 新增客户数

        if (!DateCalendarUtils.isNotNowTimeAndFutureTime(startTime, DateUtil.YYYY_MM_DD) && !DateCalendarUtils.isNotNowTimeAndFutureTime(endTime, DateUtil.YYYY_MM_DD)) {
            return new CommonResult().failed("选择日期不能是当前日期及未来日期");
        }
        param.setStartTime(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD));
        param.setEndTime(DateUtil.format(param.getEndTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD));
        if(subDay<=31 && DateCalendarUtils.isFristDayOfMonth(param.getStartTime()) && DateCalendarUtils.isLastDayOfMonth(param.getEndTime())){//自然月
            wxVisitedData = this.getWxVisitedData(wxMonthVisitedUrl,token, wxNeedStartTime, wxNeedEndTime);
        }else if(subDay == 6 && DateCalendarUtils.isMonday(startTime) && DateCalendarUtils.isSunday(endTime)){//自然周
            wxVisitedData = this.getWxVisitedData(wxWeekVisitedUrl,token, wxNeedStartTime, wxNeedEndTime);
        }else if(startTime !=null && startTime.equals(endTime)){//自然日
            wxVisitedData = this.getWxVisitedData(wxDayVisitedUrl,token, wxNeedStartTime, wxNeedEndTime);
        }else{
            return new CommonResult().failed("时间格式不对，只接收自然日" +
                    "（指：任意一天），" +
                    "自然周（每个星期的周一至周日的日期)，自然月数据（每月的月初至每月的月末）" +
                    "注：选择日期不能是当前日期及未来日期。");
        }


        if (wxVisitedData != null) {

            //执行成功
            uvCount = Integer.parseInt(wxVisitedData.getString("visit_uv"));
            newAddCustCount = Integer.parseInt(wxVisitedData.getString("visit_uv_new"));
        }

        if(DateCalendarUtils.isYestaday(param.getStartTime()) && DateCalendarUtils.isYestaday(param.getEndTime())){
            dataVo.setIsDisplayBeforeData(true);

            double uvCountScale = 0.0;//访客数占比
            double newAddCustCountScale = 0.0;//新增客户数占比
//            int newAddMemberCountToScale = 0;//新增会员数
//            double totalMemberToPerc = 0.0;//累计会员占比

            //前一日数据计算
            String wxNeedStartBeforeTime = DateUtil.format(
                    DateUtils.addDay(
                            DateUtil.parse(param.getStartTime(), DateUtil.YYYY_MM_DD),
                            -1),
                    DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
            String wxNeedEndBeforeTime = DateUtil.format(
                    DateUtils.addDay(
                            DateUtil.parse(param.getEndTime(), DateUtil.YYYY_MM_DD),
                            -1),
                    DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
            JSONObject wxVisitedBeforeData = this.getWxVisitedData(wxDayVisitedUrl,token, wxNeedStartBeforeTime, wxNeedEndBeforeTime);

            if(wxVisitedBeforeData != null){
                int uvCountBefore = Integer.parseInt(wxVisitedData.getString("visit_uv"));
                int newAddCustCountBefore = Integer.parseInt(wxVisitedData.getString("visit_uv_new"));

                //访客数前一日比例  (今日访客数- 前一日访客数) / 前一日总访客数
                uvCountScale = uvCountBefore != 0 ?
                        BigDecimal.valueOf(
                                (uvCount - uvCountBefore) / uvCountBefore)
                        .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;

                //新增客户数前一日比例(今日新增客户数- 前一日新增客户数) / 前一日总新增客户数
                newAddCustCountScale = newAddCustCountBefore != 0 ?
                        BigDecimal.valueOf(
                                (newAddCustCount - newAddCustCountBefore) / newAddCustCountBefore)
                                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.0;
            }

            custDataBeforeVo = new CustDataBeforeVo();
            custDataBeforeVo.setUvCountBeforeOnePerc(uvCountScale);
            custDataBeforeVo.setNewAddCustCountBeforeOnePerc(newAddCustCountScale);
        }


        dataVo.setUvCount(uvCount);
        dataVo.setNewAddCustCount(newAddCustCount);
        dataVo.setTotalCustCount(totalCustCount);
        dataVo.setNewAddMemberCount(newAddMemberCount);
        dataVo.setTotalMemberCount(totalMemberCount);
//        dataVo.setTotalMemberToPerc(totalMemberToPerc);
        dataVo.setCustDataBeforeVo(custDataBeforeVo);

//        dataVo.setRefDate(param.getStartTime() + "：" + param.getEndTime());
        dataVo.setRefDate(param.getEndTime());

        return new CommonResult().success(dataVo);
    }

    @Override
    public Object selCustAnalyData(CustPropsParam param) throws Exception {
        CustPropsInfoVo dataVo = new CustPropsInfoVo();

//所需数据字段
        CustPropSexInfoVo custPropSexInfoVo = null;//客户属性性别
        List<Wx_CustAgeInfo> wxCustAgeList = null;//客户属性年龄
        List<CustAreaAnalyzeInfoVo> areaInfoVoList = null;//地域

        int totalCustCount = 0;//累计客户数

//获取用户数量
        CustGroupIndexParam tempParam = new CustGroupIndexParam();
        tempParam.setSource(param.getSource());
        tempParam.setShopId(param.getShopId());
        tempParam.setStartTime(param.getStartTime());
        tempParam.setEndTime(param.getEndTime());
        totalCustCount = this.memberService.selTotalMember(tempParam);//总客户数

        //参数处理
        String startTime = param.getStartTime();
        String endTime = param.getEndTime();
        long subDay = DateUtil.getDaySub(startTime, endTime, DateUtil.YYYY_MM_DD);
        subDay = Math.abs(subDay);//相差的天数

//转化成微信所需时间
        String wxNeedStartTime = DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        String wxNeedEndTime = DateUtil.format(param.getEndTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);

//微信 获取token

        EsMiniprogram miniprogram = memberService.getByShopId(param.getShopId() == null ? 1l : param.getShopId());
        String code = WX_HttpsUtil.wxGetQrcode(miniprogram.getAppid(), miniprogram.getAppSecret());
        JSONObject tokenObj = WX_HttpsUtil.httpsRequest(code, "GET", null);
        String token = tokenObj.getString("access_token");

//业务赋值

        JSONObject wxCustData = null;// 获取 性别 ， 年龄， 地域 数据

        //结束时间是 昨天 ，相差是几 就说明是最近 ××
        if(subDay == 29 && DateCalendarUtils.isYestaday(endTime)){//最近三十天
            //微信分布图数据
            wxCustData = this.getweanalysisappiduserportrait(token, wxNeedStartTime, wxNeedEndTime);
        }else if(subDay == 6 && DateCalendarUtils.isYestaday(endTime)){//最近7天
            //微信分布图数据
            wxCustData = this.getweanalysisappiduserportrait(token, wxNeedStartTime, wxNeedEndTime);
        }else if(subDay == 0 && DateCalendarUtils.isYestaday(endTime)){//最近1天
            //微信分布图数据
            wxCustData = this.getweanalysisappiduserportrait(token, wxNeedStartTime, wxNeedEndTime);
        }else{
            return new CommonResult().paramFailed("时间格式不正确！ 只接受最近一天，最近7天，最近30天的格式");
        }

        if (wxCustData != null) {
            String refDate = wxCustData.getString("ref_date");
            JSONObject visit_uv = wxCustData.getJSONObject("visit_uv");//微信活跃用户对象

            //性别分析Vo数据--》按微信数据
            custPropSexInfoVo = this.getCustPropSexInfo(JSON.parseArray(visit_uv.getString("genders")), refDate, totalCustCount);
            //年龄分析--》按微信区间  这个年龄总人数传的微信总人数
            wxCustAgeList = this.getCustAgeInfo(JSON.parseArray(visit_uv.getString("ages")), custPropSexInfoVo.getTotalCustCount());
            //地区排行榜分析--》按微信数据   这个年龄总人数传的微信总人数
            areaInfoVoList = this.getCustAreaInfo(visit_uv.getJSONArray("province"), custPropSexInfoVo.getTotalCustCount());
        }

        dataVo.setCustPropSexInfoVo(custPropSexInfoVo);
        dataVo.setWxCustAgeList(wxCustAgeList);
        dataVo.setAreaAnalyzeInfoVoList(areaInfoVoList);
        dataVo.setRefDate(param.getStartTime() + "至" + param.getEndTime());

        return new CommonResult().success(dataVo);
    }

    @Override
    public Map<String, CustTradeSuccessInfoVo> getTradeScuInfo(CustTradeSuccessParam param) {
        Map<String, CustTradeSuccessInfoVo> tradedList = getTradedList(param);
        return tradedList;
    }

    @Override
    public List<CustTendencyInfoVo> getTendencyMapData(CustTendencyParam param) {

        List<CustTendencyInfoVo> data = null;//最终返回数据

        List<EsShopOrder> newOrderList = orderFeigin.getNewCustOrderList(param);
        List<EsShopOrder> oldOrderList = orderFeigin.getOldCustOrderList(param);//接入订单老客户数据

//        private Integer dataType;//1.成交次数 2. 支付订单数 3. 客单价 4.支付金额
        Integer dataType = param.getDataType();
        if (dataType == 1) {
            data = this.getListByTradedCount(param, newOrderList, oldOrderList);//成交次数
        } else if (dataType == 2) {
            data = this.getListByPayCount(param, newOrderList, oldOrderList);//支付订单数
        } else if (dataType == 3) {
            data = this.getListByCustPriceOne(param, newOrderList, oldOrderList);//客单价
        } else if (dataType == 4) {
            data = this.getListByPayAmount(param, newOrderList, oldOrderList);//支付金额
        }

        CustTendencyInfoVo.isAsc = true;
        Collections.sort(data);

        return data;
    }

    private List<CustTendencyInfoVo> getListByPayAmount(CustTendencyParam param, List<EsShopOrder> newOrderList, List<EsShopOrder> oldOrderList) {
        List<CustTendencyInfoVo> data = new ArrayList<>();
        try {
            Map<String, BigDecimal> newDataMap = getMapByPayAmount(param, newOrderList);
            Map<String, BigDecimal> oldDataMap = getMapByPayAmount(param, oldOrderList);
            //此时两个map 的大小一致 key 一致
            for (Map.Entry<String, BigDecimal> item : newDataMap.entrySet()) {
//                public CustTendencyInfoVo(String newCustValue, String oldCustValue, String refDate) {
                data.add(new CustTendencyInfoVo(
                        String.valueOf(item.getValue()), //新客户值数据
                        String.valueOf(oldDataMap.get(item.getKey())),//；老客户值
                        item.getKey()));//关系日期
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private Map<String, BigDecimal> getMapByPayAmount(CustTendencyParam param, List<EsShopOrder> orderList) {
        Map<String, BigDecimal> dataMap = new HashMap<>();
        try {
            for (EsShopOrder order : orderList) {
                String refDate = DateUtils.format(order.getCreateTime(), DateUtil.YYYY_MM_DD);
                if (!dataMap.containsKey(refDate)) {
                    dataMap.put(refDate, BigDecimal.valueOf(0.00));
                }
                Integer status = order.getStatus();
                if (status == 1 || status == 2 || status == 3) {
                    dataMap.put(refDate, dataMap.get(refDate).add(order.getPayPrice()));
                }
            }

            //如果某天没有数据， 当天数据展示默认值0
            String startTime = param.getStartTime();
            String endTime = param.getEndTime();
            if (ValidatorUtils.notEmpty(startTime) && ValidatorUtils.notEmpty(endTime)) {
                long len = DateUtil.getDaySub(startTime, endTime, DateUtil.YYYY_MM_DD);//相差天数
                len = Math.abs(len) + 1;//实际正常天数 + 1
                if (len != dataMap.size()) {
                    String nextDay = startTime;
                    for (int i = 0; i < len; i++) {
                        if (!dataMap.containsKey(nextDay)) {
                            dataMap.put(nextDay, BigDecimal.valueOf(0.00));
                        }
                        nextDay = DateUtils.addDay(DateUtils.toDate(nextDay), 1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataMap;

    }


    private List<CustTendencyInfoVo> getListByCustPriceOne(CustTendencyParam param, List<EsShopOrder> newOrderList, List<EsShopOrder> oldOrderList) {

        List<CustTendencyInfoVo> data = new ArrayList<>();
        Map<String, BigDecimal> newDataMap = getMapByCustPriceOne(param, newOrderList);
        Map<String, BigDecimal> oldDataMap = getMapByCustPriceOne(param, oldOrderList);
        //此时两个map 的大小一致 key 一致
        for (Map.Entry<String, BigDecimal> item : newDataMap.entrySet()) {
            data.add(new CustTendencyInfoVo(
                    String.valueOf(item.getValue()), //新客户值数据
                    String.valueOf(oldDataMap.get(item.getKey())),//老客户值数据
                    item.getKey()));//关系日期
        }
        return data;
    }

    private Map<String, BigDecimal> getMapByCustPriceOne(CustTendencyParam param, List<EsShopOrder> orderList) {

        Map<String, BigDecimal> dataMap = new HashMap<>();
        try {
            //累加那天的支付金额
            for (EsShopOrder order : orderList) {
                String refDate = DateUtils.format(order.getCreateTime(), DateUtil.YYYY_MM_DD);
                if (!dataMap.containsKey(refDate)) {
                    dataMap.put(refDate, BigDecimal.valueOf(0.00));
                }
                Integer status = order.getStatus();
                if (status == 1 || status == 2 || status == 3) {
                    dataMap.put(refDate, dataMap.get(refDate).add(order.getPayPrice()));
                }
            }

            //如果某天没有数据， 当天数据展示默认值0
            String startTime = param.getStartTime();
            String endTime = param.getEndTime();
            if (ValidatorUtils.notEmpty(startTime) && ValidatorUtils.notEmpty(endTime)) {
                long len = DateUtil.getDaySub(startTime, endTime, DateUtil.YYYY_MM_DD);//相差天数
                len = Math.abs(len) + 1;//实际正常天数 + 1
                if (len != dataMap.size()) {
                    String nextDay = startTime;
                    for (int i = 0; i < len; i++) {
                        if (!dataMap.containsKey(nextDay)) {
                            dataMap.put(nextDay, BigDecimal.valueOf(0.00));
                        }
                        nextDay = DateUtils.addDay(DateUtils.toDate(nextDay), 1);
                    }
                }
            }

            Map<String, Integer> tradedCountMap = this.getMapByTradedCount(param, orderList);
            //此时两个map 的大小一致 key 一致
            for (Map.Entry<String, Integer> item : tradedCountMap.entrySet()) {
                BigDecimal payAmount = dataMap.get(item.getKey());
                BigDecimal count = BigDecimal.valueOf(item.getValue());
                BigDecimal priceOne = count != BigDecimal.valueOf(0) ? payAmount.divide(count).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(0.0);
                dataMap.put(item.getKey(), priceOne);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataMap;
    }


    private List<CustTendencyInfoVo> getListByPayCount(CustTendencyParam param, List<EsShopOrder> newOrderList, List<EsShopOrder> oldOrderList) {

        List<CustTendencyInfoVo> data = new ArrayList<>();
        Map<String, Integer> newDataMap = getMapByPayCount(param, newOrderList);
        Map<String, Integer> oldDataMap = getMapByPayCount(param, oldOrderList);
        //此时两个map 的大小一致 key 一致
        for (Map.Entry<String, Integer> item : newDataMap.entrySet()) {
            data.add(new CustTendencyInfoVo(
                    String.valueOf(item.getValue()), //新客户值数据
                    String.valueOf(oldDataMap.get(item.getKey())),//老客户值数据
                    item.getKey()));//关系日期
        }
        return data;
    }

    private List<CustTendencyInfoVo> getListByTradedCount(CustTendencyParam param, List<EsShopOrder> newOrderList, List<EsShopOrder> oldOrderList) {

        List<CustTendencyInfoVo> data = new ArrayList<>();
        Map<String, Integer> newDataMap = getMapByTradedCount(param, newOrderList);
        Map<String, Integer> oldDataMap = getMapByTradedCount(param, oldOrderList);
        //此时两个map 的大小一致 key 一致
        for (Map.Entry<String, Integer> item : newDataMap.entrySet()) {
//                public CustTendencyInfoVo(String newCustValue, String oldCustValue, String refDate) {
            data.add(new CustTendencyInfoVo(
                    String.valueOf(item.getValue()), //新客户值数据
                    String.valueOf(oldDataMap.get(item.getKey())),//；老客户值
                    item.getKey()));//关系日期
        }
        return data;
    }


    private Map<String, Integer> getMapByTradedCount(CustTendencyParam param, List<EsShopOrder> orderList) {
        Map<String, Integer> dataMap = new HashMap<>();
        try {
            for (EsShopOrder order : orderList) {
                String refDate = DateUtils.format(order.getCreateTime(), DateUtil.YYYY_MM_DD);
                if (!dataMap.containsKey(refDate)) {
                    dataMap.put(refDate, 0);
                }
                Integer status = order.getStatus();
                if (status == 3) {
                    dataMap.put(refDate, dataMap.get(refDate) + 1);
                }
            }

            //如果某天没有数据， 当天数据展示默认值0
            String startTime = param.getStartTime();
            String endTime = param.getEndTime();
            if (ValidatorUtils.notEmpty(startTime) && ValidatorUtils.notEmpty(endTime)) {
                long len = DateUtil.getDaySub(startTime, endTime, DateUtil.YYYY_MM_DD);//相差天数
                len = Math.abs(len) + 1;//实际正常天数 + 1
                if (len != dataMap.size()) {
                    String nextDay = startTime;
                    for (int i = 0; i < len; i++) {
                        if (!dataMap.containsKey(nextDay)) {
                            dataMap.put(nextDay, 0);
                        }
                        nextDay = DateUtils.addDay(DateUtils.toDate(nextDay), 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataMap;
    }

    private Map<String, Integer> getMapByPayCount(CustTendencyParam param, List<EsShopOrder> orderList) {
        Map<String, Integer> dataMap = new HashMap<>();
        try {
            for (EsShopOrder order : orderList) {
                String refDate = DateUtils.format(order.getCreateTime(), DateUtil.YYYY_MM_DD);
                if (!dataMap.containsKey(refDate)) {
                    dataMap.put(refDate, 0);
                }
                Integer status = order.getStatus();
                if (status == 1 || status == 2 || status == 3) {
                    dataMap.put(refDate, dataMap.get(refDate) + 1);
                }
            }

            //如果某天没有数据， 当天数据展示默认值0
            String startTime = param.getStartTime();
            String endTime = param.getEndTime();
            if (ValidatorUtils.notEmpty(startTime) && ValidatorUtils.notEmpty(endTime)) {
                long len = DateUtil.getDaySub(startTime, endTime, DateUtil.YYYY_MM_DD);//相差天数
                len = Math.abs(len) + 1;//实际正常天数 + 1
                if (len != dataMap.size()) {
                    String nextDay = startTime;
                    for (int i = 0; i < len; i++) {
                        if (!dataMap.containsKey(nextDay)) {
                            dataMap.put(nextDay, 0);
                        }
                        nextDay = DateUtils.addDay(DateUtils.toDate(nextDay), 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataMap;
    }


    @Override
    public Object getCustRemainScaleByDate(CustTradeRemainParam param) {
        Map<String, Object> data = new HashMap<>();
        if (param.getRemainType() == 1) { //月留存


        } else if (param.getRemainType() == 2) {//周留存

        }


        return data;
    }

    /**
     * 获得成交客户分析所有数据
     *
     * @param param
     * @return
     */
    private Map<String, CustTradeSuccessInfoVo> getTradedList(CustTradeSuccessParam param) {
        Map<String, CustTradeSuccessInfoVo> data = new HashMap<>();

        CustTradeSuccessInfoVo all = null;
        CustTradeSuccessInfoVo newCust = null;
        CustTradeSuccessInfoVo oldCust = null;

        List<EsShopOrder> allOrderList = orderFeigin.getAllOrderInfo(param);//待接入订单数据
        List<EsShopOrder> newOrderList = orderFeigin.getNewOrderInfo(param);//待接入订单数据
        List<EsShopOrder> oldOrderList = orderFeigin.getOldOrderInfo(param);//待接入订单数据
        all = this.getTradedInfoByOrderList(allOrderList);
        newCust = this.getTradedInfoByOrderList(newOrderList);
        oldCust = this.getTradedInfoByOrderList(oldOrderList);

        OrderCustTotalVo orderCustTotalVo = orderFeigin.getCustOrderInfoByCon(param);//待接入订单数据

        all.setTradeSuccessCount(orderCustTotalVo.getCount());
        all.setCustCountToScale(100);
        all.setPayAmountToScale(100);
        all.setCustType(1);
        all.setCustTypeEn("全部客户");

        newCust.setTradeSuccessCount(orderCustTotalVo.getNewClientCount());
        newCust.setCustCountToScale(orderCustTotalVo.getCount() != 0 ?
                BigDecimal.valueOf((double) orderCustTotalVo.getNewClientCount() / (orderCustTotalVo.getCount()) * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() : 0.0);
        newCust.setPayAmountToScale(orderCustTotalVo.getTotal() != 0 ?
                BigDecimal.valueOf(orderCustTotalVo.getNewConsumeAmount() / (orderCustTotalVo.getTotal()) * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() : 0.0);
        newCust.setCustType(2);
        newCust.setCustTypeEn("新客户");

        oldCust.setTradeSuccessCount(orderCustTotalVo.getOldClientCount());
        oldCust.setCustCountToScale(orderCustTotalVo.getCount() != 0 ?
                BigDecimal.valueOf((double) orderCustTotalVo.getOldClientCount() / (orderCustTotalVo.getCount()) * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() : 0.0);
        oldCust.setPayAmountToScale(orderCustTotalVo.getTotal() != 0 ?
                BigDecimal.valueOf(orderCustTotalVo.getOldConsumeAmount() / orderCustTotalVo.getTotal() * 100 )
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() : 0.0);
        oldCust.setCustType(3);
        oldCust.setCustTypeEn("老客户");

        all.setRefDate(param.getStartTime() + "至" + param.getEndTime());

        data.put("all", all);
        data.put("newCust", newCust);
        data.put("oldCust", oldCust);
        return data;
    }

    /**
     * 根据订单id 获得成交客户Vo单个数据
     *
     * @param orderList
     * @return
     */
    private CustTradeSuccessInfoVo getTradedInfoByOrderList(List<EsShopOrder> orderList) {

        CustTradeSuccessInfoVo vo = new CustTradeSuccessInfoVo();
        int payCount = 0;//支付订单数 （后台支付 + 实际付款数）
        BigDecimal unitPriceByOne = BigDecimal.valueOf(0.0);//客单价
        BigDecimal payAmount = BigDecimal.valueOf(0.0);//支付金额
        for (EsShopOrder order : orderList) {
            Integer status = order.getStatus();
            if (status == 1 || status == 2 || status == 3) {
                payCount++;
                payAmount = payAmount.add(order.getPayPrice());
            }
        }

        unitPriceByOne = payCount != 0 ? BigDecimal.valueOf(payAmount.doubleValue() / (payCount)).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(0.0);
        vo.setPayCount(payCount);
        vo.setPayAmount(payAmount);
        vo.setUnitPriceByOne(unitPriceByOne);

        return vo;
    }


    /**
     * 获得年龄分析数据
     *
     * @param ages
     * @param totalCustCount
     * @return
     */
    private List<Wx_CustAgeInfo> getCustAgeInfo(JSONArray ages, Integer totalCustCount) {
        List<Wx_CustAgeInfo> list = new ArrayList<>();
        int len = ages.size();
        for (int i = 0; i < len; i++) {
            JSONObject item = ages.getJSONObject(i);
            Integer value = Integer.valueOf(item.getString("value"));
            double scale = totalCustCount != 0 ? BigDecimal.valueOf((double) value / (totalCustCount) * 100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() : 0.0;
            list.add(new Wx_CustAgeInfo(Integer.valueOf(item.getString("id")),
                    item.getString("name"), value, scale));
        }

        return list;
    }


    /**
     * 获得微信访问数据
     * @param dateUrl 所对应的日期URL
     * @param accessToken
     * @param wxNeedStartTime
     * @param wxNeedEndTime
     * @return
     */
    private JSONObject getWxVisitedData(String dateUrl,String accessToken, String wxNeedStartTime, String wxNeedEndTime) {
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


    /**
     * 获得用户属性分布图数据（age，sex，area）
     * 注：（时间范围 ， 最近 1 7 30  想差时间为0 6 29 ） 且 选中的时间必须是现在时间的前一天(微信统计)
     * //eg： now:20190703     最近一天： 20190702 - 20190702
     *
     * @param accessToken
     * @param wxNeedStartTime
     * @param wxNeedEndTime
     * @return
     */
    private JSONObject getweanalysisappiduserportrait(String accessToken, String wxNeedStartTime, String wxNeedEndTime) {
        JSONObject result = null;
        String baseURL = wxUserAnalyUrl;
        JSONObject jsonParamData = new JSONObject();
        jsonParamData.put("begin_date", wxNeedStartTime);
        jsonParamData.put("end_date", wxNeedEndTime);
        result = WX_HttpsUtil.httpsRequest(String.format(baseURL, accessToken), "POST", jsonParamData.toString());
        System.out.println(String.format(baseURL, accessToken));
        if(result.getString("errmsg") != null || result.getString("errcode") != null){
            System.out.println(result.getString("errmsg"));
            return null;
        }
        return result;
    }

    /**
     * 获得客户属性中得 性别分析数据
     *
     * @param genders
     * @param refDate
     * @param totalCustCount
     * @return
     */
    private CustPropSexInfoVo getCustPropSexInfo(JSONArray genders, String refDate, int totalCustCount) {
        //客户属性---》性别分析

        int wxTotalCustCount = 0;//总客户数
        int maleCount = 0;//男性总数量
        double maleToScale = 0.0;//男性占比
        int femaleCount = 0;//女性总数量
        double femaleToScale = 0.0;//女性占比
        int unknownCount = 0;//未知总数量
        double unknownToScale = 0.0;//未知占比
//        String refDate = "";//关系日期

//        refDate = wxCustData.getString("ref_date");
//        JSONArray genders = JSON.parseArray(wxCustData.getString("genders"));
        int lenGender = genders.size();
        for (int i = 0; i < lenGender; i++) {
            JSONObject jsonGender = genders.getJSONObject(i);
            //各性别总数赋值
            if (Integer.valueOf(jsonGender.getString("id")) == 1 && "男".equals(jsonGender.getString("name"))) {
                maleCount = Integer.valueOf(jsonGender.getString("value"));
                wxTotalCustCount += maleCount;
                continue;
            }
            if (Integer.valueOf(jsonGender.getString("id")) == 2 && "女".equals(jsonGender.getString("name"))) {
                femaleCount = Integer.valueOf(jsonGender.getString("value"));
                wxTotalCustCount += femaleCount;
                continue;
            }
            if (Integer.valueOf(jsonGender.getString("id")) == 3 && "未知".equals(jsonGender.getString("name"))) {
                unknownCount = Integer.valueOf(jsonGender.getString("value"));
                wxTotalCustCount += maleCount;
                continue;
            }
        }

        //各性别比例赋值
        maleToScale = wxTotalCustCount != 0 ? (double) maleCount / (wxTotalCustCount) * 100 : 0.0;
        femaleToScale = wxTotalCustCount != 0 ? (double) femaleCount / (wxTotalCustCount) * 100 : 0.0;
        unknownToScale = wxTotalCustCount != 0 ? (double) unknownCount / (wxTotalCustCount) * 100 : 0.0;

        //各性别比例百分比优化
        maleToScale = BigDecimal.valueOf(maleToScale).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        femaleToScale = BigDecimal.valueOf(femaleToScale).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        unknownToScale = BigDecimal.valueOf(unknownToScale).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        //性别分析Vo类实例化
        return new CustPropSexInfoVo(wxTotalCustCount, maleCount, maleToScale,
                femaleCount, femaleToScale, unknownCount, unknownToScale, refDate);
    }

    /**
     * 获得地域排行信息
     *
     * @param provinces
     * @param totalCustCount
     * @return
     */
    private List<CustAreaAnalyzeInfoVo> getCustAreaInfo(JSONArray provinces, int totalCustCount) {
        List<CustAreaAnalyzeInfoVo> data = new ArrayList<>();
        double proCountToSale = 0.0;
        int lenPro = provinces.size();
        for (int i = 0; i < lenPro; i++) {
            JSONObject temp = provinces.getJSONObject(i);
            proCountToSale = totalCustCount != 0 ? (double) Integer.valueOf(temp.getString("value")) / (totalCustCount) * 100 : 0.00;
            proCountToSale = BigDecimal.valueOf(proCountToSale).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            data.add(new CustAreaAnalyzeInfoVo(temp.getString("name").replaceAll("省",""), Integer.valueOf(temp.getString("value")),
                    Integer.valueOf(temp.getString("id")), proCountToSale));
        }

        //根据省份人数进行降序排列
        Collections.sort(data, new Comparator<CustAreaAnalyzeInfoVo>() {
            @Override
            public int compare(CustAreaAnalyzeInfoVo o1, CustAreaAnalyzeInfoVo o2) {
                return o2.getCustCountByArea() - o1.getCustCountByArea();
            }
        });
        //将考前的设置top //
        int len = data.size();
        for(int i = 0; i < len; i++){
            data.get(i).setTop(i + 1);
        }

        if(data.size() > 10){
            return data.subList(0, 10);
        }

        return data;
    }


}
