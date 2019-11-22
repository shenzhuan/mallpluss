package com.mei.zhuang.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.dao.order.EsShopCartMapper;
import com.mei.zhuang.dao.order.EsShopOrderGoodsMapper;
import com.mei.zhuang.dao.order.EsShopOrderMapper;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.enums.OrderStatus;
import com.mei.zhuang.service.order.*;
import com.mei.zhuang.utils.*;
import com.mei.zhuang.vo.EsMiniprogram;
import com.mei.zhuang.vo.data.trade.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DataViewServiceImpl implements DataViewService {

    @Resource
    private ShopOrderGoodsService shopOrderGoodsService;
    @Resource
    private EsShopOrderMapper orderMapper;
    @Resource
    private EsShopOrderGoodsMapper orderGoodsMapper;
    @Resource
    private ShopOrderService shopOrderService;
    @Resource
    private EsShopCartMapper cartMapper;
    @Resource
    private GoodsFegin goodsFegin;
    @Resource
    private MembersFegin memberFegin;

    private String VisitUrl = "https://api.weixin.qq.com/datacube/getweanalysisappiddailyvisittrend?access_token=%s";


    @Override
    public DataViewVo viewVoList(TradeAnalyzeParam param) throws Exception {

        DataViewVo vo = new DataViewVo();
        param.setEndTime(param.getEndTime() + " 23:59:59.999");
        List<EsShopGoods> goodsList = goodsFegin.selShopGoodsList(param);
        int count1 = 0; //在售商品
        int count2 = 0; // 仓库商品数
        int count3 = 0; // 售馨商品
        BigDecimal unitPriceByOne = BigDecimal.valueOf(0.0);//客单价
        for (EsShopGoods goods : goodsList) {
            if (goods.getStatus() == 1) {
                count1++;
            } else if (goods.getStatus() == -2) {
                count2++;
            } else if (goods.getStatus() == 3) {
                count3++;
            }
        }
        System.out.println(count1 + "在售商品");
        BigDecimal payTotalPrice = new BigDecimal("0.00");//支付总金额
        int refundCount = 0;//退款数量
        int payNumber = 0;//付款订单数
        int ordersNumber = 0;//待发货订单数
        BigDecimal refundTotalPrice = new BigDecimal("0.00");//退款总金额
        //条件 订单状态：0->待付款；1->待发货；2->待收货；3->已完成；->4已退款；->5维权中；->6维权已完成；->7已取消；->8已关闭；->9无效订单；

        List<EsShopOrder> orderList = orderMapper.selectList(condition(param));
        for (EsShopOrder order : orderList) {
            if (order.getStatus() == 1 || order.getStatus() == 2 || order.getStatus() == 3) {
                if (order.getStatus() == 1) {
                    ordersNumber++;
                }
                payNumber++;
                //付款金额
                payTotalPrice = payTotalPrice.add(order.getPayPrice());
            } else if (order.getStatus() == 4) {
                refundCount++;
                //退款金额
                refundTotalPrice = refundTotalPrice.add(order.getPayPrice());
            }
        }
        unitPriceByOne = payNumber != 0 ? BigDecimal.valueOf(payTotalPrice.doubleValue() / (payNumber)).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(0.0);
        System.out.println(unitPriceByOne + "单客价格");
        //购物车人数
        List<Long> longscart = cartMapper.shopNumber(param);
        System.out.println(longscart.size() + "购物车人数");
        //付款人数
        List<Long> longsmember = orderMapper.memberIdList(param);
        System.out.println(longsmember.size() + "付款人数");
        //总销量
        List<Long> orderIdList = orderMapper.orderIdList2(param);
        int average = 0;
        if (orderIdList.size() > 0) {
            Integer goodsnum = orderGoodsMapper.selGoodsTotalSaleCount(orderIdList);
            System.out.println(goodsnum + "总销量");
            average = goodsnum / longsmember.size();
            System.out.println(average + "平均件数");
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(param.getStartTime()));
        //设置结束时间
       /* Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(param.getEndTime()));*/
        // List<String> Datelist = new ArrayList<String>();
        // Datelist.add(format.format(calBegin.getTime()));
        int visitpv = 0;
        int visituv = 0;
        while (format.parse(param.getEndTime()).after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            param.setStartTime(format.format(calBegin.getTime()));
            param.setEndTime(format.format(calBegin.getTime()));
            JSONObject jsonObject = JSONData(param);
            if (jsonObject != null) {
                visitpv += (Integer) jsonObject.get("visit_pv");
                visituv += (Integer) jsonObject.get("visit_uv");
            }
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
        }


        //数据比例
        if (DateCalendarUtils.isYestaday(param.getStartTime()) && DateCalendarUtils.isYestaday(param.getEndTime())) {
            vo.setSetSetIsDisplayBeforeData(true);
            double Pageviews = 0;//浏览量
            double Pagevisitors = 0;//访客数
            double TotCounToScale;//待发货订单数
            BigDecimal PayTotAmountToScale;//支付总金额前一日比例
            double ShopToScale;//加购物车人数
            double Purchasesnumber;//人均购买件数
            double Salegoods;//在售商品数
            double Soldgoods;//售罄商品数
            double DaNumber;//付款人数
            double DaOrder;//付款订单
            double DaPrice;//客单价
            double DaRefund;//退款金额
            //前一日条件
            TradeAnalyzeParam paramBefore = new TradeAnalyzeParam();
            paramBefore.setStartTime(DateUtils.addDay(DateUtils.toDate(param.getStartTime()), -1));
            paramBefore.setEndTime(DateUtils.addDay(DateUtils.toDate(param.getEndTime()), -1));
            paramBefore.setSource(param.getSource());
            paramBefore.setEndTime(paramBefore.getEndTime() + " 23:59:59.999");
            System.out.println(paramBefore.getEndTime());
            //购物车人数
            List<Long> longscart2 = cartMapper.shopNumber(paramBefore);
            System.out.println(longscart2.size() + "前一日购物车人数");
            //总销量
            List<Long> orderIdList2 = orderMapper.orderIdList2(paramBefore);

            //付款人数
            List<Long> longsmember2 = orderMapper.memberIdList(paramBefore);
            int average2 = 0;
            System.out.println(longsmember2.size() + "付款人数");
            if (orderIdList2.size() > 0) {
                Integer goodsnum2 = orderGoodsMapper.selGoodsTotalSaleCount(orderIdList2);
                System.out.println(goodsnum2 + "总销量");
                average2 = goodsnum2 / longsmember2.size();
                System.out.println(average2 + "平均件数");
            }
            //前一日商品数
            int cou = 0;//在售商品
            int cou2 = 0;
            int cou3 = 0;// 售馨商品
            List<EsShopGoods> goodsList2 = goodsFegin.selShopGoodsList(paramBefore);
            for (EsShopGoods goods : goodsList2) {
                if (goods.getStatus() == 1) {
                    cou++;
                } else if (goods.getStatus() == -2) {
                    cou2++;
                } else if (goods.getStatus() == 3) {
                    cou3++;
                }
            }
            System.out.println(cou + "昨天在售商品");
            //前一日订单数据
            int orNumber = 0;//待发货订单数
            BigDecimal Price = new BigDecimal("0.00");

            int refundCount2 = 0;
            int payNumber2 = 0;//付款订单数
            BigDecimal refundTotalPrice2 = new BigDecimal("0.00");//退款总金额
            BigDecimal unitPriceByOne2 = new BigDecimal("0.00");//客单价
            List<EsShopOrder> orderList2 = orderMapper.selectList(condition(paramBefore));
            for (EsShopOrder ord : orderList2) {
                if (ord.getStatus() == 1 || ord.getStatus() == 2 || ord.getStatus() == 3) {
                    if (ord.getStatus() == 1) {
                        orNumber++;
                    }
                    payNumber2++;
                    //付款金额
                    Price = Price.add(ord.getPayPrice());
                } else if (ord.getStatus() == 4) {
                    refundCount2++;
                    //退款金额
                    refundTotalPrice2 = refundTotalPrice2.add(ord.getPayPrice());
                }
            }
            unitPriceByOne2 = payNumber2 != 0 ? BigDecimal.valueOf(Price.doubleValue() / (payNumber2)).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(0.0);
            System.out.println(unitPriceByOne2 + "单客价格");

            JSONObject jsonObject2 = JSONData(param);
            int visitpv2 = 0;
            int visituv2 = 0;
            if (jsonObject2 != null) {
                visitpv2 = (Integer) jsonObject2.get("visit_pv");
                visituv2 = (Integer) jsonObject2.get("visit_uv");
            }
            //现在选择的数据 - 昨天的数据 / 昨天的数据的总量
            //优化处理 百分率后保留2位小数
            Pageviews = visitpv2 != 0 ?
                    BigDecimal.valueOf((double) (visitpv - visitpv2) / (visitpv2))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.0;

            Pagevisitors = visituv2 != 0 ?
                    BigDecimal.valueOf((double) (visituv - visituv2) / (visituv2))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.0;

            TotCounToScale = orNumber != 0 ?
                    BigDecimal.valueOf((double) (ordersNumber - orNumber) / (orNumber))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.0;

            double pay = Price.doubleValue() != 0.0 ?
                    BigDecimal.valueOf((payTotalPrice.doubleValue() - Price.doubleValue()) / Price.doubleValue())
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.0;
            BigDecimal divide = BigDecimal.valueOf(pay).setScale(2, BigDecimal.ROUND_HALF_UP);
            ShopToScale = longscart2.size() != 0 ?
                    BigDecimal.valueOf((double) (longscart.size() - longscart2.size()) / (longscart2.size()))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;

            Purchasesnumber = average2 != 0.0 ?
                    BigDecimal.valueOf((double) (average - average2) / (average2))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;

            Salegoods = cou != 0.0 ?
                    BigDecimal.valueOf((double) (count1 - cou) / (cou))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;

            Soldgoods = cou3 != 0.0 ?
                    BigDecimal.valueOf((double) (count3 - cou3) / (cou3))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;
            DaNumber = longsmember2.size() != 0.0 ?
                    BigDecimal.valueOf((double) (longsmember.size() - longsmember2.size()) / (longsmember2.size()))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;

            DaOrder = payNumber2 != 0.0 ?
                    BigDecimal.valueOf((double) (payNumber - payNumber2) / (payNumber2))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;
            DaPrice = unitPriceByOne2.doubleValue() != 0.0 ?
                    BigDecimal.valueOf(unitPriceByOne.doubleValue() - unitPriceByOne2.doubleValue() / unitPriceByOne2.doubleValue())
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;
            DaRefund = refundTotalPrice2.doubleValue() != 0.0 ?
                    BigDecimal.valueOf(refundTotalPrice.doubleValue() - refundTotalPrice2.doubleValue() / refundTotalPrice2.doubleValue())
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;

            //赋值比例数据
            vo.setDview(new DataView(Pageviews, Pagevisitors, TotCounToScale, divide, ShopToScale, Purchasesnumber, Salegoods, Soldgoods, DaNumber, DaOrder, DaPrice, DaRefund));
        }

        vo.setViews(visitpv);
        vo.setVisitors(visituv);
        vo.setPayNumber(longsmember.size());
        vo.setSendGoods(ordersNumber);
        vo.setPayOrder(payNumber);
        vo.setShopNumber(longscart.size());
        vo.setGuestPrice(unitPriceByOne);
        vo.setByamost(payTotalPrice);
        vo.setPerNumber(average);
        vo.setOutNumber(count3);
        vo.setSaleNumber(count1);
        vo.setRefundOrder(refundTotalPrice);
        return vo;
    }

    @Override
    public EntityView entityList(TradeAnalyzeParam param) throws Exception {
        EntityView viw = new EntityView();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        viw.setEnTime(sdf2.format(new Date()));
        String startime = viw.getEnTime() + " 00:00:00.0";
        param.setEndTime(sdf.format(new Date()));
        param.setStartTime(startime);
        param.setSource("1");
        //商品状态
        int cou = 0;//在售商品
        int cou2 = 0; //仓库
        int cou3 = 0;// 售馨商品
        System.out.println(param + "数据");
        List<EsShopGoods> goodsList2 = goodsFegin.selShopGoodsList(param);
        for (EsShopGoods goods : goodsList2) {
            if (goods.getStatus() == 1) {
                cou++;
            } else if (goods.getStatus() == -2) {
                cou2++;
            } else if (goods.getStatus() == 3) {
                cou3++;
            }
        }
        //积累客户数
        //
        int memberNumber = memberFegin.memberNumber(param);
        //付款人数
        System.out.println(param + "今天时间");
        List<Long> longsmember = orderMapper.memberIdList(param);
        System.out.println(longsmember.size() + "付款人数");
        //订单数
        QueryWrapper<EsShopOrder> condition = new QueryWrapper();
        //condition.notIn("status", OrderStatus.INIT.getValue(), OrderStatus.CLOSED.getValue());
        if (ValidatorUtils.notEmpty(param.getStartTime()))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(param.getEndTime()))
            condition.le("create_time", Timestamp.valueOf(param.getEndTime()));
        List<EsShopOrder> orderList = orderMapper.selectList(condition);
        System.out.println(orderList.toString() + "订单数据");
        int payNumber = 0;//订单数
        int StayOrder = 0;//待付款订单数
        int DeliveryOrder = 0;//待发货订单数
        BigDecimal payTotalPrice = new BigDecimal("0.00");//支付总金额
        BigDecimal unitPrice = new BigDecimal("0.00");//客单价
        for (EsShopOrder order : orderList) {
            if (order.getStatus() == 0) {
                StayOrder++;
            }
            if (order.getStatus() == 1) {
                DeliveryOrder++;
            }
            if (order.getStatus() == 1 || order.getStatus() == 2 || order.getStatus() == 3) {
                payNumber++;
                //付款金额
                payTotalPrice = payTotalPrice.add(order.getPayPrice());
            }
        }
        unitPrice = payNumber != 0 ? BigDecimal.valueOf(payTotalPrice.doubleValue() / (payNumber)).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(0.0);
        System.out.println(unitPrice + "单客价格");
        //今天数据
        int visitpv = 0;
        int visituv = 0;
        JSONObject jsonObject = JSONData(param);
        if (jsonObject != null) {
            visitpv = (Integer) jsonObject.get("visit_pv");
            visituv = (Integer) jsonObject.get("visit_uv");
        }
        double rate = visitpv != 0 ? (double) (payNumber / visitpv) * 100 : 0.0;
        //昨天全天
        TradeAnalyzeParam paramBefore = new TradeAnalyzeParam();
        paramBefore.setStartTime(DateUtils.addDay(DateUtils.toDate(param.getStartTime()), -1));
        paramBefore.setEndTime(DateUtils.addDay(DateUtils.toDate(param.getEndTime()), -1));
        paramBefore.setSource(param.getSource());
        paramBefore.setEndTime(paramBefore.getEndTime() + " 23:59:59.999");
        System.out.println(paramBefore.getEndTime() + "昨天的时间" + paramBefore.getStartTime());
        //转化成微信昨天数据
        JSONObject jObject = JSONData(paramBefore);
        List<Long> longsmember2 = orderMapper.memberIdList(paramBefore);
        System.out.println(paramBefore + "昨天时间");
        System.out.println(longsmember2.size() + "昨日付款人数" + jObject);
        //订单数

        List<EsShopOrder> orderList2 = orderMapper.selectList(condition(paramBefore));
        int payNumber2 = 0;//昨日订单数
        BigDecimal payTotalPrice2 = new BigDecimal("0.00");//支付总金额
        BigDecimal unitPrice2 = new BigDecimal("0.00");//客单价
        for (EsShopOrder ord : orderList2) {
            if (ord.getStatus() == 1 || ord.getStatus() == 2 || ord.getStatus() == 3) {
                payNumber2++;
                //付款金额
                payTotalPrice2 = payTotalPrice2.add(ord.getPayPrice());
            }
        }
        unitPrice2 = payNumber2 != 0 ? BigDecimal.valueOf(payTotalPrice2.doubleValue() / (payNumber2)).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(0.0);
        System.out.println(unitPrice2 + "单客价格" + payNumber2);
        int visitpv2 = 0;
        int visituv2 = 0;

        if (jObject != null) {
            visitpv2 = (Integer) jObject.get("visit_pv");
            visituv2 = (Integer) jObject.get("visit_uv");
        }
        double rate2 = visitpv2 != 0 ? (double) (payNumber2 / visitpv2) * 100 : 0.0;


        viw.setSaleNumber(cou);
        viw.setOutNumber(cou3);
        viw.setHouseGoodNumber(cou2);
        viw.setViews(visitpv);
        viw.setViewsYd(visitpv2);
        viw.setVisitorsYd(visituv2);
        viw.setVisitors(visituv);
        viw.setCustomersNumber(memberNumber);
        viw.setOrProportion(payNumber2);
        viw.setPayOrder(payNumber);
        viw.setStayOrder(StayOrder);
        viw.setDeliveryOrder(DeliveryOrder);
        viw.setPayNumber(longsmember.size());
        viw.setPayProportion(longsmember2.size());
        viw.setPayRate(rate);
        viw.setPayRatio(rate2);
        viw.setUnitPrice(unitPrice);
        viw.setUnitRatio(unitPrice2);
        viw.setPayAmount(payTotalPrice);
        viw.setPaidAmount(payTotalPrice2);
        return viw;
    }

    //微信接口数据获取
    public JSONObject JSONData(TradeAnalyzeParam param) {
        String wxNeedStartTime = DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        String wxNeedEndTime = DateUtil.format(param.getEndTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        //微信 获取token
        EsMiniprogram miniprogram = memberFegin.getByShopId(param.getShopId() == null ? 1 : param.getShopId());
        String code = WX_HttpsUtil.wxGetQrcode(miniprogram.getAppid(), miniprogram.getAppSecret());
        JSONObject tokenObj = WX_HttpsUtil.httpsRequest(code, "GET", null);
        String token = tokenObj.getString("access_token");
        JSONObject wxVisitedData = getWxVisitedData(VisitUrl, token, wxNeedStartTime, wxNeedEndTime);
        return wxVisitedData;
    }

    @Override
    public List<TrendView> TrendList(TradeAnalyzeParam param) throws Exception {
        //时间条件处理
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calEnd = Calendar.getInstance();
        param.setEndTime(format.format(calEnd.getTime()) + " 23:59:59.999");
        //条件

        Map<String, TrendView> dataMap = new HashMap<>();
        List<TrendView> TrendList = new ArrayList<>();
        List<EsShopOrder> orderList = orderMapper.selectList(condition(param));
        for (EsShopOrder order : orderList) {
            String orderDate = DateUtils.format(order.getCreateTime());
            if (!dataMap.containsKey(orderDate)) {
                String str = orderDate.substring(5, 10);
                dataMap.put(orderDate, new TrendView());
                dataMap.get(orderDate).setRelationDate(str);
                dataMap.get(orderDate).setDatatime(orderDate);
            }
            TrendView dataTempByMap = dataMap.get(orderDate);
            if (OrderUtils.isPayStatus(order)) {
                dataTempByMap.setPayOrder(dataTempByMap.getPayOrder() + 1);
            } else if (order.getStatus() == OrderStatus.REFUND.getValue()) {
                dataTempByMap.setPayRefund(dataTempByMap.getPayRefund() + 1);
            }
        }
        PublicDaView(param, dataMap, TrendList);
        return TrendList;
    }

    public void PublicDaView(TradeAnalyzeParam param, Map<String, TrendView> dataMap, List<TrendView> TrendList) throws Exception {
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
                        dataMap.put(nextDay, new TrendView());
                        String str2 = nextDay.substring(5, 10);
                        dataMap.get(nextDay).setDatatime(nextDay);
                        dataMap.get(nextDay).setRelationDate(str2);
                    }
                    nextDay = DateUtils.addDay(DateUtils.toDate(nextDay), 1);
                }
            }
        }

        for (Map.Entry<String, TrendView> obj : dataMap.entrySet()) {
            TrendList.add(obj.getValue());
        }
        Collections.sort(TrendList, new Comparator<TrendView>() {
            public int compare(TrendView m1, TrendView m2) {
                //升序
                return new Long(DateUtil.getDaySub(m1.getDatatime(), m2.getDatatime(), DateUtil.YYYY_MM_DD)).intValue();
            }
        });
    }


    @Override
    public List<TrendView> visitorsList(TradeAnalyzeParam param) throws Exception {
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
        Map<String, TrendView> dataMap = new HashMap<>();
        for (String orderDate : Datelist) {
            dataMap.put(orderDate, new TrendView());
            TrendView dataTempByMap = dataMap.get(orderDate);
            //转化成微信所需时间
            String wxNeedStartTime = DateUtil.format(orderDate, DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
            String wxNeedEndTime = DateUtil.format(orderDate, DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
            //微信 获取token
            EsMiniprogram miniprogram = memberFegin.getByShopId(param.getShopId() == null ? 1 : param.getShopId());
            String code = WX_HttpsUtil.wxGetQrcode(miniprogram.getAppid(), miniprogram.getAppSecret());
            JSONObject tokenObj = WX_HttpsUtil.httpsRequest(code, "GET", null);
            String token = tokenObj.getString("access_token");
            System.out.println(token);
            JSONObject wxVisitedData = getWxVisitedData(VisitUrl, token, wxNeedStartTime, wxNeedEndTime);
            if (wxVisitedData != null) {
                dataTempByMap.setViews((Integer) wxVisitedData.get("visit_pv"));
                dataTempByMap.setVisitors((Integer) wxVisitedData.get("visit_uv"));
            }
            String str = orderDate.substring(5, 10);
            dataTempByMap.setDatatime(orderDate);
            dataTempByMap.setRelationDate(str);
        }
        List<TrendView> TrendList = new ArrayList<>();
        PublicDaView(param, dataMap, TrendList);
        return TrendList;
    }

    public BigDecimal OrderList(String calBegin, String calEnd) {
        QueryWrapper<EsShopOrder> condition = new QueryWrapper();
        condition.notIn("status", OrderStatus.INIT.getValue(), OrderStatus.CLOSED.getValue());
        if (ValidatorUtils.notEmpty(calBegin))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(calBegin, DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(calEnd))
            condition.le("create_time", Timestamp.valueOf(calEnd));

        List<EsShopOrder> orderList2 = orderMapper.selectList(condition);
        int payNumber2 = 0;//付款订单数
        BigDecimal Price2 = new BigDecimal("0.00");//总金额
        BigDecimal unitPriceByOne2 = new BigDecimal("0.00");//客单价
        for (EsShopOrder order : orderList2) {
            if (order.getStatus() == 1 || order.getStatus() == 2 || order.getStatus() == 3) {
                payNumber2++;
                //付款金额
                Price2 = Price2.add(order.getPayPrice());
            }
        }
        unitPriceByOne2 = payNumber2 != 0 ? BigDecimal.valueOf(Price2.doubleValue() / (payNumber2)).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(0.0);

        return unitPriceByOne2;
    }

    @Override
    public Map<String, Object> unitList(TradeAnalyzeParam param) throws Exception {
        TrendView view = new TrendView();
        //时间条件处理
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date str1 = format.parse(param.getEndTime());
        param.setEndTime(format.format(str1) + " 23:59:59.999");


        Map<String, TrendView> dataMap = new HashMap<>();
        List<TrendView> TrendList = new ArrayList<>();
        List<EsShopOrder> orderList = orderMapper.selectList(condition(param));
        int payNumber = 0;//付款订单数
        int totalNumber = 0;//总订单
        BigDecimal UPrice = new BigDecimal("0.00");//每天客单价金额
        BigDecimal Price = new BigDecimal("0.00");//每天金额
        BigDecimal totalPrice = new BigDecimal("0.00");//总金额
        BigDecimal unitPriceByOne = new BigDecimal("0.00");//总客单价

        for (int i = 0; i < orderList.size(); i++) {
            EsShopOrder order = orderList.get(i);
            String orderDate = DateUtils.format(order.getCreateTime());
            if (!dataMap.containsKey(orderDate)) {
                String str = orderDate.substring(5, 10);
                dataMap.put(orderDate, new TrendView());
                dataMap.get(orderDate).setRelationDate(str);
                dataMap.get(orderDate).setDatatime(orderDate);
                if (i > 1) {
                    EsShopOrder esShopOrder = orderList.get(i - 1);
                    String esOrder = DateUtils.format(esShopOrder.getCreateTime());
                    TrendView dataTempByMap = dataMap.get(esOrder);
                    UPrice = payNumber != 0 ? BigDecimal.valueOf(Price.doubleValue() / (payNumber)).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(0.0);
                    System.out.println(UPrice + "每天总单客价" + payNumber + "价格" + Price + orderDate);
                    dataTempByMap.setUnitPrice(UPrice.doubleValue());
                    payNumber = 0;
                    Price = new BigDecimal("0.00");
                }
            }
            //每一天的单客价
            if (order.getStatus() == 1 || order.getStatus() == 2 || order.getStatus() == 3) {
                payNumber++;
                totalNumber++;
                //付款金额
                Price = Price.add(order.getPayPrice());
                totalPrice = totalPrice.add(order.getPayPrice());
            }
            System.out.println(UPrice + "每天单客价" + payNumber + "价格" + Price);

        }
        //总平均单客价格
        unitPriceByOne = totalNumber != 0 ? BigDecimal.valueOf(Price.doubleValue() / (totalNumber)).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(0.0);
        System.out.println(unitPriceByOne + "总平均单客价格");
        System.out.println(Price + "总计");

        //环比与同比
        Date str2 = format.parse(param.getStartTime());
        param.setStartTime(format.format(str2));
        long subDay = DateUtil.getDaySub(param.getEndTime(), param.getStartTime(), DateUtil.YYYY_MM_DD);
        subDay = Math.abs(subDay);//相差的天数
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(param.getStartTime()));
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(param.getEndTime()));
        System.out.println(param.getStartTime() + param.getEndTime() + String.valueOf(calEnd.getTime()));
        String start = null;
        String end = null;
        if (subDay <= 31) {//自然月
            calBegin.add(Calendar.MONTH, -1);
            calEnd.add(Calendar.MONTH, -1);
            end = format.format(calEnd.getTime()) + " 23:59:59.999";
            start = format.format(calBegin.getTime());
            numerical(start, end, Price, unitPriceByOne, param, view);
        } else if (subDay == 6 && DateCalendarUtils.isMonday(param.getStartTime()) && DateCalendarUtils.isSunday(param.getEndTime())) {//自然周
            calBegin.add(Calendar.DATE, -1);
            calEnd.add(Calendar.DATE, -1);
            end = format.format(calEnd.getTime()) + " 23:59:59.999";
            start = format.format(calBegin.getTime());
            numerical(start, end, Price, unitPriceByOne, param, view);
        }
        PublicDaView(param, dataMap, TrendList);
        Map<String, Object> Map = new HashMap<>();
        Map.put("data", view);
        Map.put("excle", TrendList);
        return Map;
    }

    public void numerical(String start, String end, BigDecimal Price, BigDecimal unitPriceByOne, TradeAnalyzeParam param, TrendView view) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //环比（本期数-上期数）/上期数×100%。 今年
        double SeNumber;
        BigDecimal bigDecimal = OrderList(start, end);
        SeNumber = bigDecimal.doubleValue() != 0.0 ? BigDecimal.valueOf((double) (unitPriceByOne.doubleValue() - bigDecimal.doubleValue()) / bigDecimal.doubleValue())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.0;
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
        BigDecimal bigDecimal2 = OrderList(start, end);
        SameNumber = bigDecimal2.doubleValue() != 0.0 ? BigDecimal.valueOf((double) (unitPriceByOne.doubleValue() - bigDecimal2.doubleValue()) / bigDecimal2.doubleValue())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.0;

        view.setViewVo(new TrendViewVo(SeNumber, SameNumber, Price, unitPriceByOne));
    }

    @Override
    public Map<String, Object> rateList(TradeAnalyzeParam param) throws Exception {
        TrendView view = new TrendView();
        //时间条件处理
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date str1 = format.parse(param.getEndTime());
        param.setEndTime(format.format(str1) + " 23:59:59.999");
        Map<String, TrendView> dataMap = new HashMap<>();
        List<TrendView> TrendList = new ArrayList<>();
        List<EsShopOrder> orderList = orderMapper.selectList(condition(param));
        int payNumber = 0;//付款订单数
        int totalNumber = 0;//总订单
        double totalrate = 0;
//微信 获取token
        String wxNeedStartTime = null;
        String wxNeedEndTime = null;
        EsMiniprogram miniprogram = memberFegin.getByShopId(param.getShopId() == null ? 1 : param.getShopId());
        String code = WX_HttpsUtil.wxGetQrcode(miniprogram.getAppid(), miniprogram.getAppSecret());
        JSONObject tokenObj = WX_HttpsUtil.httpsRequest(code, "GET", null);
        String token = tokenObj.getString("access_token");
        System.out.println(token);
        for (int i = 0; i < orderList.size(); i++) {
            EsShopOrder order = orderList.get(i);
            String orderDate = DateUtils.format(order.getCreateTime());
            if (!dataMap.containsKey(orderDate)) {
                dataMap.put(orderDate, new TrendView());
                String str = orderDate.substring(5, 10);
                dataMap.put(orderDate, new TrendView());
                dataMap.get(orderDate).setRelationDate(str);
                dataMap.get(orderDate).setDatatime(orderDate);
                if (i > 1) {
                    EsShopOrder esShopOrder = orderList.get(i - 1);
                    String esOrder = DateUtils.format(esShopOrder.getCreateTime());
                    TrendView dataTempByMap = dataMap.get(esOrder);
                    //转化成微信所需时间
                    wxNeedStartTime = DateUtil.format(esOrder, DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
                    wxNeedEndTime = DateUtil.format(esOrder, DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
                    JSONObject wxVisitedData = getWxVisitedData(VisitUrl, token, wxNeedStartTime, wxNeedEndTime);
                    //浏览量
                    int visitpv = 0;
                    if (wxVisitedData != null) {
                        visitpv = (Integer) wxVisitedData.get("visit_pv");
                    }
                    double rate = visitpv != 0 ? (double) (payNumber / visitpv) * 100 : 0.0;
                    //每天转换率
                    dataTempByMap.setConversion(rate);
                    payNumber = 0;
                }
            }
            if (dataMap.size() == 1 && i == orderList.size() - 1) {
                TrendView dataTempByMap = dataMap.get(orderDate);
                //转化成微信所需时间
                wxNeedStartTime = DateUtil.format(orderDate, DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
                wxNeedEndTime = DateUtil.format(orderDate, DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
                JSONObject wxVisitedData = getWxVisitedData(VisitUrl, token, wxNeedStartTime, wxNeedEndTime);
                int visitpv = 0;
                if (wxVisitedData != null) {
                    //浏览量
                    visitpv = (Integer) wxVisitedData.get("visit_pv");
                }
                double rate = visitpv != 0 ? (double) (payNumber / visitpv) * 100 : 0.0;
                //每天转换率
                dataTempByMap.setConversion(rate);
                payNumber = 0;
            }
            //每一天的订单数
            if (order.getStatus() == 1 || order.getStatus() == 2 || order.getStatus() == 3) {
                totalNumber++;
                payNumber++;
            }
        }
        wxNeedStartTime = DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        wxNeedEndTime = DateUtil.format(param.getEndTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        JSONObject wxVisitedData2 = getWxVisitedData(VisitUrl, token, wxNeedStartTime, wxNeedEndTime);
        //浏览量
        if (wxVisitedData2 != null) {
            int visitpv2 = (Integer) wxVisitedData2.get("visit_pv");
            totalrate = visitpv2 != 0 ? (double) (totalNumber / visitpv2) * 100 : 0.0;

        }
        //环比与同比
        Date str2 = format.parse(param.getStartTime());
        param.setStartTime(format.format(str2));
        long subDay = DateUtil.getDaySub(param.getEndTime(), param.getStartTime(), DateUtil.YYYY_MM_DD);
        subDay = Math.abs(subDay);//相差的天数
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(param.getStartTime()));
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(param.getEndTime()));
        String start = null;
        String end = null;
        if (subDay <= 31) {//自然月
            calBegin.add(Calendar.MONTH, -1);
            calEnd.add(Calendar.MONTH, -1);
            end = format.format(calEnd.getTime()) + " 23:59:59.999";
            start = format.format(calBegin.getTime());

        } else if (subDay == 6 && DateCalendarUtils.isMonday(param.getStartTime()) && DateCalendarUtils.isSunday(param.getEndTime())) {//自然周
            calBegin.add(Calendar.DATE, -1);
            calEnd.add(Calendar.DATE, -1);
            end = format.format(calEnd.getTime()) + " 23:59:59.999";
            start = format.format(calBegin.getTime());
        }

        //环比（本期数-上期数）/上期数×100%。 今年
        double SeNumber;
        double bigDecimal = RateList(start, end);
        SeNumber = bigDecimal != 0.0 ? BigDecimal.valueOf((double) (totalrate - bigDecimal) / bigDecimal)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.0;
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
        double bigDecimal2 = RateList(start, end);
        SameNumber = bigDecimal2 != 0.0 ? BigDecimal.valueOf((double) (totalrate - bigDecimal2) / bigDecimal2)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.0;

        view.setViewVo(new TrendViewVo(SeNumber, SameNumber, totalrate));
        PublicDaView(param, dataMap, TrendList);
        Map<String, Object> Map = new HashMap<>();
        Map.put("data", view);
        Map.put("excle", TrendList);
        return Map;
    }

    public QueryWrapper<EsShopOrder> condition(TradeAnalyzeParam param) {
        QueryWrapper<EsShopOrder> condition = new QueryWrapper();
        condition.notIn("status", OrderStatus.INIT.getValue(), OrderStatus.CLOSED.getValue());
        if (ValidatorUtils.notEmpty(param.getStartTime()))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(param.getEndTime()))
            condition.le("create_time", Timestamp.valueOf(param.getEndTime()));
        return condition;
    }


    @Override
    public Map<String, Object> PayViewList(TradeAnalyzeParam param) throws Exception {
        EntityView viw = new EntityView();
        Map<String, Object> Map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        viw.setEnTime(sdf2.format(new Date()));
        System.out.println(viw.getEnTime());
        String startime = viw.getEnTime() + " 00:00:00.0";
        //  param.setEndTime("2019-9-23 23:59:59.999");
        param.setEndTime(sdf.format(new Date()));
        param.setStartTime(startime);
        System.out.println(param.getEndTime() + "时间");
        Map<String, EntityView> dataMap = new HashMap<>();
        Map<String, EntityView> dataMap2 = new HashMap<>();
        List<EntityView> TrendList = new ArrayList<>();

        BigDecimal price = new BigDecimal(0.0);
        List<EsShopOrder> orderList = orderMapper.selectList(condition(param));
        System.out.println(orderList.toString() + "dingd数据");
        if (orderList != null && orderList.size() > 0) {
            for (EsShopOrder order : orderList) {
                String orderdata = DateUtil.format(order.getCreateTime(), DateUtil.YYYY_MM_DD_HH_MM);
                String orData = orderdata.substring(0, 13);
                if (!dataMap.containsKey(orData)) {
                    dataMap.put(orData, new EntityView());
                    dataMap.get(orData).setDate(orData.substring(11, 13) + ":00");
                    dataMap.get(orData).setEnTime(orData);
                }
                EntityView EndataMap = dataMap.get(orData);
                System.out.println(EndataMap);
                if (order.getStatus() == 1 || order.getStatus() == 2 || order.getStatus() == 3) {
                    //付款金额
                    System.out.println(order.getPayPrice());
                    price = price.add(order.getPayPrice());
                    dataMap.get(orData).setPayAmount(price.add(dataMap.get(orData).getPayAmount()));
                    System.out.println(dataMap.get(orData).getPayAmount());
                }
            }
        }
        EntityView(param, dataMap, TrendList);
        Map.put("excle", TrendList);
        //昨天的数据
        List<EntityView> TrendList2 = new ArrayList<>();
        TradeAnalyzeParam paramBefore = new TradeAnalyzeParam();
        paramBefore.setStartTime(DateUtils.addDay(DateUtils.toDate(param.getStartTime()), -1));
        paramBefore.setEndTime(DateUtils.addDay(DateUtils.toDate(param.getEndTime()), -1));
        paramBefore.setEndTime(paramBefore.getEndTime() + " 24:00:00.0");
        BigDecimal price2 = new BigDecimal(0.0);
        List<EsShopOrder> orderList2 = orderMapper.selectList(condition(paramBefore));
        for (EsShopOrder order : orderList2) {
            String orderdata = DateUtil.format(order.getCreateTime(), DateUtil.YYYY_MM_DD_HH_MM);
            String orData = orderdata.substring(0, 13);
            if (!dataMap2.containsKey(orData)) {
                dataMap2.put(orData, new EntityView());
                dataMap2.get(orData).setDate(orData.substring(11, 13) + ":00");
                dataMap2.get(orData).setEnTime(orData);
            }
            EntityView EndataMap = dataMap2.get(orData);
            if (order.getStatus() == 1 || order.getStatus() == 2 || order.getStatus() == 3) {
                //付款金额
                price2 = price2.add(order.getPayPrice());
                dataMap2.get(orData).setPaidAmount(price2.add(dataMap2.get(orData).getPaidAmount()));
            }
        }

        EntityView(paramBefore, dataMap2, TrendList2);
        viw.setPayAmount(price);
        viw.setPaidAmount(price2);
        Map.put("data", viw);
        Map.put("excle2", TrendList2);
        return Map;
    }

    public void EntityView(TradeAnalyzeParam param, Map<String, EntityView> dataMap, List<EntityView> TrendList) throws Exception {
        String st[] = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(param.getStartTime()));
        if (ValidatorUtils.notEmpty(param.getStartTime()) && ValidatorUtils.notEmpty(param.getEndTime())) {
            for (int i = 0; i < st.length; i++) {
                String orderSt = format.format(calBegin.getTime()) + " " + st[i];
                System.out.println(orderSt);
                if (!dataMap.containsKey(orderSt)) {
                    dataMap.put(orderSt, new EntityView());
                    dataMap.get(orderSt).setEnTime(orderSt);
                    dataMap.get(orderSt).setDate(st[i] + ":00");
                }
            }
        }

        for (Map.Entry<String, EntityView> obj : dataMap.entrySet()) {
            TrendList.add(obj.getValue());
        }
        Collections.sort(TrendList, new Comparator<EntityView>() {
            public int compare(EntityView m1, EntityView m2) {
                //升序
                return m1.getDate().compareTo(m2.getDate());
            }
        });
    }


    public double RateList(String calBegin, String calEnd) {
        QueryWrapper<EsShopOrder> condition = new QueryWrapper();
        condition.notIn("status", OrderStatus.INIT.getValue(), OrderStatus.CLOSED.getValue());
        if (ValidatorUtils.notEmpty(calBegin))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(calBegin, DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(calEnd))
            condition.le("create_time", Timestamp.valueOf(calEnd));

        List<EsShopOrder> orderList2 = orderMapper.selectList(condition);
        String wxNeedStartTime2 = DateUtil.format(calBegin, DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        String wxNeedEndTime2 = DateUtil.format(calEnd, DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        EsMiniprogram miniprogram = memberFegin.getByShopId((long) 1);
        String code = WX_HttpsUtil.wxGetQrcode(miniprogram.getAppid(), miniprogram.getAppSecret());
        JSONObject tokenObj = WX_HttpsUtil.httpsRequest(code, "GET", null);
        String token = tokenObj.getString("access_token");
        JSONObject wxVisitedData2 = getWxVisitedData(VisitUrl, token, wxNeedStartTime2, wxNeedEndTime2);
        //浏览量
        int visitpv2 = 0;
        if (wxVisitedData2 != null) {
            visitpv2 = (Integer) wxVisitedData2.get("visit_pv");
        }
        int payNumber2 = 0;//付款订单数
        double rateNumber = 0;
        for (EsShopOrder order : orderList2) {
            if (order.getStatus() == 1 || order.getStatus() == 2 || order.getStatus() == 3) {
                payNumber2++;
            }
        }
        rateNumber = visitpv2 != 0 ? (double) payNumber2 / visitpv2 : 0.0;
        return rateNumber;
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
