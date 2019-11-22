package com.mei.zhuang.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.enums.OrderStatus;
import com.mei.zhuang.service.order.ITradeDataAnalyzeService;
import com.mei.zhuang.service.order.ShopOrderService;
import com.mei.zhuang.utils.*;
import com.mei.zhuang.vo.data.trade.*;
import com.mei.zhuang.vo.order.ExportParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Auther: Tiger
 * @Date: 2019-06-27 9:28
 * @Description:
 */
@Service
public class ITradeDataAnalyzeServiceImpl implements ITradeDataAnalyzeService {

    @Resource
    private ShopOrderService shopOrderService;

    @Override
    public TradeDataByHeadVo getHeadData(TradeAnalyzeParam param) throws Exception {
        if(ValidatorUtils.empty(param.getStartTime())){//默认时间 开始时间店铺最小创建时间  最后时间：前端传吧
            param.setStartTime(DateUtil.format(shopOrderService.getMinCreateTime(param.getShopId()), DateUtil.YYYY_MM_DD));
        }


        //获得今日数据
        TradeDataByHeadVo headDataVo = this.getTradeHeadDataVoByOrderList(this.selOrderDataInfo(param), param);
        TradeDataByHeadVo beforeHeadData = null;//前一日数据

        long len = DateUtil.getDaySub(param.getStartTime(), param.getEndTime(), DateUtil.YYYY_MM_DD);//相差天数

        //昨天执行操作， 确保日期符合正常逻辑
//        if (Math.abs(len) == 1 && DateUtil.format(new Date(), DateUtil.YYYY_MM_DD).equals(param.getEndTime())) {
        if(DateCalendarUtils.isYestaday(param.getStartTime()) && DateCalendarUtils.isYestaday(param.getEndTime())){
            headDataVo.setIsDisplayBeforeData(true);
            //前一日数据计算
            TradeAnalyzeParam paramBefore = new TradeAnalyzeParam();
            paramBefore.setStartTime(DateUtils.addDay(DateUtils.toDate(param.getEndTime()), -1));
            paramBefore.setEndTime(DateUtils.addDay(DateUtils.toDate(param.getEndTime()), -1));
            paramBefore.setSource(param.getSource());
            beforeHeadData = this.getTradeHeadDataVoByOrderList(this.selOrderDataInfo(paramBefore),paramBefore);
        }

        //计算前一日比例数据
        if(headDataVo.getIsDisplayBeforeData()){

            double payAmountBeforeOnePerc;//支付金额前一日比例
            double payCountBeforeOnePerc;//支付订单数前一日比例
            double payCountByPeopleBeforeOnePerc;//付款人数前一日比例
            double actualPayCountBeforeOnePerc;//付款订单数前一日比例
            double unitPriceByOneBeforeOnePerc;//客单价前一日比例
            double refundPriceBeforeOnePerc;//退款订单金额前一日比例



            payAmountBeforeOnePerc = beforeHeadData.getPayAmount().doubleValue() != 0.0 ?
                    BigDecimal.valueOf(
                            (headDataVo.getPayAmount().doubleValue() - beforeHeadData.getPayAmount().doubleValue())
                            / beforeHeadData.getPayAmount().doubleValue())
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;

            payCountBeforeOnePerc = beforeHeadData.getPayCount() != 0 ?
                    (double)(headDataVo.getPayCount() - beforeHeadData.getPayCount()) / (beforeHeadData.getPayCount()) * 100 : 0.00;
            payCountByPeopleBeforeOnePerc = beforeHeadData.getPayCountByPeople() != 0 ?
                    (double)(headDataVo.getPayCountByPeople() - beforeHeadData.getPayCountByPeople()) / (beforeHeadData.getPayCountByPeople()) * 100 :0.00;
            actualPayCountBeforeOnePerc = beforeHeadData.getActualPayCount() != 0 ?
                    (double)(headDataVo.getActualPayCount() - beforeHeadData.getActualPayCount()) / (beforeHeadData.getActualPayCount()) * 100 : 0.00;

            unitPriceByOneBeforeOnePerc = beforeHeadData.getUnitPriceByOne().doubleValue() != 0.0 ?
                    BigDecimal.valueOf(
                            (headDataVo.getUnitPriceByOne().doubleValue() - beforeHeadData.getUnitPriceByOne().doubleValue() )
                                    / beforeHeadData.getUnitPriceByOne().doubleValue())
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;


            refundPriceBeforeOnePerc = beforeHeadData.getRefundPrice() .doubleValue() != 0.0 ?
                    BigDecimal.valueOf(
                            (headDataVo.getRefundPrice().doubleValue() - beforeHeadData.getRefundPrice().doubleValue())
                                    / beforeHeadData.getRefundPrice().doubleValue())
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 : 0.00;

            //优化处理 百分率后保留2位小数
            payCountBeforeOnePerc =  BigDecimal.valueOf(payCountBeforeOnePerc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            payCountByPeopleBeforeOnePerc =  BigDecimal.valueOf(payCountByPeopleBeforeOnePerc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            actualPayCountBeforeOnePerc =  BigDecimal.valueOf(actualPayCountBeforeOnePerc).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();


            headDataVo.setBeforeData(new TradeDataBeforeVo(payAmountBeforeOnePerc, payCountBeforeOnePerc, payCountByPeopleBeforeOnePerc,
                    actualPayCountBeforeOnePerc, unitPriceByOneBeforeOnePerc, refundPriceBeforeOnePerc));
        }

        //设置新老客户数据
        OrderCustTotalVo orderCustTotalVo = calcCustTotal(param);

        headDataVo.setNewConsumeAmount(BigDecimal.valueOf(orderCustTotalVo.getNewConsumeAmount()));
        headDataVo.setOldConsumeAmount(BigDecimal.valueOf(orderCustTotalVo.getOldConsumeAmount()));

        int count = orderCustTotalVo.getCount();
        headDataVo.setNewPerc(count != 0 ? (double)orderCustTotalVo.getNewClientCount() / (count) * 100 : 0.00);
        headDataVo.setOldPerc(count != 0 ? (double)orderCustTotalVo.getOldClientCount() / (count) * 100 : 0.00);
        headDataVo.setNewPayAmountPerc(headDataVo.getPayAmount().doubleValue() != 0.0 ?
            orderCustTotalVo.getNewConsumeAmount() / (headDataVo.getPayAmount().doubleValue()) * 100 : 0.00);
        headDataVo.setOldPayAmountPerc(headDataVo.getNewPayAmountPerc() != 0.0 ? 100 - headDataVo.getNewPayAmountPerc(): 0.0);

        //优化处理 百分率后保留2位小数
        headDataVo.setNewPerc(BigDecimal.valueOf(headDataVo.getNewPerc()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        headDataVo.setOldPerc(BigDecimal.valueOf(headDataVo.getOldPerc()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        headDataVo.setNewPayAmountPerc(BigDecimal.valueOf(headDataVo.getNewPayAmountPerc()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        headDataVo.setOldPayAmountPerc(BigDecimal.valueOf(headDataVo.getOldPayAmountPerc()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());


        return headDataVo;
    }

    @Override
    public List<TradeDataEntity> getTradeData(TradeAnalyzeParam param, boolean isAsc) throws Exception {
        List<TradeDataEntity> dataList = this.getTradeData(param);
        if(isAsc){//正序
            Collections.sort(dataList, new Comparator<TradeDataEntity>() {
                @Override
                public int compare(TradeDataEntity o1, TradeDataEntity o2) {
                    return new Long(DateUtil.getDaySub(o1.getRelationDate(),o2.getRelationDate(),DateUtil.YYYY_MM_DD)).intValue();
                }
            });
        }else{//倒序
            Collections.sort(dataList, new Comparator<TradeDataEntity>() {
                @Override
                public int compare(TradeDataEntity o1, TradeDataEntity o2) {
                    return new Long(DateUtil.getDaySub(o2.getRelationDate(),o1.getRelationDate(),DateUtil.YYYY_MM_DD)).intValue();
                }
            });
        }

        return dataList;
    }

    @Override
    public List<TradeDataEntity> getDetailData(TradeAnalyzeParam param) throws Exception {
//        TradeDataEntity.isAsc = ;//设置降序
        return this.getTradeData(param, false);
    }

    @Override
    public boolean exportDetailData(TradeAnalyzeParam param, ExportParam exportParam, HttpServletResponse response) {

        try {

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private List<TradeDataEntity> getTradeData(TradeAnalyzeParam param) throws Exception {
        if(ValidatorUtils.empty(param.getStartTime())){//默认时间 开始时间店铺最小创建时间  最后时间：前端传吧
            param.setStartTime(DateUtil.format(shopOrderService.getMinCreateTime(param.getShopId()), DateUtil.YYYY_MM_DD));
        }
        //key: 日期  value:数据
        Map<String, TradeDataEntity> dataMap = new HashMap<>();
        List<EsShopOrder> orderList = this.selOrderDataInfo(param);
        for (EsShopOrder order : orderList) {
            String orderDate = DateUtils.format(order.getCreateTime());
            if (!dataMap.containsKey(orderDate)) {
                dataMap.put(orderDate, new TradeDataEntity());
                dataMap.get(orderDate).setRelationDate(orderDate);
            }

            TradeDataEntity dataTempByMap = dataMap.get(orderDate);
            System.out.println(dataTempByMap);
            if (OrderUtils.isPayStatus(order)) {
                dataTempByMap.setPayAmount(dataTempByMap.getPayAmount().add(order.getPayPrice()));
                dataTempByMap.setPayCount(dataTempByMap.getPayCount() + 1);
                dataTempByMap.setPayCountByPeople(dataTempByMap.getPayCountByPeople() + 1);
                if (order.getPayType() != 3) {//不是后台付款点击付款状态
                    dataTempByMap.setActualPayCount(dataTempByMap.getActualPayCount() + 1);
                }
            }
            if (order.getStatus() == OrderStatus.REFUND.getValue()) {
                BigDecimal price=order.getRefundPrice()!=null?order.getRefundPrice(): BigDecimal.valueOf(0.0);
                dataTempByMap.setRefundPrice(dataTempByMap.getRefundPrice().add(price));
            }
        }

        //计算客单价（支付金额/支付次数）
        for (Map.Entry<String, TradeDataEntity> obj : dataMap.entrySet()) {
            TradeDataEntity tempItem = dataMap.get(obj.getKey());
            tempItem.setUnitPriceByOne(BigDecimal.valueOf(tempItem.getPayAmount().doubleValue() / tempItem.getPayCount())
                    .setScale(2, BigDecimal.ROUND_HALF_UP));
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
                    System.out.println(dataMap.toString());
                    if (!dataMap.containsKey(nextDay)) {
                        dataMap.put(nextDay, new TradeDataEntity());
                        dataMap.get(nextDay).setRelationDate(nextDay);
                    }
                    nextDay = DateUtils.addDay(DateUtils.toDate(nextDay), 1);
                    System.out.println(nextDay);
                }
            }
        }


        List<TradeDataEntity> dataList = new ArrayList<>();
        for (Map.Entry<String, TradeDataEntity> obj : dataMap.entrySet()) {
            dataList.add(obj.getValue());
        }
        System.out.println(dataList+"数据");
        return dataList;
    }

    private List<EsShopOrder> selOrderDataInfo(TradeAnalyzeParam param) {

        //时间条件处理
        param.setEndTime(param.getEndTime() + " 23:59:59.0");
        //条件
        QueryWrapper<EsShopOrder> condition = new QueryWrapper();
        condition.notIn("status", OrderStatus.INIT.getValue(), OrderStatus.CLOSED.getValue());

        if (ValidatorUtils.notEmpty(param.getStartTime()))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(param.getEndTime()))
            condition.le("create_time", Timestamp.valueOf(param.getEndTime()));
        if (ValidatorUtils.notEmpty(param.getSource()))
            condition.eq("soure_type", param.getSource());
        return shopOrderService.list(condition);
    }

    private TradeDataByHeadVo getTradeHeadDataVoByOrderList(List<EsShopOrder> orderList, TradeAnalyzeParam param) {
        TradeDataByHeadVo headDataVo = new TradeDataByHeadVo();

        BigDecimal payAmount = BigDecimal.valueOf(0.00);//支付金额
        int payCount = 0;//支付订单数 （后台支付 + 实际付款数）
        int payCountByPeople = 0;//付款人数
        int actualPayCount = 0;//付款订单数（实际付款数）
        BigDecimal unitPriceByOne = BigDecimal.valueOf(0.00);//客单价
        BigDecimal refundPrice = BigDecimal.valueOf(0.00);//退款订单金额

        for (EsShopOrder order : orderList) {
            if (OrderUtils.isPayStatus(order)) {
                payAmount = payAmount.add(order.getPayPrice());
//                payCountByPeople++;
                if (order.getPayType() != 3) {//不是后台付款点击付款状态
                    actualPayCount++;
                }
            } else if (order.getStatus() == OrderStatus.REFUND.getValue()) {
                refundPrice = refundPrice.add(order.getPayPrice());
            }
            payCount++;
        }


        payCountByPeople = this.shopOrderService.selectUCustoCount(param);

        //计算客单价（支付金额/顾客总数）
        unitPriceByOne = payCount != 0 ?
                BigDecimal.valueOf(payAmount.doubleValue() / payCount)
                        .setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(0.00);

        //设置值

        headDataVo.setPayAmount(payAmount);
        headDataVo.setPayCount(payCount);
        headDataVo.setPayCountByPeople(payCountByPeople);
        headDataVo.setActualPayCount(actualPayCount);
        headDataVo.setUnitPriceByOne(unitPriceByOne);
        headDataVo.setRefundPrice(refundPrice);
        return headDataVo;
    }


    private OrderCustTotalVo calcCustTotal(TradeAnalyzeParam param){

      /*  <!--获取新老顾客数据思路：
        1.首先从时间段内（所有条件） 取出member_id
        2.从小于选中截止日期中区分老客户，得到老客户id
        3.最后根据老客户id 在选中日期内 累加计算
        4.新客户从条件内用总的减掉老客户的数据
                -->*/

        //测试sql
        /*--总金额
        select sum(pay_price),count(member_id) from es_shop_order where status NOT IN (0, 8)
        AND create_time >= to_timestamp('2019-06-09', 'YYYY/MM/DD HH24:MI:SS') AND create_time <= to_timestamp('2019-06-25', 'YYYY/MM/DD HH24:MI:SS');
*/


        /*
        select sum(record.total) as oldConsumeAmount, sum(record.count) as oldClientCount
        from (select SUM(pay_price) as total , count(member_id) as count,member_id
                from es_shop_order WHERE status NOT IN (0, 8)
                AND create_time >= to_timestamp('2019-06-09', 'YYYY/MM/DD HH24:MI:SS') AND create_time <= to_timestamp('2019-06-25', 'YYYY/MM/DD HH24:MI:SS')
                group by member_id)
        as record
        where member_id in
                (select member_id
                        from es_shop_order
                        where member_id in
                                (select member_id from es_shop_order
                                        WHERE status NOT IN (0, 8) AND create_time >= to_timestamp('2019-06-09', 'YYYY/MM/DD HH24:MI:SS') 							AND create_time <= to_timestamp('2019-06-25', 'YYYY/MM/DD HH24:MI:SS')
                                        group by member_id)
                        AND create_time < to_timestamp('2019-06-25', 'YYYY/MM/DD HH24:MI:SS')
                        group by member_id having count(member_id) > 1
                )*/


        OrderCustTotalVo oldConsumeInfo = shopOrderService.getOldConsumeInfo(param);
        OrderCustTotalVo totalInfo = shopOrderService.getTotalInfo(param);
        if(oldConsumeInfo == null ){
            oldConsumeInfo = new OrderCustTotalVo();
        }
        if(totalInfo == null){
            totalInfo = new OrderCustTotalVo();
        }
        oldConsumeInfo.setCount(totalInfo.getCount());
        oldConsumeInfo.setNewClientCount(totalInfo.getCount() - oldConsumeInfo.getOldClientCount());
        oldConsumeInfo.setNewConsumeAmount(totalInfo.getTotal() - oldConsumeInfo.getOldConsumeAmount());
        oldConsumeInfo.setRelationEndDate(param.getEndTime());

        return oldConsumeInfo;
    }


}


