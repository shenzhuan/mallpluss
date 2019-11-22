package com.mei.zhuang.service.goods.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.dao.goods.EsShopGoodsMapper;
import com.mei.zhuang.dao.member.CrmOperationLogMapper;
import com.mei.zhuang.dao.order.EsShopOrderMapper;
import com.mei.zhuang.entity.CrmOperationLog;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.service.goods.EsShopGoodsGroupService;
import com.mei.zhuang.service.goods.IGoodsAnalyService;
import com.mei.zhuang.service.order.ShopOrderService;
import com.mei.zhuang.utils.*;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.data.goods.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Auther: Tiger
 * @Date: 2019-08-01 13:53
 * @Description:
 */
@Service
public class IGoodsAnalyServiceImpl implements IGoodsAnalyService {

    @Resource
    private EsShopGoodsGroupService shopGoodsGroupService;

    @Resource
    private EsShopGoodsMapper esShopGoodsMapper;

    private String WeiXinPage = "https://api.weixin.qq.com/datacube/getweanalysisappidvisitpage?access_token=%s";
    @Resource
    private EsShopGoodsMapper goodsMapper;
    @Resource
    private EsShopOrderMapper orderMapper;
    @Resource
    private ShopOrderService orderService;
    @Resource
    private CrmOperationLogMapper crmOperationLogMapper;

    private List<EsShopGoods> selGoodsList(GoodsAnalyzeParam param) {

        //status商品状态（1为出售中，3为已售罄，-2为仓库中，-1为回收站）
        //条件
        QueryWrapper<EsShopGoods> condition = new QueryWrapper();

        if (ValidatorUtils.notEmpty(param.getStartTime()))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(param.getEndTime()))
            condition.le("create_time", Timestamp.valueOf(param.getEndTime()));
        if (ValidatorUtils.notEmpty(param.getShopId())) {
            condition.eq("shop_id", param.getShopId());
        }

        return esShopGoodsMapper.selectList(condition);

    }


    @Override
    public Object goodsStatic(GoodsAnalyzeParam param) throws Exception {
        GoodsHeadDataEntity vo = new GoodsHeadDataEntity();
        param.setEndTime(param.getEndTime() + " 23:59:59.999");
        List<EsShopGoods> goodsList = this.selGoodsList(param);
        int count1 = 0; //在售商品
        int count2 = 0; // 仓库商品数
        int count3 = 0; // 售馨商品
        int count4 = 0; // 商品总销量

        for (EsShopGoods goods : goodsList) {
            if (goods.getStatus() == 1) {
                count1++;
            } else if (goods.getStatus() == -2) {
                count2++;
            } else if (goods.getStatus() == 3) {
                count3++;
            }
        }

        BigDecimal payTotalPrice = new BigDecimal("0.00");//支付总金额
        int refundCount = 0;//退款数量
        BigDecimal refundTotalPrice = new BigDecimal("0.00");//退款总金额


        List<EsShopOrder> orderList = orderService.selOrderListByGoodsAnay(param);
        System.out.println(orderList.toString() + "总金额");
        for (EsShopOrder order : orderList) {
            if (order.getStatus() == 1 || order.getStatus() == 2 || order.getStatus() == 3) {
                payTotalPrice = payTotalPrice.add(order.getPayPrice());
            } else if (order.getStatus() == 4) {
                refundCount++;
                refundTotalPrice = refundTotalPrice.add(order.getPayPrice());
            }
        }

        //商品总销量
        count4 = orderService.selGoodsTotalSaleCount(param);
        System.out.println(count4 + "銷量");


        long len = DateUtil.getDaySub(param.getStartTime(), param.getEndTime(), DateUtil.YYYY_MM_DD);//相差天数

        if (DateCalendarUtils.isYestaday(param.getStartTime()) && DateCalendarUtils.isYestaday(param.getEndTime())) {
            vo.setSetIsDisplayBeforeData(true);

            double saleTotCounToScale;//商品总销量前一日比例
            double payTotAmountToScale;//支付总金额前一日比例
            double refuCountToScale;//退款数量前一日比例
            //获得现在（今天）时间的一天数据(商品|订单)
            double refuTotaAmountToScale;//退款总金额前一日比例


            int saleTotCountBefore = 0;//前一日商品总销量
            BigDecimal payTotalPriceBefore = new BigDecimal("0.00");//前一日支付总金额
            int refundCountBefore = 0;//前一日退款数量
            BigDecimal refundTotalPriceBefore = new BigDecimal("0.00");//前一日退款总金额

            //前一日条件
            GoodsAnalyzeParam paramBefore = new GoodsAnalyzeParam();
            paramBefore.setStartTime(DateUtils.addDay(DateUtils.toDate(param.getStartTime()), -1));
            paramBefore.setEndTime(DateUtils.addDay(DateUtils.toDate(param.getEndTime()), -1));
            paramBefore.setSource(param.getSource());
            System.out.println(paramBefore.getEndTime() + "时间");
            paramBefore.setEndTime(paramBefore.getEndTime() + " 23:59:59.999");
            System.out.println(paramBefore.getEndTime());
            //前一日商品销量数据
            saleTotCountBefore = orderService.selGoodsTotalSaleCount(paramBefore);
            System.out.println("昨天销售" + saleTotCountBefore);
            //前一日订单数据
            List<EsShopOrder> beforeOrderList = orderService.selOrderListByGoodsAnay(paramBefore);
            for (EsShopOrder item : beforeOrderList) {
                if (item.getStatus() == 1 || item.getStatus() == 2 || item.getStatus() == 3) {
                    payTotalPriceBefore = payTotalPriceBefore.add(item.getPayPrice());
                } else if (item.getStatus() == 4) {
                    refundCountBefore++;
                    refundTotalPriceBefore = refundTotalPriceBefore.add(item.getPayPrice());
                }
            }

            //现在选择的数据 - 昨天的数据 / 昨天的数据的总量
            //优化处理 百分率后保留2位小数
            //销售数量
            saleTotCounToScale = saleTotCountBefore != 0 ?
                    BigDecimal.valueOf((double) (count4 - saleTotCountBefore) / (saleTotCountBefore))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.0;
            //金额数量
            payTotAmountToScale = payTotalPriceBefore.doubleValue() != 0.0 ?
                    BigDecimal.valueOf((payTotalPrice.doubleValue() - payTotalPriceBefore.doubleValue()) / payTotalPriceBefore.doubleValue())
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.0;
            //退款数量
            refuCountToScale = refundCountBefore != 0 ?
                    BigDecimal.valueOf((double) (refundCount - refundCountBefore) / (refundCountBefore))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;
            //退款金额
            refuTotaAmountToScale = refundTotalPriceBefore.doubleValue() != 0.0 ?
                    BigDecimal.valueOf((refundTotalPrice.doubleValue() - refundTotalPriceBefore.doubleValue()) / (refundTotalPriceBefore.doubleValue()))
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;

            //赋值比例数据
            vo.setScaleData(new ScaleData(saleTotCounToScale, payTotAmountToScale, refuCountToScale, refuTotaAmountToScale));

        }

        //赋值
        vo.setOnSaleCount(count1);
        vo.setKuCount(count2);
        vo.setSaleOutCount(count3);
        vo.setSaleTotalCount(count4);
        vo.setPayTotaAmount(payTotalPrice.doubleValue());
        vo.setRefundCount(refundCount);
        vo.setRefundTotalAmount(refundTotalPrice.doubleValue());

        return new CommonResult().success(vo);
    }

    @Override
    public List<GoodsTrendMapInfoVo> goodsTrendMapStatic(GoodsTrendMapParam param) throws Exception {

        Map<String, GoodsTrendMapInfoVo> dataMap = new HashMap<>();

        param.setEndTime(param.getEndTime() + " 23:59:59.0");

        List<CrmOperationLog> logList = crmOperationLogMapper.selectList(
                new QueryWrapper<>(new CrmOperationLog())
                        .eq("module", "小程序商品管理")
                        .eq("method", "商品详情")
                        .ge("add_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)))
                        .le("add_time", Timestamp.valueOf(param.getEndTime()))
//                .eq("shop_id", param.getShopId())
                        .notIn("params", "")
        );


        //key:日期  value：存放的是访问的用户id  value（set集合数量就是访客数）
        Map<String, Set<String>> goodsUVMap = new HashMap<>();

        for (CrmOperationLog logTemp : logList) {
            String refDate = DateUtil.format(logTemp.getAddTime(), DateUtil.YYYY_MM_DD);
            if (!goodsUVMap.containsKey(refDate)) {
                goodsUVMap.put(refDate, new HashSet<>());
            }
            Set<String> uvSet = goodsUVMap.get(refDate);
            if (!uvSet.contains(logTemp.getUserName())) {
                uvSet.add(logTemp.getUserName());
            }

        }

        List<EsShopOrderGoods> orderGoodsList = orderService.selOrderGoodsByGoodsAnaly(param);
        if (orderGoodsList != null && orderGoodsList.size() != 0) {
            orderGoodsList.forEach((og) -> {
                String refDate = DateUtil.format(og.getCreateTime(), DateUtils.DATE_PATTERN);
                GoodsTrendMapInfoVo trendVo = null;
                if (!dataMap.containsKey(refDate)) {
                    dataMap.put(refDate, new GoodsTrendMapInfoVo());
                    dataMap.get(refDate).setRelationDate(refDate);
                }

                trendVo = dataMap.get(refDate);
                trendVo.setGoodsSaleTotalCount(trendVo.getGoodsSaleTotalCount() + og.getCount());
                Set<String> refDateUvSet = goodsUVMap.get(refDate);
                trendVo.setGoodsUvCount(refDateUvSet != null ? refDateUvSet.size() : 0);
            });
        }


        //如果某天没有数据， 当天数据展示默认值
        String startTime = param.getStartTime();
        String endTime = param.getEndTime();
        if (ValidatorUtils.notEmpty(startTime) && ValidatorUtils.notEmpty(endTime)) {
            long len = DateUtil.getDaySub(startTime, endTime, DateUtil.YYYY_MM_DD);//相差天数
            len = Math.abs(len) + 1;//实际正常天数 + 1
            if (len != dataMap.size()) {
                String nextDay = startTime;
                for (int i = 0; i < len; i++) {
                    if (!dataMap.containsKey(nextDay)) {
                        dataMap.put(nextDay, new GoodsTrendMapInfoVo());
                        dataMap.get(nextDay).setRelationDate(nextDay);
                    }
                    nextDay = DateUtils.addDay(DateUtils.toDate(nextDay), 1);
                }
            }
        }


        List<GoodsTrendMapInfoVo> list = new ArrayList<>();
        for (Map.Entry<String, GoodsTrendMapInfoVo> itemMap : dataMap.entrySet()) {
            list.add(itemMap.getValue());
        }

        Collections.sort(list, new Comparator<GoodsTrendMapInfoVo>() {
            @Override
            public int compare(GoodsTrendMapInfoVo o1, GoodsTrendMapInfoVo o2) {
                return (int) DateUtil.getDaySub(o1.getRelationDate(), o2.getRelationDate(), DateUtil.YYYY_MM_DD);
            }
        });//升序排序

        return list;
    }

    @Override
    public List<GoodsRankTopSaleVo> goodsRankTopBySaleCount(GoodsRankTopParam param) throws Exception {

        //key:goodsId  value:GoodsRankTopSaleVo对象。
        Map<Long, GoodsRankTopSaleVo> dataMap = new HashMap<>();


        GoodsTrendMapParam tempParam = new GoodsTrendMapParam();
        tempParam.setStartTime(param.getStartTime());
        tempParam.setSource(param.getSource());
        tempParam.setEndTime(param.getEndTime() + " 23:59:59.0");

        List<EsShopOrderGoods> orderGoodsList = orderService.selOrderGoodsByGoodsAnaly(tempParam);
        if (orderGoodsList != null && orderGoodsList.size() > 0) {
            for (EsShopOrderGoods og : orderGoodsList) {
                Long goodsId = og.getGoodsId();
                GoodsRankTopSaleVo trendVo = null;
                if (!dataMap.containsKey(goodsId)) {
                    trendVo = new GoodsRankTopSaleVo();
                    dataMap.put(goodsId, trendVo);
                    trendVo.setThumb(og.getThumb());
                    trendVo.setGoodsNameAndOption(og.getTitle());
                }
                trendVo = dataMap.get(goodsId);
                trendVo.setSaleCount(trendVo.getSaleCount() + og.getCount());
            }
        }
        List<GoodsRankTopSaleVo> dataList = new ArrayList<>();
        for (Map.Entry<Long, GoodsRankTopSaleVo> obj : dataMap.entrySet()) {
            if (dataList.size() == 10) {//只限制前10的商品销量排行榜。
                break;
            }
            dataList.add(obj.getValue());
        }

        Collections.sort(dataList, new Comparator<GoodsRankTopSaleVo>() {
            @Override
            public int compare(GoodsRankTopSaleVo o1, GoodsRankTopSaleVo o2) {
                return o2.getSaleCount() - o1.getSaleCount();
            }
        });//降序排序

        //将考前的设置top //
        int len = dataList.size();
        for (int i = 0; i < len; i++) {
            dataList.get(i).setTop(i + 1);
        }


        return dataList;
    }

    @Override
    public List<GoodsRankTopPayPriceVo> goodsRankTopByPrice(GoodsRankTopParam param) {

        //key:goodsId  value:GoodsRankTopSaleVo对象。
        Map<Long, GoodsRankTopPayPriceVo> dataMap = new HashMap<>();


        GoodsTrendMapParam tempParam = new GoodsTrendMapParam();
        tempParam.setStartTime(param.getStartTime());
        tempParam.setSource(param.getSource());
        tempParam.setEndTime(param.getEndTime() + " 23:59:59.0");

        List<EsShopOrderGoods> orderGoodsList = orderService.selOrderGoodsByGoodsAnaly(tempParam);
        if (orderGoodsList != null && orderGoodsList.size() > 0) {
            for (EsShopOrderGoods og : orderGoodsList) {
                Long goodsId = og.getGoodsId();
                GoodsRankTopPayPriceVo trendVo = null;
                if (!dataMap.containsKey(goodsId)) {
                    trendVo = new GoodsRankTopPayPriceVo();
                    dataMap.put(goodsId, trendVo);
                    trendVo.setThumb(og.getThumb());
                    trendVo.setGoodsNameAndOption(og.getTitle());

                }
                trendVo = dataMap.get(goodsId);
                BigDecimal bd = new BigDecimal(String.valueOf(trendVo.getTotalPayAmount().add(og.getPrice())));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                trendVo.setTotalPayAmount(bd);
            }
        }
        List<GoodsRankTopPayPriceVo> dataList = new ArrayList<>();
        for (Map.Entry<Long, GoodsRankTopPayPriceVo> obj : dataMap.entrySet()) {
            if (dataList.size() == 10) {//只显示前10的商品销量排行榜。
                break;
            }
            dataList.add(obj.getValue());
        }

        //int len = dataList.size();
      /*  for(int i = 0; i < len; i++){
            for(int j = 1; j < len - i - 1; j++){

                GoodsRankTopPayPriceVo comTemp1 = dataList.get(j);
                GoodsRankTopPayPriceVo comTemp2 = dataList.get(j + 1);
                if(comTemp1.getTotalPayAmount().doubleValue() < comTemp2.getTotalPayAmount().doubleValue()){
                    GoodsRankTopPayPriceVo temp = comTemp1;
                    dataList.set(j, comTemp2);
                    dataList.set(j + 1, temp);
                }
            }
            //顺便吧 top 设置下。
            dataList.get(0).setTop(i + 1);
            System.out.println(dataList.get(0).getTop());
        }*/

        Collections.sort(dataList, new Comparator<GoodsRankTopPayPriceVo>() {
            @Override
            public int compare(GoodsRankTopPayPriceVo o1, GoodsRankTopPayPriceVo o2) {
                return (int) (o2.getTotalPayAmount().doubleValue() - o1.getTotalPayAmount().doubleValue());
            }
        });//降序排序

        //将考前的设置top //
        int len = dataList.size();
        for (int i = 0; i < len; i++) {
            dataList.get(i).setTop(i + 1);
        }


        return dataList;
    }

    @Override
    public List<GoodsRankTopDetailVo> goodsRankTopDetail(GoodsRankTopParam param) {
        //日志数据：
        param.setEndTime(param.getEndTime() + " 23:59:59.999");
        List<CrmOperationLog> logList = crmOperationLogMapper.selectList(
                new QueryWrapper<>(new CrmOperationLog())
                        .eq("module", "小程序商品管理")
                        .eq("method", "商品详情")
                        .ge("add_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)))
                        .le("add_time", Timestamp.valueOf(param.getEndTime()))
//                .eq("shop_id", param.getShopId())
                        .notIn("params", "")
        );

        //将小程序商品详情的日志包装成我们要的
        // key:商品id  value:商品访问实体。
        Map<String, GoodsVisitInfo> logMap = new HashMap<>();

        //key:商品id  value：存放的是访问的用户id  value（set集合数量就是访客数）
        Map<String, Set<String>> goodsUVMap = new HashMap<>();

        for (CrmOperationLog logTemp : logList) {
            String params = logTemp.getParams();
//            if(("").equals(params)){
//                continue;
//            }
            String goodsIdS = params.substring(params.indexOf(":") + 1, params.indexOf(";"));

            GoodsVisitInfo visitInfo = null;
            if (!logMap.containsKey(goodsIdS)) {
                logMap.put(goodsIdS, new GoodsVisitInfo());
            }

            if (!goodsUVMap.containsKey(goodsIdS)) {
                goodsUVMap.put(goodsIdS, new HashSet<>());
            }

            Set<String> uvSet = goodsUVMap.get(goodsIdS);
            if (!uvSet.contains(logTemp.getUserName())) {
                uvSet.add(logTemp.getUserName());
            }
            //添加用户到goodsUVmap

            visitInfo = logMap.get(goodsIdS);
            visitInfo.setGoodsPV(visitInfo.getGoodsUV() + 1);

        }

        //计算订单这边东西
        //key:goodsId  value:GoodsRankTopSaleVo对象。
        Map<Long, GoodsRankTopDetailVo> dataMap = new HashMap<>();

        //key:商品id  value：存放的是访问的用户id  value值，判断复购人数
        Map<Long, Set<Long>> orderUVMap = new HashMap<>();

        //key：商品id ， value:复购人数

        Map<Long, Integer> orderUVMapValue = new HashMap<>();


        GoodsTrendMapParam tempParam = new GoodsTrendMapParam();
        tempParam.setStartTime(param.getStartTime());
        tempParam.setSource(param.getSource());
        tempParam.setEndTime(param.getEndTime());

        List<EsShopOrderGoods> orderGoodsList = orderService.selOrderGoodsByGoodsAnaly(tempParam);
        if (orderGoodsList != null && orderGoodsList.size() != 0) {
            for (EsShopOrderGoods og : orderGoodsList) {
                Long goodsId = og.getGoodsId();
                GoodsRankTopDetailVo rankTopDetailVo = null;
                if (!dataMap.containsKey(goodsId)) {
                    rankTopDetailVo = new GoodsRankTopDetailVo();
                    dataMap.put(goodsId, rankTopDetailVo);
                }
                rankTopDetailVo = dataMap.get(goodsId);
                rankTopDetailVo.setGoodsNameAndOption(og.getTitle());
                rankTopDetailVo.setPayCount(rankTopDetailVo.getPayCount() + og.getCount());
                rankTopDetailVo.setTotalPayAmount(rankTopDetailVo.getTotalPayAmount() + og.getPrice().doubleValue());


                if (!orderUVMap.containsKey(goodsId)) {
                    orderUVMap.put(goodsId, new HashSet<>());
                }

                //set 包含 memberId吗？
                Set<Long> goodsIdSet = orderUVMap.get(goodsId);
                if (goodsIdSet.contains(og.getMemberId())) {
                    //复加
                    orderUVMapValue.put(goodsId, orderUVMapValue.get(goodsId) + 1);
                } else {
                    //复购人数 逻辑
                    goodsIdSet.add(og.getMemberId());
                }
            }
        }


        //合并：

        List<GoodsRankTopDetailVo> dataList = new ArrayList<>();

        dataMap.forEach((goodsId, rankDetailTemp) -> {
            //vlaue值 是复购人数
            Integer onceBuy = orderUVMapValue.get(goodsId);
            rankDetailTemp.setPaiedPeopleCount(onceBuy != null ? onceBuy.intValue() : 0);
            //设置改商品访客数   set数量就是访客数
            rankDetailTemp.setGoodsUV(goodsUVMap.get((goodsId + "")).size());
            dataList.add(rankDetailTemp);
        });


        return dataList;
    }

    //商品排行
    @Override
    public List<EsShopOrderGoods> goodsRankTopRanking(GoodsAnalyzeParam param) {

        param.setEndTime(param.getEndTime() + " 23:59:59.999");
        param.setStartTime(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS));
        //时间内订单商品
        List<EsShopOrderGoods> orderLis = orderService.orderGoodsList(param);
        System.out.println(orderLis.toString() + "数据");
//转化成微信所需时间
       /* String wxNeedStartTime = DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
        String wxNeedEndTime = DateUtil.format(param.getEndTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD);
//微信 获取token

        EsMiniprogram miniprogram = memberFegin.getByShopId(param.getShopId() == null ? 1l : param.getShopId());

        String code = WX_HttpsUtil.wxGetQrcode(miniprogram.getAppid(), miniprogram.getAppSecret());

        JSONObject tokenObj = WX_HttpsUtil.httpsRequest(code, "GET", null);
        String token = tokenObj.getString("access_token");
        System.out.println("token:");
        System.out.println(token);
        JSONObject wxVisitedData = getWxVisitedData(WeiXinPage,token,wxNeedStartTime,wxNeedEndTime);
        System.out.println(wxVisitedData.toString()+"数据源");*/

        return orderLis;
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
