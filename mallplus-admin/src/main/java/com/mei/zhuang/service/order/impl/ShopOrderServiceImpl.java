package com.mei.zhuang.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.constant.RedisConstant;
import com.mei.zhuang.dao.order.*;
import com.mei.zhuang.entity.Table.TableColumnInfo;
import com.mei.zhuang.entity.goods.EsShopCustomizedBasic;
import com.mei.zhuang.entity.goods.EsShopCustomizedPacket;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsOption;
import com.mei.zhuang.entity.marking.*;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.entity.order.*;
import com.mei.zhuang.enums.OrderGoodsStatus;
import com.mei.zhuang.enums.OrderStatus;
import com.mei.zhuang.exception.BusinessException;
import com.mei.zhuang.service.member.impl.RedisUtil;
import com.mei.zhuang.service.order.*;
import com.mei.zhuang.util.WX_TemplateMsgUtil;
import com.mei.zhuang.utils.*;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.EsMiniprogram;
import com.mei.zhuang.vo.OmsOrderVo;
import com.mei.zhuang.vo.TemplateData;
import com.mei.zhuang.vo.data.customer.CustTendencyParam;
import com.mei.zhuang.vo.data.customer.CustTradeSuccessParam;
import com.mei.zhuang.vo.data.goods.GoodsAnalyzeParam;
import com.mei.zhuang.vo.data.goods.GoodsTrendMapParam;
import com.mei.zhuang.vo.data.trade.OrderCustTotalVo;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import com.mei.zhuang.vo.marking.CodeResult;
import com.mei.zhuang.vo.marking.MjDcVo;
import com.mei.zhuang.vo.order.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */
@Slf4j
@Service
public class ShopOrderServiceImpl extends ServiceImpl<EsShopOrderMapper, EsShopOrder> implements ShopOrderService {
    @Resource
    private EsShopFormidMapper formidMapper;
    @Resource
    private EsShopOrderSettingsMapper orderSettingsMapper;
    @Resource
    private RedisUtil redisRepository;
    @Resource
    private EsShopOrderMapper orderMapper;
    @Resource
    private EsShopOrderBatchSendDetailMapper orderBatchSendDetailMapper;
    @Resource
    private EsShopOperationLogMapper logMapper;
    @Resource
    private EsShopOrderPriceChangeLogMapper changeLogMapper;

    @Resource
    private ShopOrderPackageService shopOrderPackageService;
    @Resource
    private ShopOrderGoodsService shopOrderGoodsService;
    @Resource
    private ShopDeliveryService shopDeliveryService;

    @Resource
    private EsShopOrderGoodsMapper orderGoodsMapper;
    @Resource
    private EsShopCartMapper cartMapper;
    @Resource
    private EsDeliveryAddresserMapper addresserMapper;

    @Resource
    private TableMapper tableMapper;

    @Resource
    private EsShopCustomizedAppletMapper esShopCustomizedAppletMapper;

    @Resource
    private EsShopCustomizedAppletService appletMakedService;

    @Resource
    private ShopOrderCartServiceImpl orderCartService;
    @Resource
    private EsActivatySmallBeautyBoxGiftBoxMapper esActivatySmallBeautyBoxGiftBoxMapper;

    @Resource
    private GoodsFegin goodsFegin;
    @Resource
    private MembersFegin membersFegin;

    @Resource
    private MarkingFegin markingFegin;

    @Resource
    private EsActivatySmallBeautyBoxGiftBoxService esActivatySmallBeautyBoxGiftBoxService;
    @Resource
    private WechatApiService wechatApiService;
    @Resource
    private EsCoreMessageTemplateMapper esCoreMessageTemplateMapper;

    public Map<String, Object> homeStatic() {

        List<EsShopOrder> orderList = orderMapper.selectList(new QueryWrapper<>());
        int nowOrderCount = 0; // 今日交易订单
        BigDecimal nowOrderAmount = new BigDecimal(0); //今日销交易总额

        int yesOrderCount = 0; // 昨日交易订单

        BigDecimal yesOrderAmount = new BigDecimal(0); //日交易总额

        int qiOrderCount = 0; // 7日交易订单
        BigDecimal qiOrderAmount = new BigDecimal(0); //7日交易总额

        int monthOrderCount = 0; // 本月交易订单
        BigDecimal monthOrderAmount = new BigDecimal(0); //本月交易总额


        int nowPayCount = 0; // 今日成交订单
        BigDecimal nowPayAmount = new BigDecimal(0); //今日成交总额

        int yesPayCount = 0; // 昨日成交订单
        BigDecimal yesPayAmount = new BigDecimal(0); //日成交总额

        int qiPayCount = 0; // 7日成交订单
        BigDecimal qiPayAmount = new BigDecimal(0); //7日成交总额

        int monthPayCount = 0; // 本月成交订单
        BigDecimal monthPayAmount = new BigDecimal(0); //本月成交总额

        int y3 = 0; // 昨日订单
        BigDecimal y3Amount = new BigDecimal(0); //日销售总额
        int y4 = 0; // 昨日订单
        BigDecimal y4Amount = new BigDecimal(0); //日销售总额
        int y5 = 0; // 昨日订单
        BigDecimal y5Amount = new BigDecimal(0); //日销售总额
        int y6 = 0; // 昨日订单
        BigDecimal y6Amount = new BigDecimal(0); //日销售总额
        int y7 = 0; // 昨日订单
        BigDecimal y7Amount = new BigDecimal(0); //日销售总额

        int status0 = 0; // 待付款
        int status1 = 0;//待发货
        int status2 = 0;//待收货
        int status3 = 0;// 维权中
        int status4 = 0;//

        BigDecimal nowAvgConsume = new BigDecimal(0.00);//今日人均消费
        BigDecimal yesAvgConsume = new BigDecimal(0.00);//昨日人均消费

        List<OrderStatic> orderStaticList = new ArrayList<>();
        for (EsShopOrder order : orderList) {
            //今日
            if (DateUtils.format(order.getCreateTime()).equals(DateUtils.format(new Date()))) {
                if (order.getStatus() == OrderStatus.INIT.getValue()) {
                    status0++;
                } else if (order.getStatus() == OrderStatus.TO_DELIVER.getValue()) {
                    status1++;
                } else if (order.getStatus() == OrderStatus.DELIVERED.getValue()) {
                    status2++;
                } else if (order.getStatus() == OrderStatus.RIGHT_APPLY.getValue()) {
                    status3++;
                }
                if (OrderUtils.isPayStatus(order)) {
                    nowPayCount++;
                    nowPayAmount = nowPayAmount.add(order.getPayPrice());
                }
                nowOrderCount++;
                nowOrderAmount = nowOrderAmount.add(order.getOriginalPrice());
            }
            //昨日
            if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -1))) {
                if (OrderUtils.isPayStatus(order)) {
                    yesPayCount++;
                    yesPayAmount = yesPayAmount.add(order.getPayPrice());
                }
                yesOrderCount++;
                yesOrderAmount = yesOrderAmount.add(order.getOriginalPrice());
            }
            //7日
            if (DateUtils.calculateDaysNew(order.getCreateTime(), new Date()) >= 7) {
                if (OrderUtils.isPayStatus(order)) {
                    qiPayCount++;
                    qiPayAmount = qiPayAmount.add(order.getPayPrice());
                }
                qiOrderCount++;
                qiOrderAmount = qiOrderAmount.add(order.getOriginalPrice());
            }
            //本月
            if (order.getCreateTime().getTime() >= DateUtils.geFirstDayDateByMonth().getTime()) {
                if (OrderUtils.isPayStatus(order)) {
                    monthPayCount++;
                    monthPayAmount = monthPayAmount.add(order.getPayPrice());
                }
                monthOrderCount++;
                monthOrderAmount = monthOrderAmount.add(order.getOriginalPrice());
            }

            //近七日
            if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -2))) {
                y3++;
                y3Amount = yesOrderAmount.add(order.getOriginalPrice());
            } else if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -3))) {
                y4++;
                y4Amount = yesOrderAmount.add(order.getOriginalPrice());
            } else if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -4))) {
                y5++;
                y5Amount = yesOrderAmount.add(order.getOriginalPrice());
            } else if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -5))) {
                y6++;
                y6Amount = yesOrderAmount.add(order.getOriginalPrice());
            } else if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -6))) {
                y7++;
                y7Amount = yesOrderAmount.add(order.getOriginalPrice());
            }

        }
        orderStaticList.add(new OrderStatic(DateUtils.addDay(new Date(), -6), y7Amount, y7));
        orderStaticList.add(new OrderStatic(DateUtils.addDay(new Date(), -5), y6Amount, y6));
        orderStaticList.add(new OrderStatic(DateUtils.addDay(new Date(), -4), y5Amount, y5));
        orderStaticList.add(new OrderStatic(DateUtils.addDay(new Date(), -3), y4Amount, y4));
        orderStaticList.add(new OrderStatic(DateUtils.addDay(new Date(), -2), y3Amount, y3));

        orderStaticList.add(new OrderStatic(DateUtils.addDay(new Date(), -1), yesOrderAmount, yesOrderCount));
        orderStaticList.add(new OrderStatic(DateUtils.addDay(new Date(), 0), nowOrderAmount, nowOrderCount));
        Map<String, Object> map = new HashMap();
        map.put("orderStaticList", orderStaticList);
        map.put("nowOrderCount", nowOrderCount);
        map.put("nowOrderAmount", nowOrderAmount);
        map.put("yesOrderCount", yesOrderCount);
        map.put("yesOrderAmount", yesOrderAmount);

        map.put("qiOrderCount", qiOrderCount);
        map.put("qiOrderAmount", qiOrderAmount);
        map.put("monthOrderCount", monthOrderCount);
        map.put("monthOrderAmount", monthOrderAmount);

        map.put("nowPayCount", nowPayCount);
        map.put("nowPayAmount", nowPayAmount);
        map.put("yesPayCount", yesPayCount);
        map.put("yesPayAmount", yesPayAmount);

        map.put("qiPayCount", qiPayCount);
        map.put("qiPayAmount", qiPayAmount);
        map.put("monthPayCount", monthPayCount);
        map.put("monthPayAmount", monthPayAmount);

        map.put("status0", status0);
        map.put("status1", status1);
        map.put("status2", status2);
        map.put("status3", status3);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delivery(EsShopOrderBatchSendDetail orderBatchSendDetail) throws Exception {
        //业务处理
        EsShopOrder order = this.getById(orderBatchSendDetail.getOrderId());
        if (order.getStatus() != OrderStatus.TO_DELIVER.getValue()) {
            return 0;
        }
        order.setStatus(OrderStatus.DELIVERED.getValue());
        order.setSendTime(new Date());
        order.setExpressSn(orderBatchSendDetail.getExpressSn());
        order.setIsPartDelivery(0);
        this.updateById(order);
        /*EsShopOrderPackage entity = new EsShopOrderPackage();
        entity.setSendTime(new Date());
        entity.setCityDistributionType(order.getCityDistributionType());
        entity.setFinishTime(DateUtils.toDate(DateUtils.addDay(entity.getSendTime(), 7)));
        entity.setIsCityDistribution(order.getIsCityDistribution());
        entity.setShopId(order.getShopId());
        entity.setExpressSn(order.getExpressSn());
        entity.setMemberId(1l);//待完善
        entity.setOrderId(order.getId());
        entity.setRemark("亲，给个好评呗!");
        entity.setOrderGoodsIds(order.getGoodsIds());
        shopOrderPackageService.save(entity);*/

        EsShopOperationLog log1 = new EsShopOperationLog();
        StringBuffer sb = new StringBuffer();
        sb.append(order.getId() + "订单确认发货");
        log1.setCreateTime(new Date());
        log1.setOperationDesc(sb.toString());
        logMapper.insert(log1);
        orderBatchSendDetail.setOrderNo(order.getOrderNo()); //待释放
        orderBatchSendDetail.setBatchId(0l);
        orderBatchSendDetail.setCreateTime(new Date());
        orderBatchSendDetail.setGoodsIds(order.getGoodsIds());
        orderBatchSendDetail.setShopId(order.getShopId());//待释放
        CompletableFuture.runAsync(() -> {
            try {
                push(membersFegin.getMemberById(order.getMemberId()), order, order.getOutTradeNo(), 4, null);
                /*String formid = this.getFormIdByMemberId(order.getMemberId());
                sender.orderStatusUpdateMq("");
                if (ValidatorUtils.notEmpty(formid)) {
                    push(membersFegin.getMemberById(order.getMemberId()), order, formid, 4, null);
                } else {
                    log.error("发货消息推送formId不够,memberId=" + order.getMemberId());
                }*/
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        return orderBatchSendDetailMapper.insert(orderBatchSendDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int partDelivery(EsShopOrderBatchSendDetail orderBatchSendDetail) throws Exception {

        /**部分发货功能实现：
         * 1.订单状态更改为 部分发货状态 （待所有订单商品都发货完成才算完成）
         * 2.对应的订单商品信息更改为已发货状态
         * 3.订单商品 添加到包裹信息
         * 4.店铺日志信息
         *
         * */
        EsShopOrder order = this.getById(orderBatchSendDetail.getOrderId());
        if (order.getStatus() != OrderStatus.TO_DELIVER.getValue()) {
            return 0;
        }


        //部分发货的时候， 所有商品全都发货完了才进入订单才进入待收货状态
        order.setStatus(checkDeliveryStatus(order) == true ? OrderStatus.TO_DELIVER.getValue() : OrderStatus.DELIVERED.getValue());
        order.setSendTime(new Date());
        //部分发货拼接快递编号
        StringBuffer sb1 = new StringBuffer(order.getExpressSn());
        sb1.append(",");
        sb1.append(orderBatchSendDetail.getExpressSn());
        order.setExpressSn(sb1.toString());
        this.updateById(order);
        EsShopOrderGoods esShopOrderGoods = new EsShopOrderGoods();
        esShopOrderGoods.setStatus(OrderGoodsStatus.PARTDELIVERY.getValue());
        //存储商品id集合
        List<Long> goodsIdsList = TypeCastUtils.stringToLongList(orderBatchSendDetail.getGoodsIds());
        shopOrderGoodsService.update(esShopOrderGoods, new QueryWrapper<EsShopOrderGoods>().eq("id", goodsIdsList));
        List<EsShopOrderPackage> packList = new ArrayList<EsShopOrderPackage>();
        for (Long ids : goodsIdsList) {
            EsShopOrderPackage entity = new EsShopOrderPackage();
            entity.setSendTime(new Date());
            entity.setCityDistributionType(order.getCityDistributionType());
            entity.setFinishTime(DateUtils.toDate(DateUtils.addDay(entity.getSendTime(), 7)));
            entity.setIsCityDistribution(order.getIsCityDistribution());
            entity.setShopId(order.getShopId());
            entity.setExpressSn(order.getExpressSn());
            entity.setMemberId(order.getMemberId());
            entity.setOrderId(order.getId());
            entity.setRemark("亲，给个好评呗!");
            entity.setOrderGoodsIds(ids.toString());
            packList.add(entity);
        }
        shopOrderPackageService.saveBatch(packList);
        EsShopOperationLog log = new EsShopOperationLog();
        StringBuffer sb = new StringBuffer();
        sb.append(order.getId() + "订单部分发货的商品有：" + orderBatchSendDetail.getGoodsIds());
        log.setCreateTime(new Date());
        log.setOperationDesc(sb.toString());
        logMapper.insert(log);
        orderBatchSendDetail.setOrderNo(order.getOrderNo());
        orderBatchSendDetail.setBatchId(0l);
        orderBatchSendDetail.setCreateTime(new Date());
        orderBatchSendDetail.setShopId(order.getShopId());
        return orderBatchSendDetailMapper.insert(orderBatchSendDetail);
    }

    private boolean checkDeliveryStatus(EsShopOrder order) {
        int orderGoodsCount = 0;//订单商品种类数量
        int orderSendGoodsCount = 0;//发货商品种类数量
        String[] orderGoodsIds = order.getGoodsInfo().split(",");
        if (orderGoodsIds != null) {
            orderGoodsCount = orderGoodsIds.length;
        }

//        List<EsShopOrderGoods> order_id = shopOrderGoodsService.list(new QueryWrapper<>(new EsShopOrderGoods()).eq("order_id", order.getId()));

        List<EsShopOrderBatchSendDetail> sendDetails = orderBatchSendDetailMapper.selectList(new QueryWrapper<>(new EsShopOrderBatchSendDetail()).eq("order_id", order.getId()));
        for (EsShopOrderBatchSendDetail temp : sendDetails) {
            String[] tempsGoodsIds = temp.getGoodsIds().split(",");
            if (tempsGoodsIds != null) {
                orderSendGoodsCount = tempsGoodsIds.length;
            }
        }

        if (orderGoodsCount != orderSendGoodsCount) {
            return false;
        }

        return true;
    }

    /**
     * 用户购买此商品的个数，支付成功
     *
     * @param userId
     * @param goodsId
     * @return
     */
    public Integer getBuyCountByUserId(Long userId, Long goodsId) {
        return orderMapper.getBuyCountByUserId(userId, goodsId);
    }

    /**
     * 用户购买此商品的个数，根据状态
     *
     * @param userId
     * @param goodsId
     * @return
     */
    public Integer getBuyCountByMemberId(Long userId, Long goodsId, Integer status) {
        return orderMapper.getBuyCountByMemberId(userId, goodsId, status);
    }

    @Override
    public UserOrderDetail selectByUserId(Long userId) {
        UserOrderDetail detail = new UserOrderDetail();
        EsShopOrder queryorder = new EsShopOrder();
        queryorder.setMemberId(userId);
        List<EsShopOrder> orders = orderMapper.selectList(new QueryWrapper<>(queryorder));

        int statusALl = 0; // 成交数量
        int status1 = 0;//维权中
        int status2 = 0;//退款

        BigDecimal payAmount = new BigDecimal(0);//累计金额
        BigDecimal refundAmout = new BigDecimal(
                0);//累计退款金额


        for (EsShopOrder order : orders) {
            order.setStatusEn(OrderUtils.orderStatus(order.getStatus()));
            if (OrderUtils.isPayStatus(order)) {
                statusALl++;
                payAmount = payAmount.add(order.getPayPrice());
            }
            if (order.getStatus() == OrderStatus.RIGHT_APPLY.getValue() || order.getStatus() == OrderStatus.RIGHT_APPLYF_SUCCESS.getValue()) {
                status1++;
            }
            if (order.getStatus() == OrderStatus.REFUND.getValue()) {
                status2++;
                refundAmout = refundAmout.add(order.getRefundPrice());
            }
        }

        detail.setOrderList(orders);
        detail.setPayAmount(payAmount);
        detail.setRefundAmout(refundAmout);
        detail.setStatusALl(statusALl);
        detail.setStatusRefund(status2);
        detail.setStatusRight(status1);
        detail.setStatusALl(statusALl);

        return detail;
    }

    @Override
    public EsShopOrder detail(Long orderId) {
        EsShopOrder order = orderMapper.selectOrderItemById(orderId);

        EsShopOrderGoods goodsO = new EsShopOrderGoods();
        goodsO.setOrderId(orderId);
        List<EsShopOrderGoods> orderGoodss = order.getOrderGoodsList();
        /**
         * 赠礼类型
         * 1 首赠礼 2 满赠礼 3 单品礼赠 4 验证码赠品 5.赠品优惠券的礼品
         *
         */
        List<EsShopOrderGoods> gift1 = new ArrayList<>();
        List<EsShopOrderGoods> gift2 = new ArrayList<>();
        List<EsShopOrderGoods> gift3 = new ArrayList<>();
        List<EsShopOrderGoods> gift4 = new ArrayList<>();
        List<EsShopOrderGoods> gift5 = new ArrayList<>();
        List<EsShopOrderGoods> gift6 = new ArrayList<>();
        List<EsShopOrderGoods> orderGoods = new ArrayList<>();
//        List<EsShopOrderGoods> orderGoodss = orderGoodsMapper.selectList(new QueryWrapper<>(goodsO));
        for (EsShopOrderGoods og : orderGoodss) {
            if (og.getIsGifts() == 1) {
                if (og.getGiftType() == 1) {
                    gift1.add(og);
                }
                if (og.getGiftType() == 2) {
                    gift2.add(og);
                }
                if (og.getGiftType() == 3) {
                    gift3.add(og);
                }
                if (og.getGiftType() == 4) {
                    gift4.add(og);
                }
                if (og.getGiftType() == 5) {
                    gift5.add(og);
                }
                if (og.getGiftType() == 6) {
                    gift6.add(og);
                }
            } else {
                if (og.getGoodsId() != null) { //多规格从规格表拿
                    EsShopGoods goods = goodsFegin.getGoodsById(og.getGoodsId());
                    if (og.getOptionId() != null && og.getOptionId() > 0) {
                        EsShopGoodsOption tempSkuGoods = getSkuByGoods(goods, og.getOptionId());
                        if (tempSkuGoods != null) {
                            if (tempSkuGoods.getSpecs() == null || "".equals(tempSkuGoods.getSpecs()))
                                tempSkuGoods.setSpecs("无");
                            if (tempSkuGoods.getProductsn() == null || "".equals(tempSkuGoods.getProductsn()))
                                tempSkuGoods.setProductsn("无");
                            if (tempSkuGoods.getGoodsCode() == null || "".equals(tempSkuGoods.getGoodsCode()))
                                tempSkuGoods.setGoodsCode("无");
                        }
                        goods.setOption(tempSkuGoods);
                    } else { //订单商品单规格从goods拿
                        EsShopGoodsOption temp = new EsShopGoodsOption();
                        temp.setSpecs("无");
                        temp.setGoodsCode(goods.getGoodsCode());
                        temp.setProductsn(goods.getBarCode());
                        if (temp.getGoodsCode() == null || "".equals(temp.getGoodsCode())) {
                            temp.setGoodsCode("无");
                        }
                        if (temp.getProductsn() == null || "".equals(temp.getProductsn())) {
                            temp.setProductsn("无");
                        }
                        goods.setOption(temp);
                    }
                    og.setGoods(goods);
                }
                orderGoods.add(og);
            }
        }

        if (order.getIsPartDelivery() == 0) {
            if (order.getStatus() == OrderStatus.DELIVERED.getValue() || order.getStatus() == OrderStatus.TRADE_SUCCESS.getValue()) {
                EsShopOrderBatchSendDetail batchSendDetailTemp = new EsShopOrderBatchSendDetail();
                batchSendDetailTemp.setExpressSn(order.getExpressSn());
                batchSendDetailTemp.setOrderId(order.getId());
                order.setSendBatchDetail(orderBatchSendDetailMapper.selectOne(new QueryWrapper<>(batchSendDetailTemp)));
            } else {
                order.setSendBatchDetail(new EsShopOrderBatchSendDetail());
            }
        } else {
            //包裹信息处理
            EsShopOrderPackage pack = new EsShopOrderPackage();
            pack.setOrderId(orderId);
//        pack.setShopId(order.getShopId());//上线的时候 释放
//        pack.setMemberId(order.getMemberId());//上线的时候 释放
            List<EsShopOrderPackage> packageList = this.shopOrderPackageService.list(new QueryWrapper<>(pack));
            for (EsShopOrderPackage packItem : packageList) {
                List<EsShopOrderGoods> orderGoodsList = null;
                /*String[] goodsIds = packItem.getOrderGoodsIds().split(",");
                int len = goodsIds.length;
                for (int i = 0; i < len; i++) {
                    EsShopOrderGoods temp = new EsShopOrderGoods();
                    temp.setOrderId(packItem.getOrderId());
                    temp.setGoodsId(Long.valueOf(goodsIds[i]));
                    orderGoodsList.add(orderGoodsMapper.selectOne(temp));
                }*/
                List<Long> goodsIdsToLongList = TypeCastUtils.stringToLongList(packItem.getOrderGoodsIds());
                orderGoodsList = shopOrderGoodsService.list(new QueryWrapper<>(new EsShopOrderGoods())
                        .eq("order_id", packItem.getOrderId()).in("goods_id", goodsIdsToLongList));
                packItem.setOrderGoodsList(orderGoodsList);
                EsShopOrderBatchSendDetail sendDetailTemp = new EsShopOrderBatchSendDetail();
                sendDetailTemp.setExpressSn(packItem.getExpressSn());
                sendDetailTemp.setOrderId(packItem.getOrderId());
                packItem.setBatchSendDetail(orderBatchSendDetailMapper.selectOne(new QueryWrapper<>(sendDetailTemp)));

            }
            order.setPackageList(packageList);
        }


        if (gift1 == null || gift1.size() == 0) {
            gift1 = null;
        }
        if (gift2 == null || gift2.size() == 0) {
            gift2 = null;
        }
        if (gift3 == null || gift3.size() == 0) {
            gift3 = null;
        }
        if (gift4 == null || gift4.size() == 0) {
            gift4 = null;
        }
        if (gift5 == null || gift5.size() == 0) {
            gift5 = null;
        }
        if (gift6 == null || gift6.size() == 0) {
            gift6 = null;
        }

        order.setGift1(gift1);
        order.setGift2(gift2);
        order.setGift3(gift3);
        order.setGift4(gift4);
        order.setGift5(gift5);
        order.setGift6(gift6);
        order.setOrderGoodsList(orderGoods);

        //赋值定制服务数据

        List<Long> custIds = getCustIds(order.getOrderGoodsList());//定制服务id
        List<Long> basicIds = new ArrayList<>();//定制服务基础basic 实体 ids
        List<EsShopCustomizedApplet> customizedAppletList = new ArrayList<>();
        if (custIds != null && custIds.size() != 0) {
            customizedAppletList = appletMakedService.list(new QueryWrapper<>(new EsShopCustomizedApplet())
                    .in("id", custIds));
        }
        // 存储每个商品的定制服务信息 key：cartId value : 商品定制服务信息7390
        Map<Long, OrderGoodsCustMakingInfo> custGoodsInfoMap = new HashMap<>();
        if (customizedAppletList.size() != 0 && customizedAppletList != null) {
            for (EsShopCustomizedApplet item : customizedAppletList) {
                OrderGoodsCustMakingInfo custGoods = new OrderGoodsCustMakingInfo();
                custGoods.setLetterInfo(item.getBasicContent());//刻字内容
                custGoods.setMessage(item.getLeavingMessage());//留言
                custGoods.setCardMessage(item.getCardMessage());//卡片寄语
                EsShopCustomizedPacket packInfoByOne = goodsFegin.getPackInfoByOne(item.getPacketId());
                if (packInfoByOne != null) {
                    if (packInfoByOne.getType() == 1) {//封套1包装盒2
                        custGoods.setEntire(packInfoByOne.getImg());
                        custGoods.setPacking("无");
                    } else if (packInfoByOne.getType() == 2) {
                        custGoods.setEntire("无");
                        custGoods.setPacking(packInfoByOne.getImg());
                    } else {
                        custGoods.setEntire("无");
                        custGoods.setPacking("无");
                    }

                }
                custGoodsInfoMap.put(item.getId(), custGoods);
                basicIds.add(item.getBasicId());
            }

            //赋值订单商品定制服务信息
            for (EsShopOrderGoods item1 : order.getOrderGoodsList()) {
                if (item1.getCustId() != null) {
                    item1.setMakingInfo(custGoodsInfoMap.get(item1.getCustId()));
                }
            }

        }

        //计算定制服务商品总价
        BigDecimal custTotalPrice = new BigDecimal(0);
        if (basicIds != null && basicIds.size() != 0) {
            List<EsShopCustomizedBasic> custList = goodsFegin.getCustBasicByIds(basicIds);
            if (custList.size() != 0 && custList != null) {
                for (EsShopCustomizedBasic basicItem : custList) {
                    custTotalPrice = custTotalPrice.add(basicItem.getPrice());
                }
            }
            //赋值总的定制服务商品价格
            order.setCustPrice(custTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            order.setCustPrice(BigDecimal.valueOf(0.0));
        }

        return order;
    }

    private List<Long> getCustIds(List<EsShopOrderGoods> orderGoods) {
        List<Long> custIds = new ArrayList<>();
        for (EsShopOrderGoods item : orderGoods) {
            if (item.getCustId() != null) {
                custIds.add(item.getCustId());
            }
        }
        return custIds;
    }

    private List<Long> getCartIds(List<EsShopOrderGoods> orderGoods) {
        List<Long> cartIds = new ArrayList<>();
        for (EsShopOrderGoods item : orderGoods) {
            if (item.getCartId() != null) {
                cartIds.add(item.getCartId());
            }
        }
        return cartIds;
    }

    @Override
    public OrderDetail orderDetail(Long id) {
        EsShopOrder order = orderMapper.selectById(id);
        order.setStatusEn(OrderUtils.orderStatus(order.getStatus()));
        EsShopFriendGift list = markingFegin.list();
        order.setSharePhotos(list.getSharePhotos());
        order.setShareTitle(list.getShareTitle());
        List<EsShopOrderGoods> orderGoods1 = orderGoodsMapper.selectList(new QueryWrapper<>(new EsShopOrderGoods()).eq("order_id", id));
        int goodsCount = 0;
        /**
         * 赠礼类型
         * 1 首赠礼 2 满赠礼 3 单品礼赠 4 验证码赠品 5.赠品优惠券的礼品
         *
         */
        List<EsShopOrderGoods> gift1 = new ArrayList<>();
        List<EsShopOrderGoods> gift2 = new ArrayList<>();
        List<EsShopOrderGoods> gift3 = new ArrayList<>();
        List<EsShopOrderGoods> gift4 = new ArrayList<>();
        List<EsShopOrderGoods> gift5 = new ArrayList<>();
        List<EsShopOrderGoods> gift6 = new ArrayList<>();
        List<EsShopOrderGoods> orderGoods = new ArrayList<>();
        for (EsShopOrderGoods goods : orderGoods1) {
            if (goods.getIsGifts() == 1) {
                if (goods.getGiftType() == 1) {
                    gift1.add(goods);
                }
                if (goods.getGiftType() == 2) {
                    gift2.add(goods);
                }
                if (goods.getGiftType() == 3) {
                    gift3.add(goods);
                }
                if (goods.getGiftType() == 4) {
                    gift4.add(goods);
                }
                if (goods.getGiftType() == 5) {
                    gift5.add(goods);
                }
                if (goods.getGiftType() == 6) {
                    gift6.add(goods);
                }
            } else {
                orderGoods.add(goods);
                goodsCount += goods.getCount();
            }

        }
        order.setGoodsCount(goodsCount);
        if (order.getIsPartDelivery() == 0) {
            if (order.getStatus() == OrderStatus.DELIVERED.getValue() || order.getStatus() == OrderStatus.TRADE_SUCCESS.getValue()) {
                EsShopOrderBatchSendDetail batchSendDetailTemp = new EsShopOrderBatchSendDetail();
                batchSendDetailTemp.setExpressSn(order.getExpressSn());
                batchSendDetailTemp.setOrderId(order.getId());
                order.setSendBatchDetail(orderBatchSendDetailMapper.selectOne(new QueryWrapper<>(batchSendDetailTemp)));
            } else {
                order.setSendBatchDetail(new EsShopOrderBatchSendDetail());
            }
        } else {
            //包裹信息处理
            EsShopOrderPackage pack = new EsShopOrderPackage();
            pack.setOrderId(id);
//        pack.setShopId(order.getShopId());//上线的时候 释放
//        pack.setMemberId(order.getMemberId());//上线的时候 释放
            List<EsShopOrderPackage> packageList = this.shopOrderPackageService.list(new QueryWrapper<>(pack));
            for (EsShopOrderPackage packItem : packageList) {
                List<EsShopOrderGoods> orderGoodsList = null;

                List<Long> goodsIdsToLongList = TypeCastUtils.stringToLongList(packItem.getOrderGoodsIds());
                orderGoodsList = shopOrderGoodsService.list(new QueryWrapper<>(new EsShopOrderGoods())
                        .eq("order_id", packItem.getOrderId()).in("goods_id", goodsIdsToLongList));
                packItem.setOrderGoodsList(orderGoodsList);
                EsShopOrderBatchSendDetail sendDetailTemp = new EsShopOrderBatchSendDetail();
                sendDetailTemp.setExpressSn(packItem.getExpressSn());
                sendDetailTemp.setOrderId(packItem.getOrderId());
                packItem.setBatchSendDetail(orderBatchSendDetailMapper.selectOne(new QueryWrapper<>(sendDetailTemp)));

            }
            order.setPackageList(packageList);
        }

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setGift1(gift1);
        orderDetail.setGift2(gift2);
        orderDetail.setGift3(gift3);
        orderDetail.setGift4(gift4);
        orderDetail.setGift5(gift5);
        orderDetail.setGift6(gift6);
        orderDetail.setOrderGoodsList(orderGoods);
        EsShopFriendGiftCard FriendGiftCard = markingFegin.GiftCard(order.getGiftId());
        orderDetail.setGiftCard(FriendGiftCard);

//赋值定制服务数据

        List<Long> custIds = getCustIds(orderGoods);//定制服务id
        List<Long> basicIds = new ArrayList<>();//定制服务基础basic 实体
        List<EsShopCustomizedApplet> customizedAppletList = new ArrayList<>();
        if (custIds != null && custIds.size() != 0) {
            customizedAppletList = appletMakedService.list(new QueryWrapper<>(new EsShopCustomizedApplet())
                    .in("id", custIds));
        }

        // 存储每个商品的定制服务信息 key：cartId value : 商品定制服务信息7390
        Map<Long, OrderGoodsCustMakingInfo> custGoodsInfoMap = new HashMap<>();
        if (customizedAppletList.size() != 0 && customizedAppletList != null) {
            for (EsShopCustomizedApplet item : customizedAppletList) {
                OrderGoodsCustMakingInfo custGoods = new OrderGoodsCustMakingInfo();
                custGoods.setLetterInfo(item.getBasicContent());//刻字内容
                custGoods.setMessage(item.getLeavingMessage());//留言
                custGoods.setCardMessage(item.getCardMessage());//卡片寄语
                EsShopCustomizedPacket packInfoByOne = goodsFegin.getPackInfoByOne(item.getPacketId());
                if (packInfoByOne != null) {
                    if (packInfoByOne.getType() == 1) {//封套1包装盒2
                        custGoods.setEntire(packInfoByOne.getImg());
                        custGoods.setPacking("无");
                    } else if (packInfoByOne.getType() == 2) {
                        custGoods.setEntire("无");
                        custGoods.setPacking(packInfoByOne.getImg());
                    } else {
                        custGoods.setEntire("无");
                        custGoods.setPacking("无");
                    }

                }
                custGoodsInfoMap.put(item.getId(), custGoods);
                basicIds.add(item.getBasicId());
            }

            //赋值订单商品定制服务信息
            for (EsShopOrderGoods item1 : orderGoods) {
                if (item1.getCustId() != null) {
                    item1.setMakingInfo(custGoodsInfoMap.get(item1.getCustId()));
                }
            }

        }

        //计算定制服务商品总价
        BigDecimal custTotalPrice = new BigDecimal(0);
        if (basicIds != null && basicIds.size() != 0) {
            List<EsShopCustomizedBasic> custList = goodsFegin.getCustBasicByIds(basicIds);
            if (custList.size() != 0 && custList != null) {
                for (EsShopCustomizedBasic basicItem : custList) {
                    custTotalPrice = custTotalPrice.add(basicItem.getPrice());
                }
            }
            //赋值总的定制服务商品价格
            order.setCustPrice(custTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            order.setCustPrice(BigDecimal.valueOf(0));
        }


        return orderDetail;
    }

    @Override
    public PageInfo selectPageExt(OrderParam entity) {
        Page<EsShopOrder> page = new Page<EsShopOrder>(entity.getCurrent(), entity.getSize());
        //  page.setAsc(entity.getIsAsc() == 0 ? false : true);
        List<EsShopOrder> orderList = null;
        Integer total = 0;
        entity.setEndTime(entity.getEndTime() + " 23:59:59.0");

        if ("4".equals(entity.getKeyType()) || "5".equals(entity.getKeyType())) {
            // 先查找满足 orderids 多条件 查找og  获得og 中 order_id
            //再根据order 表里的条件， 实体，
            List<Long> orderIdsByOg = orderGoodsMapper.selOgByCondition(entity);//获得 ordergood 中的 orderId
            entity.setOrderIds(orderIdsByOg);
            if (orderIdsByOg != null && orderIdsByOg.size() != 0) {
                orderList = this.orderMapper.selOrderByPage(entity);
             //   total = orderMapper.getCountByPage(entity);
                for (EsShopOrder orderItem : orderList) {
                    orderItem.setOrderGoodsList(orderGoodsMapper.selectList
                            (new QueryWrapper<>(new EsShopOrderGoods()).eq("order_id", orderItem.getId())));
                }
            } else {
                orderList = new ArrayList<>();
                total = 0;
            }
        } else {
            // 单表查询
            PageHelper.startPage(entity.getCurrent(), entity.getSize());
            orderList = orderMapper.getOrderListByCon(entity);

            for (EsShopOrder order : orderList) {
                order.setOrderGoodsList(orderGoodsMapper.selectList(new QueryWrapper<>(new EsShopOrderGoods()).eq("order_id", order.getId())));
            }
        }

        return PageInfo.of(orderList);
    }

    //好友赠礼查询
    @Override
    public List<EsShopOrder> selectfriend(OrderParam entity) {
        List<EsShopOrder> orderList = null;
        orderList = orderMapper.getOrderListByCon(entity);
        //total = orderMapper.getCountByCon(entity);
        for (EsShopOrder order : orderList) {
            order.setOrderGoodsList(orderGoodsMapper.selectList(new QueryWrapper<>(new EsShopOrderGoods()).eq("order_id", order.getId())));
        }
        return orderList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, Integer status) {
        EsShopOperationLog log1 = new EsShopOperationLog();
        EsShopOrder vo = orderMapper.selectById(id);
        StringBuffer sb = new StringBuffer();
        sb.append(id + "订单状态由'" + OrderUtils.orderStatus(vo.getStatus()) + "'(" + vo.getStatus() + ")变为'"
                + OrderUtils.orderStatus(status) + "(" + status + ")'");
        log1.setCreateTime(new Date());
        log1.setOperationDesc(sb.toString());
        vo.setStatus(status);
        logMapper.insert(log1);
        orderMapper.updateById(vo);
        CompletableFuture.runAsync(() -> {
            try {
                String formid = this.getFormIdByMemberId(vo.getMemberId());

                if (ValidatorUtils.notEmpty(formid)) {
                    push(membersFegin.getMemberById(vo.getMemberId()), vo, formid, 5, null);
                } else {
                    log.error("修改订单状态消息推送formId不够,memberId=" + vo.getMemberId());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });

        calMemberOrderInfo(vo.getMemberId());
        return true;
    }

    /**
     * 重新计算会员的订单信息
     *
     * @param memberId
     */
    private void calMemberOrderInfo(Long memberId) {
        EsShopOrder queryorder = new EsShopOrder();
        queryorder.setMemberId(memberId);
        List<EsShopOrder> orders = orderMapper.selectList(new QueryWrapper<>(queryorder));
        int statusALl = 0;
        BigDecimal payAmount = new BigDecimal(0);

        for (EsShopOrder order : orders) {
            if (OrderUtils.isPayStatus(order)) {
                statusALl++;
                payAmount = payAmount.add(order.getPayPrice());
            }
        }
        EsMember user = new EsMember();
        user.setId(memberId);
        user.setBuyCount(statusALl);
        user.setBuyMoney(payAmount);
        membersFegin.updateMemberOrderById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrder(EsShopOrder order) throws Exception {


        order.setRefundPrice(order.getPayPrice());
        order.setMemberId(1l);//后续完善.
//        order.setSendTime(null);
//        order.setAutoFinishTime(DateUtils.toDate(DateUtils.addDay(order.getSendTime(), 7)));
//        order.setAutoCloseTime(DateUtils.toDate(DateUtils.addDay(order.getSendTime(), 30)));
        order.setRemarkCloseSaler("No");

        order.setRemarkCloseBuyer("");//0
        order.setBuyerMobile("123456789");//后续完善
        order.setExpressSn(String.valueOf(IdWorker.getId()));

        order.setAddressId(1l);//后续完善
        order.setCreateTime(new Date());
        order.setSoureType(1);

        this.save(order);
        System.out.println("实体id：" + order.getId());
        String goodsInfo = order.getGoodsInfo();//订单购买的商品信息
        //处理逻辑
        List<EsShopOrderGoods> goodsList = order.getOrderGoodsList();
        List<Long> ids = new ArrayList<Long>();//订单上的商品id
        for (EsShopOrderGoods obj : goodsList) {
            ids.add(obj.getGoodsId());
            obj.setOrderId(order.getId());
            obj.setCreateTime(new Date());


            obj.setRefundStatus(0);
            obj.setRefundType(0);
            obj.setAddCredit(1);
            obj.setIsJoinMemberPrice(0);
            obj.setPriceOriginal(new BigDecimal(18.20));
            obj.setUnit("0.00");
            obj.setStatus(0);
            obj.setPackageCancelReason("");
            obj.setMemberId(order.getMemberId());
            obj.setDeductCredit(0);
            obj.setCommentStatus(0);
        }
//          判断订单里面的这个商品 是否存在!
        /*if(orderGoodsMapper.selectGoodsByIds(ids) != ids.size()){
            return false;
        }*/
        return shopOrderGoodsService.saveBatch(goodsList);
    }

    @Override
    public boolean deleteOrder(Long id) {
        return updateStatus(id, OrderStatus.DELETED.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeOrder(Long id) {
        EsShopOrder vo = orderMapper.selectById(id);
        releaseStock(id);
        CompletableFuture.runAsync(() -> {
            try {
                if (ValidatorUtils.notEmpty(vo.getCode())) {
                    markingFegin.updateCodeStatus(vo.getCode(), 1);
                }
                if (ValidatorUtils.notEmpty(vo.getCouponId())) {
                    markingFegin.updateMemberCoupon(vo.getCouponId(), vo.getMemberId(), null, 1);
                }
                String formid = this.getFormIdByMemberId(vo.getMemberId());

                if (ValidatorUtils.notEmpty(formid)) {
                    push(membersFegin.getMemberById(vo.getMemberId()), vo, formid, 3, null);
                } else {
                    log.error("关闭订单状态消息推送formId不够,memberId=" + vo.getMemberId());
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        if (vo.getStatus() == OrderStatus.INIT.getValue()) {
            vo.setStatus(OrderStatus.CLOSED.getValue());
            return orderMapper.updateById(vo) > 0;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePrice(EsShopOrder newE, BigDecimal price) {
        EsShopOperationLog log = new EsShopOperationLog();
        StringBuffer sb = new StringBuffer();
        sb.append(newE.getId() + "订单价格由" + newE.getPayPrice() + "变为" + price);
        log.setCreateTime(new Date());
        log.setOperationDesc(sb.toString());
//        log.setShopId(newE.getShopId());
        log.setShopId(1l);
        //log 一些设置
        newE.setChangePrice(price);
        if (ValidatorUtils.empty(newE.getChangePrice()) && newE.getChangePrice() != new BigDecimal(0.00)) {
            newE.setOriginalPrice(newE.getPayPrice());
        }
        newE.setPayPrice(price);
        logMapper.insert(log);
        EsShopOrderPriceChangeLog changeLog = new EsShopOrderPriceChangeLog();
        changeLog.setCreateTime(new Date());
        changeLog.setOrderId(newE.getId());
//        changeLog.setUid(newE.getUid());
//        changeLog.setShopId(newE.getShopId());
        changeLog.setShopId(1l);
        changeLog.setBeforePrice(newE.getPayPrice());
        changeLog.setAfterPrice(price);
        changeLog.setChangePrice(price);
        changeLogMapper.insert(changeLog);
        return orderMapper.updateById(newE) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object updateDelivery(EsShopOrderBatchSendDetail orderBatchSendDetail) {
        try {
            EsShopOrder order = this.orderMapper.selectById(orderBatchSendDetail.getOrderId());

            if (order == null) {
                return new CommonResult().failed("不存在id为{" + orderBatchSendDetail.getOrderId() + "}的订单");
            }
            if (order.getStatus() != OrderStatus.DELIVERED.getValue()) {
                return new CommonResult().paramFailed("已发货订单的物流信息才能修改");
            }
            if (order.getExpressSn().equals(orderBatchSendDetail.getExpressSn())) {
                return new CommonResult().paramFailed("两次快递单号不能一致");
            }
            Integer isPartDelivery = order.getIsPartDelivery();//是否部分发货标识
            if (isPartDelivery == 0) {//非部分发货
                EsShopOrderBatchSendDetail temp = new EsShopOrderBatchSendDetail();
                temp.setOrderId(orderBatchSendDetail.getOrderId());
                EsShopOrderBatchSendDetail old = orderBatchSendDetailMapper.selectOne(new QueryWrapper<>(temp));
                EsShopOperationLog log = new EsShopOperationLog();
                StringBuffer sb = new StringBuffer();
                sb.append(orderBatchSendDetail.getOrderId() + "订单物流信息" + old.getExpressName() + "," + old.getExpressSn() + "变为" + orderBatchSendDetail.getExpressName() + "," + orderBatchSendDetail.getExpressSn());
                log.setCreateTime(new Date());
                log.setOperationDesc(sb.toString());
                logMapper.insert(log);
            } else {

            }

            orderBatchSendDetailMapper.updateById(orderBatchSendDetail);
            return new CommonResult().success();
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object confimDelivery(Long id) {
        EsShopOrder order = this.orderMapper.selectById(id);
        if (order.getStatus() != OrderStatus.DELIVERED.getValue()) {
            return new CommonResult().paramFailed("已发货订单才能确认收货");
        }
        order.setStatus(OrderStatus.TRADE_SUCCESS.getValue());
        order.setFinishTime(new Date());
        orderMapper.updateById(order);

        EsShopOperationLog log1 = new EsShopOperationLog();
        StringBuffer sb = new StringBuffer();
        sb.append(id + "订单确认收获");
        log1.setCreateTime(new Date());
        log1.setOperationDesc(sb.toString());
        logMapper.insert(log1);
        CompletableFuture.runAsync(() -> {
            try {
                List<EsShopOrderGoods> orderGoods = orderGoodsMapper.selectList(
                        new QueryWrapper<>(new EsShopOrderGoods()).eq("order_id", order.getId()).eq("is_gifts", 0));
                EsMember user = membersFegin.getMemberById(order.getMemberId());
                if (this.hasCountByMemberById(user.getId()) > 0) {
                    markingFegin.sendNewCoupon(user.getId(), 2);
                }
                CartMarkingVo vo = new CartMarkingVo();
                vo.setMemberId(order.getMemberId());
                vo.setPayAmount(order.getPayPrice());
                vo.setShopOrderGoodsList(orderGoods);
                vo.setScope(1);
                vo.setOpenId(user.getOpenid());
                markingFegin.sendManualCoupon(vo);
                markingFegin.sendFillFillCoupon(vo);
                markingFegin.sendShopCoupon(vo);

            } catch (Exception e) {
                log.error("确认收货异步更新失败：{}", e.getMessage());
            }
        });
        return new CommonResult().success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancleDelivery(EsShopOrder order, String remark) {
        EsShopOrderBatchSendDetail query = new EsShopOrderBatchSendDetail();
        query.setOrderId(order.getId());
        query.setExpressSn(order.getExpressSn());
        EsShopOperationLog log = new EsShopOperationLog();
        StringBuffer sb = new StringBuffer();
        sb.append(order.getId() + "订单取消发货" + order.getStatus());
        log.setCreateTime(new Date());
        log.setOperationDesc(sb.toString());
        logMapper.insert(log);
        orderBatchSendDetailMapper.delete(new QueryWrapper<>(query));
        order.setStatus(OrderStatus.TO_DELIVER.getValue());
        return orderMapper.updateById(order);
    }

    @Override
    public boolean exportOrderList(OrderParam entity, ExportParam exportParam, HttpServletResponse response) {
       /* Page page = new Page(entity.getCurrent(), entity.getSize());
        List<EsShopOrder> data = this.selectPageExt(entity).getRecords();
        try {
            ExportExcelWrapper<EsShopOrder> export = new ExportExcelWrapper<EsShopOrder>();
            export.exportExcel(exportParam.getFileName(), exportParam.getSheetName(), exportParam.getHeaders().split(","), exportParam.getColumns().split(","), data, response, ExportExcelUtil.EXCEl_FILE_2007);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }*/
        return true;
    }

    @Override
    public Object testFind() {
        return null;
    }

    @Override
    public BigDecimal addressToFee(BookOrderParam orderParam) {
        BigDecimal price = new BigDecimal(0);
        String type = orderParam.getType();
        List<EsShopCart> cartPromotionItemList = new ArrayList<>();
        List<EsShopCart> cartItemList = new ArrayList<>();
        // 1 商品详情 2 勾选购物车 3全部购物车的商品
        if ("1".equals(type)) {
            getCartByGoodsDetail(orderParam, cartPromotionItemList);

        } else if ("2".equals(type)) {
            String cartIdList1 = orderParam.getCartIds();
            if (org.apache.commons.lang.StringUtils.isBlank(cartIdList1)) {
                return price;
            }
            String[] ids1 = cartIdList1.split(",");
            List<Long> resultList = new ArrayList<>(ids1.length);
            for (String s : ids1) {
                resultList.add(Long.valueOf(s));
            }
            cartPromotionItemList = cartMapper.selectBatchIds(resultList);
        }

        for (EsShopCart cart : cartPromotionItemList) {
            EsShopGoods goods = goodsFegin.getGoodsById(cart.getGoodsId());
            if (ValidatorUtils.notEmpty(goods.getEdareas()) && goods.getEdareas().contains(orderParam.getAddressCity())) {

            } else {
                cartItemList.add(cart);
            }
        }
        int countDis = 0;
        for (EsShopCart cart : cartItemList) {
            EsShopGoods goods = goodsFegin.getGoodsById(cart.getGoodsId());
            boolean falg1 = false;
            boolean falg2 = false;
            if (ValidatorUtils.notEmpty(goods.getEdnum()) && goods.getEdnum() > 0) {
                if (countByCart(cartPromotionItemList, cart.getGoodsId()) >= goods.getEdnum()) {
                    falg1 = true;
                }
            }
            if (ValidatorUtils.notEmpty(goods.getEdmoney()) && goods.getEdmoney().compareTo(new BigDecimal(0)) > 0) {
                if (countByPrice(cartPromotionItemList, cart.getGoodsId()).compareTo(goods.getEdmoney()) >= 0) {
                    falg2 = true;
                }
            }
            if (!falg1 && !falg2 && ValidatorUtils.empty(goods.getUniformPostageStatus()) && ValidatorUtils.notEmpty(goods.getUniformPostagePrice())) {
                if (countDis == 0) {
                    price = goods.getUniformPostagePrice();
                }
                if (goods.getUniformPostagePrice().compareTo(price) > 0) {
                    price = goods.getUniformPostagePrice();
                }
                countDis++;
            }
        }
        return price;
    }

    @Override
    public List<EsShopOrder> getNewCustOrderList(CustTendencyParam param) {
        return this.orderMapper.getNewOrderList(param);
    }

    @Override
    public List<EsShopOrder> getAllOrderInfo(CustTradeSuccessParam param) {
        return this.orderMapper.getAllOrderInfo(param);
    }

    @Override
    public List<EsShopOrder> getOldOrderInfo(CustTradeSuccessParam param) {
        return this.orderMapper.getOldOrderIfo(param);
    }

    @Override
    public List<EsShopOrder> getNewOrderInfo(CustTradeSuccessParam param) {
        return this.orderMapper.getNewOrderInfo(param);
    }

    @Override
    public OrderCustTotalVo getCustOrderInfoByCon(CustTradeSuccessParam param) {
        TradeAnalyzeParam tempParam = new TradeAnalyzeParam();
        tempParam.setStartTime(param.getStartTime());
        tempParam.setEndTime(param.getEndTime());
        tempParam.setSource(param.getSource());
        tempParam.setShopId(param.getShopId());

        OrderCustTotalVo oldConsumeInfo = orderMapper.getOldConsumeInfo(tempParam);//老客户金额 老客户总人数
        OrderCustTotalVo totalInfo = orderMapper.getTotalInfo(tempParam);//获得总金额 总人数
        if (oldConsumeInfo == null) {
            oldConsumeInfo = new OrderCustTotalVo();
        }
        if (totalInfo == null) {
            totalInfo = new OrderCustTotalVo();
        }
        oldConsumeInfo.setCount(totalInfo.getCount());
        oldConsumeInfo.setNewClientCount(totalInfo.getCount() - oldConsumeInfo.getOldClientCount());
        oldConsumeInfo.setNewConsumeAmount(totalInfo.getTotal() - oldConsumeInfo.getOldConsumeAmount());
        oldConsumeInfo.setRelationEndDate(param.getEndTime());
        oldConsumeInfo.setTotal(totalInfo.getTotal());


        return oldConsumeInfo;
    }

    @Override
    public Long selectExist(EsMember entity) {
        EsShopCart cart = new EsShopCart();
        cart.setMemberId(entity.getId());
        cart.setActivatyType(1);
        List<EsShopCart> carts = cartMapper.selectList(new QueryWrapper<>(cart));
        if (carts != null) {
            for (EsShopCart car : carts) {
                EsShopGoods obj = (EsShopGoods) goodsFegin.getGoodsById(car.getGoodsId());
                if (obj != null) {
                    return obj.getSmallBeautyBoxId();
                } else {
                    return Long.parseLong("0");
                }
            }
            return Long.parseLong("0");
        } else {
            return Long.parseLong("0");
        }
    }

    @Override
    public int selGoodsTotalSaleCount(GoodsAnalyzeParam param) {
        //条件
        param.setStartTime(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS));
        param.setEndTime(param.getEndTime() + " 23:59:59.999");
        List<Long> orderIdList = orderMapper.orderIdList(param);
        //获得选择条件的订单， 然后累加商品数量。
        if (orderIdList != null && orderIdList.size() != 0) {
            return orderGoodsMapper.selGoodsTotalSaleCount(orderIdList);
        } else {
            return 0;
        }
    }

    @Override
    public List<EsShopOrderGoods> orderGoodsList(GoodsAnalyzeParam param) {
       /* param.setEndTime(param.getEndTime() + " 23:59:59.999");
        param.setStartTime(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS));*/
        List<EsShopOrderGoods> OrderGoods = orderGoodsMapper.OrderGoods(param);
        List<EsShopOrderGoods> purchaseGoods = orderGoodsMapper.purchaseGoods(param);
        for (EsShopOrderGoods or : purchaseGoods) {
            for (EsShopOrderGoods goods : OrderGoods) {
                if (or.getGoodscount() > 1 && goods.getGoodsId().longValue() == or.getGoodsId().longValue()) {
                    goods.setPurchaseNumber(goods.getPurchaseNumber() + 1);
                }
            }
        }
        System.out.println(OrderGoods.toString());
        return OrderGoods;
    }

    @Override
    public List<EsShopOrderGoods> orderGoodsList(long goodsId) {
        QueryWrapper<EsShopOrderGoods> condition = new QueryWrapper();
        condition.eq("goods_id", goodsId);
        return orderGoodsMapper.selectList(condition);
    }

    @Override
    public List<EsShopOrderGoods> selOrderGoodsByGoodsAnaly(GoodsTrendMapParam param) {
        //条件
        QueryWrapper<EsShopOrder> condition = new QueryWrapper(new EsShopOrder(), "id");
        condition.notIn("status", OrderStatus.INIT.getValue(), OrderStatus.CLOSED.getValue());
        if (ValidatorUtils.notEmpty(param.getStartTime()))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(param.getEndTime()))
            condition.le("create_time", Timestamp.valueOf(param.getEndTime()));
        if (ValidatorUtils.notEmpty(param.getSource()))
            condition.eq("soure_type", param.getSource());


        //获得选择条件的订单， 然后累加商品数量。
        List<Object> orderIds = this.listObjs(condition);
        List<Long> orderIdL = new ArrayList<>();

        this.listObjs(condition).forEach((item) -> {
            orderIdL.add((Long) item);
        });
        if (orderIdL != null && orderIdL.size() != 0) {
            return this.orderGoodsMapper.selectList(new QueryWrapper<>(new EsShopOrderGoods())
                    .in("order_id", orderIdL)
            );
        }

        return null;

    }

    @Override
    public Integer inserts(EsShopCart entity) {
        EsShopCart cart = new EsShopCart();
        cart.setMemberId(entity.getMemberId());
        cart.setActivatyType(1);
        Integer num = cartMapper.delete(new QueryWrapper<>(cart));
        if (num >= 0) {
            return cartMapper.insert(entity);
        } else {
            return 0;
        }

    }

    @Override
    public List<Integer> paymentNumber(EsShopOrder entity) {
        return orderMapper.paymentNumber(entity);
    }

    @Override
    public List<EsShopOrder> getOldCustOrderList(CustTendencyParam param) {
        return this.orderMapper.getOldOrderList(param);
    }

    /**
     * 计算邮费
     *
     * @param cartPromotionItemList
     * @param orderParam
     * @return
     */
    private BigDecimal getDispatchPrice(List<EsShopCart> cartPromotionItemList, BookOrderParam orderParam) {
        BigDecimal price = new BigDecimal(0);
        List<EsShopCart> cartItemList = new ArrayList<>();
        for (EsShopCart cart : cartPromotionItemList) {
            EsShopGoods goods = goodsFegin.getGoodsById(cart.getGoodsId());
            if (ValidatorUtils.notEmpty(goods.getEdareas()) && ValidatorUtils.notEmpty(orderParam.getAddressCity()) && ValidatorUtils.notEmpty(orderParam.getAddressProvince()) && (goods.getEdareas().contains(orderParam.getAddressCity().substring(0, 2)) || goods.getEdareas().contains(orderParam.getAddressProvince().substring(0, 2)))) {

            } else {
                cartItemList.add(cart);
            }
        }
        int countDis = 0;
        for (EsShopCart cart : cartItemList) {
            EsShopGoods goods = goodsFegin.getGoodsById(cart.getGoodsId());
            boolean falg1 = false;
            boolean falg2 = false;
            if (ValidatorUtils.notEmpty(goods.getEdnum()) && goods.getEdnum() > 0) {
                if (countByCart(cartPromotionItemList, cart.getGoodsId()) >= goods.getEdnum()) {
                    falg1 = true;
                }
            }
            if (ValidatorUtils.notEmpty(goods.getEdmoney()) && goods.getEdmoney().compareTo(new BigDecimal(0)) > 0) {
                if (countByPrice(cartPromotionItemList, cart.getGoodsId()).compareTo(goods.getEdmoney()) >= 0) {
                    falg2 = true;
                }
            }
            if (!falg1 && !falg2 && ValidatorUtils.empty(goods.getUniformPostageStatus()) && ValidatorUtils.notEmpty(goods.getUniformPostagePrice())) {
                if (countDis == 0) {
                    price = goods.getUniformPostagePrice();
                }
                if (goods.getUniformPostagePrice().compareTo(price) > 0) {
                    price = goods.getUniformPostagePrice();
                }
                countDis++;
            }
        }
        return price;
    }

    @Override
    public Object codeToGift(BookOrderParam orderParam) {
        String type = orderParam.getType();
        List<EsShopCart> cartPromotionItemList = new ArrayList<>();
        // 1 商品详情 2 勾选购物车 3全部购物车的商品
        if ("1".equals(type)) {
            getCartByGoodsDetail(orderParam, cartPromotionItemList);

        } else if ("2".equals(type)) {
            String cartIdList1 = orderParam.getCartIds();
            if (org.apache.commons.lang.StringUtils.isBlank(cartIdList1)) {
                return new CommonResult().paramFailed();
            }
            String[] ids1 = cartIdList1.split(",");
            List<Long> resultList = new ArrayList<>(ids1.length);
            for (String s : ids1) {
                resultList.add(Long.valueOf(s));
            }
            cartPromotionItemList = cartMapper.selectBatchIds(resultList);
        }
        CartMarkingVo vo1 = new CartMarkingVo();
        vo1.setCartList(cartPromotionItemList);
        vo1.setCode(orderParam.getCode());
        CodeResult rules = null;
        try {
            rules = markingFegin.getCodeGoods(vo1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommonResult().success(rules);
    }

    //订单详情
    @Override
    public Object preOrder(BookOrderParam orderParam) {
        String type = orderParam.getType();
        List<EsShopCart> cartPromotionItemList = new ArrayList<>();
        List<EsShopCart> cartItemList = new ArrayList<>();
        // 1 商品详情 2 勾选购物车 3全部购物车的商品
        StopWatch stopWatch = new StopWatch("预览订单");
        stopWatch.start("查询商品数据");
        if ("1".equals(type)) {
            getCartByGoodsDetail(orderParam, cartPromotionItemList);
            cartItemList = cartPromotionItemList;

        } else if ("2".equals(type)) {
            String cartIdList1 = orderParam.getCartIds();
            if (org.apache.commons.lang.StringUtils.isBlank(cartIdList1)) {
                return new CommonResult().paramFailed();
            }
            String[] ids1 = cartIdList1.split(",");
            List<Long> resultList = new ArrayList<>(ids1.length);
            for (String s : ids1) {
                resultList.add(Long.valueOf(s));
            }
            cartPromotionItemList = cartMapper.selectBatchIds(resultList);
            for (EsShopCart cart : cartPromotionItemList) {
                EsShopGoods goods = goodsFegin.getGoodsById(cart.getGoodsId());
                if (goods == null || goods.getId() == null) {
                    cartMapper.deleteById(cart.getId());
                    continue;
                }
                if (goods.getStatus() != 1) {
                    cartMapper.deleteById(cart.getId());
                    continue;
                }
                checkGoods(goods, false, cart.getTotal());
                if (!ValidatorUtils.empty(cart.getOptionId()) && cart.getOptionId() > 0) {
                    EsShopGoodsOption skuStock = getSkuByGoods(goods, cart.getOptionId());
                    if (skuStock == null) {
                        log.error("规格被删除：id=" + cart.getOptionId());
                        cartMapper.deleteById(cart.getId());
                        continue;
                    } else {
                        cart.setCategoryId(goods.getCategoryId());
                        cart.setPic(skuStock.getThumb());
                        cart.setGoodsName(skuStock.getGoodsName());
                        cart.setOptionName(skuStock.getTitle());
                        cart.setPrice(skuStock.getPrice());
                        checkSkuGoods(skuStock, cart.getTotal());
                    }
                    if (skuStock.getVirtualStock() <= 0) {
                        cartMapper.deleteById(cart.getId());
                        continue;
                    }
                } else {
                    if (goods.getVituralStock() <= 0) {
                        cartMapper.deleteById(cart.getId());
                        continue;
                    }
                    checkGoods(goods, true, cart.getTotal());
                    cart.setCategoryId(goods.getCategoryId());
                    cart.setPic(goods.getThumb());
                    cart.setGoodsName(goods.getTitle());
                    cart.setPrice(goods.getPrice());
                }
                EsShopCustomizedApplet applet = new EsShopCustomizedApplet();
                if (cart.getId() != null && cart.getCustId() != null) {
                    applet = esShopCustomizedAppletMapper.selectById(cart.getCustId());

                    if (applet != null && applet.getId() != null) {
                        applet.setCartId(cart.getId());
                        cart.setEsShopCustApplet(applet);
                    }
                }
                cartItemList.add(cart);
            }
        }
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        ConfirmOrderResult result = getConfirmOrderResult(orderParam.getMemberId(), cartItemList, orderParam);
        return new CommonResult().success(result);

    }

    @Override
    public Object friendOrder(BookOrderParam orderParam) {
        String type = orderParam.getType();
        List<EsShopCart> cartPromotionItemList = new ArrayList<>();
        List<EsShopCart> cartItemList = new ArrayList<>();

        if ("4".equals(type)) {
            getCartByGoodsDetail(orderParam, cartPromotionItemList);
            cartItemList = cartPromotionItemList;
            System.out.println("商品id" + cartItemList.toString());
        }
        StringBuffer goodsIds = new StringBuffer();
        EsShopCart cart = new EsShopCart();
        goodsIds.append(orderParam.getGoodsId());
        EsShopGoods goods = goodsFegin.getGoodsById(orderParam.getGoodsId());
        System.out.println("商品" + goods);


        //商品购买次数
        Integer goodsTotal = countByCart(cartPromotionItemList, orderParam.getGoodsId());

        if (goods.getNumMax() > 0 && goodsTotal > goods.getNumMax()) {
            return new CommonResult().failed("单次最多购买:" + goods.getNumMax());
        }
        if (goods.getNumMin() > 0 && goodsTotal < goods.getNumMin()) {
            return new CommonResult().failed("单次最少购买:" + goods.getNumMin());
        }
        Integer buyCount = getBuyCountByUserId(orderParam.getMemberId(), goods.getId());
        if (ValidatorUtils.empty(buyCount)) {
            buyCount = 0;
        }
        if (goods.getMostPurchases() > 0) {
            if (buyCount >= goods.getMostPurchases()) {
                return new CommonResult().failed(3, "最多购买:" + goods.getMostPurchases() + "件,当前已经购买件数:" + buyCount);
            }
            if (goodsTotal > (goods.getMostPurchases() - buyCount)) {
                return new CommonResult().failed("最多购买:" + goods.getMostPurchases() + "件,已经购买：" + buyCount + ",当前选择件数:" + goodsTotal);
            }
        }
        ConfirmOrderResult result = getConfirmOrderResult(orderParam.getMemberId(), cartItemList, orderParam);
        return new CommonResult().success(result);
    }

    @Override
    public Object friendbookOrder(BookOrderParam orderParam) throws Exception {
        BigDecimal moneyBasic = new BigDecimal("0");
        Date nowD = new Date();
        EsShopOrderSettings query = new EsShopOrderSettings();
        query.setShopId(orderParam.getShopId());
        EsShopOrderSettings orderSettings = orderSettingsMapper.selectOne(new QueryWrapper<>(query));
        EsMember member = membersFegin.getMemberById(orderParam.getMemberId());
        if (member == null || member.getId() == null) {
            return new CommonResult().failed("账号错误");
        }
        String type = orderParam.getType();
        //收货信息
        //  EsShopOrder order = createOrderObj(orderParam);
        EsShopOrder order = new EsShopOrder();
        order.setIsPartDelivery(0);
        //转化为订单信息并插入数据库
        order.setMemberId(orderParam.getMemberId());
        order.setCreateTime(new Date());
        //支付方式：1->微信小程序
        order.setPayType(0);//创建订单的时候 默认未支付
        //订单来源：0->小程序
        order.setSoureType(1);
        //订单状态：
        order.setStatus(OrderStatus.INIT.getValue());
        order.setGiftGiving(1);
        //收货人信息：姓名、电话、邮编、地址
        order.setRemarkBuyer(orderParam.getRemarkBuyer());
        //生成订单号
        order.setOrderNo(generateOrderSn(order));
        //订单备注
        order.setRemark(orderParam.getRemark());

        StringBuffer goodsIds = new StringBuffer();
        List<EsShopCart> cartPromotionItemList = new ArrayList<>();
        // 1 商品详情(立即购买) 2 勾选购物车 3全部购物车的商品 4.好友赠礼

        if ("4".equals(type)) {
            getCartByGoodsDetail(orderParam, cartPromotionItemList);
        }

        //获取购物车及优惠信息
        List<EsShopOrderGoods> orderItemList = new ArrayList<>();
        List<CartPromotionItem> promotionItemList = new ArrayList<>();

        BigDecimal markingAmount = new BigDecimal("0");//优惠金额
        BigDecimal couponAmount = new BigDecimal("0");//优惠金额
        BigDecimal totalAmount = new BigDecimal("0");//实付金额

        BigDecimal goodsTotalPrice = new BigDecimal("0");//订单商品总计
        // 邮费 取订单中需要邮费的商品中邮费最高
        int count = 0;
        order.setDispatchPrice(getDispatchPrice(cartPromotionItemList, orderParam));
        //StringBuffer cartIds = new StringBuffer();//购物车ids
        for (EsShopCart cart : cartPromotionItemList) {
            if (count == 0) {
                order.setGoodsName(cart.getGoodsName());
            }
            count++;
            goodsIds.append(cart.getGoodsId());
            EsShopGoods goods = goodsFegin.getGoodsById(cart.getGoodsId());
            EsShopOrderGoods orderItem = createOrderItemObj(cart, goods);
            orderItem.setTypeoption(orderParam.getTypeOption());
            orderItem.setTypeword(orderParam.getTypeword());
            Integer buyCount = getBuyCountByUserId(orderParam.getMemberId(), goods.getId());
            if (ValidatorUtils.empty(buyCount)) {
                buyCount = 0;
            }

            // 单品优惠
            BigDecimal cartPrice = cart.getPrice();
            if (ValidatorUtils.notEmpty(goods.getDiscount()) && Integer.parseInt(goods.getDiscount()) > 0) {
                BigDecimal dicont = new BigDecimal(Integer.parseInt(goods.getDiscount())).multiply(new BigDecimal(0.1));
                if (goods.getServiceConditions() == 0) {
                    buyCount = getBuyCountByMemberId(orderParam.getMemberId(), goods.getId(), OrderStatus.TRADE_SUCCESS.getValue());
                    if (ValidatorUtils.empty(buyCount)) {
                        buyCount = 0;
                    }
                }
                if (goods.getIsSustainedUse() == 1 && buyCount > 0) {
                    orderItem.setPriceDiscount(dicont);
                    cartPrice = cartPrice.multiply(dicont);
                }
                if (goods.getIsSustainedUse() == 0 && buyCount == 1) {
                    orderItem.setPriceDiscount(dicont);
                    cartPrice = cartPrice.multiply(dicont);
                }
            }
            //查询定制服务  去除刻字服务费
            EsShopCustomizedBasic esShopCustomizedBasic = new EsShopCustomizedBasic();
            EsShopCustomizedApplet applet = new EsShopCustomizedApplet();
            if (cart.getId() != null && cart.getCustId() != null) {
                applet = esShopCustomizedAppletMapper.selectById(cart.getCustId());
                if (applet != null && applet.getId() != null) {
                    applet.setCartId(cart.getId());
                    cart.setEsShopCustApplet(applet);
                    //查询刻字服务费
                    esShopCustomizedBasic = goodsFegin.detailBasics(applet.getBasicId());
                    //判断是否满额立减
                    if (esShopCustomizedBasic.getIsFull() == 1) {
                        if (cartPrice.compareTo(esShopCustomizedBasic.getFullPrice()) >= 0
                                && (ValidatorUtils.empty(esShopCustomizedBasic.getStartTime()) ||
                                (Long.parseLong(esShopCustomizedBasic.getStartTime()) <= nowD.getTime() &&
                                        Long.parseLong(esShopCustomizedBasic.getEndTime()) >= nowD.getTime()))) {
                            //刻字免费
                        } else {
                            moneyBasic = moneyBasic.add(esShopCustomizedBasic.getPrice().multiply(new BigDecimal(cart.getTotal())));
                        }
                    }
                }
            }
            goodsTotalPrice = goodsTotalPrice.add(cartPrice.multiply(new BigDecimal(cart.getTotal())));
            totalAmount = totalAmount.add(cartPrice.multiply(new BigDecimal(cart.getTotal())));

            if (!ValidatorUtils.empty(cart.getOptionId()) && cart.getOptionId() > 0) {
                checkGoods(goods, false, cart.getTotal());
                EsShopGoodsOption skuStock = getSkuByGoods(goods, cart.getOptionId());
                checkSkuGoods(skuStock, cart.getTotal());
            } else {
                checkGoods(goods, true, cart.getTotal());
            }
            //生成下单商品信息
            orderItem.setPriceUnit(cart.getPrice());//设置订单商品单价信息
            orderItem.setMemberDiscountPrice(cartPrice);
            orderItem.setCustId(cart.getCustId());
            orderItemList.add(orderItem);
            //  cartIds.append(cart.getId() + ",");
        }
        // cartIds.deleteCharAt(cartIds.length() - 1);
        //order.setCartIds(cartIds.toString());

        // 优惠计算 取 优惠更大的
        CartMarkingVo vo = new CartMarkingVo();
        vo.setCartList(cartPromotionItemList);

        BigDecimal manjianAmount = new BigDecimal("0");
        BigDecimal discountAmount = new BigDecimal("0");
        MjDcVo manjianList = markingFegin.matchManjian(cartPromotionItemList);
        if (ValidatorUtils.notEmpty(manjianList)) {
            manjianAmount = manjianList.getBasicAmount();
        }
        MjDcVo discountList = markingFegin.matchDiscount(cartPromotionItemList);
        if (ValidatorUtils.notEmpty(discountList)) {
            discountAmount = discountList.getBasicAmount();
        }
        markingAmount = discountAmount;
        if (manjianAmount.compareTo(discountAmount) > 0) {
            if (ValidatorUtils.notEmpty(manjianList)) {
                order.setManjianOrDiscountid(manjianList.getId());
                order.setManjianInfo(JsonUtils.toJsonStr(manjianList));
                markingAmount = manjianAmount;
            }
        } else {
            if (ValidatorUtils.notEmpty(discountList)) {
                order.setManjianOrDiscountid(discountList.getId());
                order.setDiscountInfo(JsonUtils.toJsonStr(discountList));
            }
        }

        addGiftGoodsOrder(orderParam, order, cartPromotionItemList, orderItemList, vo);

        //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额

        order.setCode(orderParam.getCode());
        BigDecimal payAmount2 = totalAmount.subtract(markingAmount);
        BigDecimal payAmount1 = payAmount2;
        order.setOriginalPrice(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        order.setOriginalGoodsPrice(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        order.setGoodsPrice(goodsTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        order.setRemarkBuyer(orderParam.getRemark());
        order.setReceiveType("快递");//默认快递
        order.setMemberRealname(member.getRealname());  //这里
        order.setMemberNickname(member.getNickname());
        order.setMemberMobile(member.getMobile());
        if (orderSettings != null) {
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE, orderSettings.getAutoCloseTime());
            order.setCloseTime(nowTime.getTime());
        } else {
            EsShopOrderSettings querySet = new EsShopOrderSettings();
            querySet.setShopId(orderParam.getShopId());
            EsShopOrderSettings orderSettings1 = orderSettingsMapper.selectOne(new QueryWrapper<>(querySet));
            redisRepository.set(orderParam.getShopId() + "", orderSettings1);
        }

        order.setGoodsIds(goodsIds.toString());//设置订单商品id
        order.setShopId(orderParam.getShopId());
        //判断使用使用了优惠券

        order.setDiscountAmount(markingAmount);
        if (payAmount1.compareTo(new BigDecimal(0)) < 0) {
            payAmount1 = new BigDecimal(0);
        }
        payAmount1 = payAmount1.add(order.getDispatchPrice()).add(moneyBasic);
        if (payAmount1.compareTo(new BigDecimal(0)) > 0) {
            order.setPayPrice(payAmount1.setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            order.setPayPrice(new BigDecimal(0.01));
        }
        order.setMoneyBasic(moneyBasic.setScale(2, BigDecimal.ROUND_HALF_UP));
        //插入order表和order_item表
        //好友赠礼订单
        order.setOrderType(2);
        order.setGiftId(orderParam.getGiftId());
        orderMapper.insert(order);
        for (EsShopOrderGoods orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderGoodsMapper.insert(orderItem);
        }
        if (ValidatorUtils.notEmpty(orderParam.getCouponId())) {
            markingFegin.updateMemberCoupon(orderParam.getCouponId(), order.getId(), order.getOrderNo(), 0);
        }
        OmsOrderVo orderVo = new OmsOrderVo();
        orderVo.setMember(member);
        orderVo.setOrder(order);
        orderVo.setOrderGoodsList(orderItemList);

        for (EsShopOrderGoods orderItem : orderItemList) {
            lockStock(orderItem, 0);
        }
        //删除购物车中的下单商品

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);


        CompletableFuture.runAsync(() -> {
            try {
                push(member, order, orderParam.getFormid(), 1, orderItemList);

                if (!hasOrder(orderParam.getMemberId())) {
                    markingFegin.sendNewCoupon(orderParam.getMemberId(), 2);
                }

            } catch (Exception e) {
                log.error("发送新人券下单失败：{}", e.getMessage());
            }
        });
        return new CommonResult().success("下单成功", result);
    }

    private void getCartByGoodsDetail(BookOrderParam orderParam, List<EsShopCart> cartPromotionItemList) {
        EsShopCart cart = new EsShopCart();
        EsShopGoods goods = goodsFegin.getGoodsById(orderParam.getGoodsId());
        if (ValidatorUtils.empty(orderParam.getOptionId())) {
            System.out.println("商品信息" + goods);
            checkGoods(goods, true, orderParam.getTotal());
            cart.setGoodsId(orderParam.getGoodsId());
            cart.setMemberId(orderParam.getMemberId());
            cart.setShopId(goods.getShopId());
            cart.setPic(goods.getThumb());
            cart.setGoodsName(goods.getTitle());
            cart.setPrice(goods.getPrice());
            cart.setTotal(orderParam.getTotal());
            cart.setType(orderParam.getTypeOption());
            cart.setTypeword(orderParam.getTypeword());
            cart.setCategoryId(goods.getCategoryId());

        } else {
            checkGoods(goods, false, orderParam.getTotal());
            EsShopGoodsOption sku = getSkuByGoods(goods, orderParam.getOptionId());
            checkSkuGoods(sku, orderParam.getTotal());
            cart.setGoodsId(orderParam.getGoodsId());
            cart.setMemberId(orderParam.getMemberId());
            cart.setCategoryId(goods.getCategoryId());
            cart.setShopId(sku.getShopId());
            cart.setOptionName(sku.getTitle());
            cart.setOptionId(orderParam.getOptionId());
            cart.setPic(sku.getThumb());
            cart.setGoodsName(sku.getGoodsName());
            cart.setPrice(sku.getPrice());
            cart.setTotal(orderParam.getTotal());
            cart.setType(orderParam.getTypeOption());
            cart.setTypeword(orderParam.getTypeword());
        }
        //    orderCartService.save(cart);//立即购买插入购物车
        cartPromotionItemList.add(cart);
    }

    /**
     * 此商品再购物车中的数量
     *
     * @param cartPromotionItemList
     * @param goodsId
     * @return
     */
    private Integer countByCart(List<EsShopCart> cartPromotionItemList, Long goodsId) {
        int count = 0;
        for (EsShopCart cart : cartPromotionItemList) {
            if (Objects.equals(cart.getGoodsId(), goodsId)) {
                count = count + cart.getTotal();
            }
        }
        return count;
    }

    /**
     * 此商品再购物车中的总金额
     *
     * @param cartPromotionItemList
     * @param goodsId
     * @return
     */
    private BigDecimal countByPrice(List<EsShopCart> cartPromotionItemList, Long goodsId) {
        BigDecimal count = BigDecimal.valueOf(0);
        for (EsShopCart cart : cartPromotionItemList) {
            if (Objects.equals(cart.getGoodsId(), goodsId)) {
                count = count.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())));
            }
        }
        return count;
    }

    private ConfirmOrderResult getConfirmOrderResult(Long memberId, List<EsShopCart> cartPromotionItemList, BookOrderParam orderParam) {
        ConfirmOrderResult result = new ConfirmOrderResult();

        try {
            BigDecimal totalAmount = new BigDecimal("0");
            BigDecimal moneyBasic = new BigDecimal("0");

            Date nowD = new Date();
            StopWatch stopWatch = new StopWatch("预览订单");
            stopWatch.start("查询运费");
            BigDecimal dispatchPrice = getDispatchPrice(cartPromotionItemList, orderParam);
            stopWatch.stop();
            stopWatch.start("查询定制数据");
            for (EsShopCart cart : cartPromotionItemList) {
                EsShopGoods goods = goodsFegin.getGoodsById(cart.getGoodsId());
                Integer buyCount = getBuyCountByUserId(orderParam.getMemberId(), goods.getId());
                if (ValidatorUtils.empty(buyCount)) {
                    buyCount = 0;
                }
                BigDecimal cartPrice = cart.getPrice();
                if (ValidatorUtils.notEmpty(goods.getDiscount()) && Integer.parseInt(goods.getDiscount()) > 0) {
                    BigDecimal dicont = new BigDecimal(Integer.parseInt(goods.getDiscount())).multiply(new BigDecimal(0.1));
                    if (goods.getServiceConditions() == 0) {
                        buyCount = getBuyCountByMemberId(orderParam.getMemberId(), goods.getId(), OrderStatus.TRADE_SUCCESS.getValue());
                        if (ValidatorUtils.empty(buyCount)) {
                            buyCount = 0;
                        }
                    }
                    if (goods.getIsSustainedUse() == 1 && buyCount > 0) {
                        cartPrice = cartPrice.multiply(dicont);
                    }
                    if (goods.getIsSustainedUse() == 0 && buyCount == 1) {
                        cartPrice = cartPrice.multiply(dicont);
                    }
                }
                if (ValidatorUtils.notEmpty(cart.getId())) {
                    //查询定制服务  去除刻字服务费
                    EsShopCustomizedBasic esShopCustomizedBasic = new EsShopCustomizedBasic();
                    EsShopCustomizedApplet applet = new EsShopCustomizedApplet();
                    if (cart.getId() != null && cart.getCustId() != null) {
                        applet = esShopCustomizedAppletMapper.selectById(cart.getCustId());

                        if (applet != null && applet.getId() != null) {
                            applet.setCartId(cart.getId());
                            //存在刻字服务
                            //查询刻字服务费
                            esShopCustomizedBasic = goodsFegin.detailBasics(applet.getBasicId());
                            //1.判断定制服务费是否限时免费
                            if (goods.getLetterFree() != null && goods.getLetterFree() == 1) {//开启限免
                                //判断限免时间
                                long startTime = 0;
                                long endTime = 0;
                                String[] attr = goods.getLetterTime().split(",");
                                for (int i = 0; i < attr.length; i++) {
                                    if (i == 0) {
                                        startTime = Long.parseLong(attr[i]);
                                    }
                                    if (i == 1) {
                                        endTime = Long.parseLong(attr[i]);
                                    }
                                }
                                if (startTime <= nowD.getTime() && endTime >= nowD.getTime()) {
                                    //刻字服务免费
                                } else {
                                    //判断是否满额立减
                                    if (esShopCustomizedBasic.getIsFull() == 1) {
                                        if (cartPrice.compareTo(esShopCustomizedBasic.getFullPrice()) >= 0
                                                && (ValidatorUtils.empty(esShopCustomizedBasic.getStartTime()) ||
                                                (Long.parseLong(esShopCustomizedBasic.getStartTime()) <= nowD.getTime() &&
                                                        Long.parseLong(esShopCustomizedBasic.getEndTime()) >= nowD.getTime()))) {
                                            //刻字免费
                                        } else {
                                            moneyBasic = moneyBasic.add(esShopCustomizedBasic.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                        }
                                    }
                                }
                            } else {
                                //判断是否满额立减
                                if (esShopCustomizedBasic.getIsFull() == 1) {
                                    if (cartPrice.compareTo(esShopCustomizedBasic.getFullPrice()) >= 0
                                            && (ValidatorUtils.empty(esShopCustomizedBasic.getStartTime()) ||
                                            (Long.parseLong(esShopCustomizedBasic.getStartTime()) <= nowD.getTime() &&
                                                    Long.parseLong(esShopCustomizedBasic.getEndTime()) >= nowD.getTime()))) {
                                        //刻字免费
                                    } else {
                                        moneyBasic = moneyBasic.add(esShopCustomizedBasic.getPrice().multiply(new BigDecimal(cart.getTotal())));
                                    }
                                }
                            }

                        }
                    }
                }
                totalAmount = totalAmount.add(cartPrice.multiply(new BigDecimal(cart.getTotal())));

            }
            stopWatch.stop();

            stopWatch.start("查询营销数据");
            StringBuffer mk = new StringBuffer();
            MjDcVo manjianList = markingFegin.matchManjian(cartPromotionItemList);
            result.setManjianRule(manjianList);
            MjDcVo discountList = markingFegin.matchDiscount(cartPromotionItemList);
            result.setDiscountRule(discountList);
            CartMarkingVo vo = new CartMarkingVo();
            vo.setCartList(cartPromotionItemList);
            vo.setType(1);
            if (orderMapper.hasOrderBefore(memberId)) {
                vo.setType(2);
            }
            List<EsShopFirstPurchaseRule> firstPurchaseRuleList = markingFegin.matchFirstPurchase(vo);
            result.setFirstPurchaseRuleList(firstPurchaseRuleList);
            //满赠礼
            List<EsShopFullGift> fullGiftList = markingFegin.matchFullGift(cartPromotionItemList);
            result.setFullGiftList(fullGiftList);
            //选赠礼
            List<EsShopFullGift> ChoosefullGiftList = markingFegin.ChooseFullGift(cartPromotionItemList);
            result.setChoosGiftList(ChoosefullGiftList);
            List<EsShopFullGift> esShopFullGift2 = markingFegin.matchFullGift2();
            result.setFullGiftListtwo(esShopFullGift2);
            if (firstPurchaseRuleList != null && firstPurchaseRuleList.size() > 0) {
                mk.append("3");
            }
            if (fullGiftList != null && fullGiftList.size() > 0) {
                mk.append("4");
            }
            List<EsShopSingleGift> singleGiftList = markingFegin.matchSingleGift(cartPromotionItemList);
            result.setShopSingleGiftList(singleGiftList);
            if (singleGiftList != null && singleGiftList.size() > 0) {
                mk.append("5");
            }
            if (ChoosefullGiftList != null && ChoosefullGiftList.size() > 0) {
                mk.append("7");
            }
            result.setCodeGiftGoodsMapList(new ArrayList<EsShopCodeGiftGoodsMap>());
            if (ValidatorUtils.notEmpty(orderParam.getCode())) {
                CartMarkingVo vo1 = new CartMarkingVo();
                vo1.setCartList(cartPromotionItemList);
                vo1.setCode(orderParam.getCode());
                try {
                    if (ValidatorUtils.notEmpty(orderParam.getCode())) {
                        CodeResult rules1 = markingFegin.getCodeGoods(vo1);
                        if (rules1 != null && rules1.getStatus() == 3 &&
                                rules1.getGiftGoodsMaps() != null && rules1.getGiftGoodsMaps().size() > 0) {
                            result.setCodeGiftGoodsMapList(rules1.getGiftGoodsMaps());
                            mk.append("6");
                        }
                    }
                } catch (Exception e) {
                    log.error("赠礼验证码:" + e.getMessage());
                }
            }
            //获取购物车信息
            result.setCartList(cartPromotionItemList);
            stopWatch.stop();
            stopWatch.start("查询价格数据");
            // 价格计算
            ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
            calcAmount.setFreightAmount(new BigDecimal(0));

            BigDecimal promotionAmount = new BigDecimal("0");
            BigDecimal manjianAmount = new BigDecimal("0");
            BigDecimal discountAmount = new BigDecimal("0");
            if (manjianList != null && manjianList.getId() != null) {
                manjianAmount = manjianList.getBasicAmount();
            }
            if (discountList != null && discountList.getId() != null) {
                discountAmount = discountList.getBasicAmount();
            }
            promotionAmount = discountAmount;
            if (manjianAmount.compareTo(discountAmount) > 0) {
                promotionAmount = manjianAmount;
                if (manjianAmount.compareTo(new BigDecimal(0)) > 0) {
                    mk.append("1");
                }
            } else {
                if (discountAmount.compareTo(new BigDecimal(0)) > 0) {
                    mk.append("2");
                }
            }
            vo.setMemberId(memberId);
            vo.setMarketingId(mk.toString());
            List<EsMemberCoupon> memberCouponList = markingFegin.selectUserMemberCoupon(vo);
            if (memberCouponList != null && memberCouponList.size() > 0) {
                result.setMemberCouponList(memberCouponList);
            } else {
                result.setMemberCouponList(new ArrayList<EsMemberCoupon>());
            }

            calcAmount.setTotalAmount(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            calcAmount.setPromotionAmount(promotionAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            BigDecimal payAmount1 = totalAmount.subtract(promotionAmount);
            //计算优惠券
            BigDecimal payAmount = new BigDecimal("0");
            if (ValidatorUtils.notEmpty(orderParam.getCouponId())) {
                vo.setPayAmount(payAmount1);
                vo.setMemberCouponId(orderParam.getCouponId());
                payAmount = markingFegin.selectById(vo);
                calcAmount.setCouponAmount(payAmount1.subtract(payAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                payAmount = payAmount1;
            }
            calcAmount.setCardAmount(moneyBasic.setScale(2, BigDecimal.ROUND_HALF_UP));
            if (payAmount.compareTo(new BigDecimal(0)) < 0) {
                payAmount = new BigDecimal(0);
            }
            payAmount = payAmount.add(moneyBasic).add(dispatchPrice);
            if (payAmount.compareTo(new BigDecimal(0)) > 0) {
                calcAmount.setPayAmount(payAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                calcAmount.setPayAmount(new BigDecimal(0.01));
            }
            calcAmount.setFreightAmount(dispatchPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
            result.setCalcAmount(calcAmount);
            stopWatch.stop();
            log.info(stopWatch.prettyPrint());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Object linkToBuy(CartParam cartParam) {
        return null;
    }

    @Override
    @Transactional
    public Object bookOrder(BookOrderParam orderParam) throws Exception {
        BigDecimal moneyBasic = new BigDecimal("0");
        Date nowD = new Date();
        EsShopOrderSettings query = new EsShopOrderSettings();
        query.setShopId(orderParam.getShopId());
        EsShopOrderSettings orderSettings = orderSettingsMapper.selectOne(new QueryWrapper<>(query));
        EsMember member = membersFegin.getMemberById(orderParam.getMemberId());
        if (member == null || member.getId() == null) {
            return new CommonResult().failed("账号错误");
        }
        String type = orderParam.getType();
        EsShopOrder order = createOrderObj(orderParam);
        StringBuffer goodsIds = new StringBuffer();
        List<EsShopCart> cartPromotionItemList = new ArrayList<>();
        StopWatch stopWatch = new StopWatch("下单");
        stopWatch.start("查询购物车数据");
        if ("3".equals(type)) { // 1 商品详情(立即购买) 2 勾选购物车 3全部购物车的商品
            cartPromotionItemList = cartMapper.selectList(new QueryWrapper<EsShopCart>().eq("member_id", orderParam.getMemberId()));
        }
        if ("1".equals(type)) {
            getCartByGoodsDetail(orderParam, cartPromotionItemList);
        } else if ("2".equals(type)) {
            String cartIdList1 = orderParam.getCartIds();
            if (org.apache.commons.lang.StringUtils.isBlank(cartIdList1)) {
                return new CommonResult().paramFailed();
            }
            String[] ids1 = cartIdList1.split(",");
            List<Long> resultList = new ArrayList<>(ids1.length);
            for (String s : ids1) {
                resultList.add(Long.valueOf(s));
            }
            cartPromotionItemList = cartMapper.selectBatchIds(resultList);
        }
        stopWatch.stop();

        stopWatch.start("查询加购价商品和运费");
        EsShopGoodsRules goodsRules = markingFegin.matchGoodsRules(cartPromotionItemList);
        if (goodsRules != null && ValidatorUtils.notEmpty(goodsRules.getId())) {
            return new CommonResult().failed("加购价商品不能单独使用");
        }

        //获取购物车及优惠信息
        List<EsShopOrderGoods> orderItemList = new ArrayList<>();


        BigDecimal markingAmount = new BigDecimal("0");//优惠金额

        BigDecimal totalAmount = new BigDecimal("0");//实付金额

        BigDecimal goodsTotalPrice = new BigDecimal("0");//订单商品总计
        // 邮费 取订单中需要邮费的商品中邮费最高
        int count = 0;
        order.setDispatchPrice(getDispatchPrice(cartPromotionItemList, orderParam));
        stopWatch.stop();
        stopWatch.start("查询商品限制");
        StringBuffer cartIds = new StringBuffer();//购物车ids
        for (EsShopCart cart : cartPromotionItemList) {
            if (count == 0) {
                order.setGoodsName(cart.getGoodsName());
            }
            count++;
            goodsIds.append(cart.getGoodsId());
            EsShopGoods goods = goodsFegin.getGoodsById(cart.getGoodsId());
            EsShopOrderGoods orderItem = createOrderItemObj(cart, goods);
            orderItem.setTypeoption(orderParam.getTypeOption());
            orderItem.setTypeword(orderParam.getTypeword());
            Integer goodsTotal = countByCart(cartPromotionItemList, cart.getGoodsId());
            if (goods.getNumMax() > 0 && goodsTotal > goods.getNumMax()) {
                return new CommonResult().failed("单次最多购买:" + goods.getNumMax() + "件,当前件数:" + goodsTotal);
            }
            if (goods.getNumMin() > 0 && goodsTotal < goods.getNumMin()) {
                return new CommonResult().failed("单次最少购买:" + goods.getNumMin() + "件,当前件数:" + goodsTotal);
            }
            Integer buyCount = getBuyCountByUserId(orderParam.getMemberId(), goods.getId());
            if (ValidatorUtils.empty(buyCount)) {
                buyCount = 0;
            }
            if (goods.getMostPurchases() > 0) {
                if (buyCount >= goods.getMostPurchases()) {
                    return new CommonResult().failed("最多购买:" + goods.getMostPurchases() + "件,当前已经购买件数:" + buyCount);
                }
                if (goodsTotal > (goods.getMostPurchases() - buyCount)) {
                    return new CommonResult().failed("最多购买:" + goods.getMostPurchases() + "件,已经购买：" + buyCount + ",当前选择件数:" + goodsTotal);
                }
            }
            // 单品优惠
            BigDecimal cartPrice = cart.getPrice();
            if (ValidatorUtils.notEmpty(goods.getDiscount()) && Integer.parseInt(goods.getDiscount()) > 0) {
                BigDecimal dicont = new BigDecimal(Integer.parseInt(goods.getDiscount())).multiply(new BigDecimal(0.1));
                if (goods.getServiceConditions() == 0) {
                    buyCount = getBuyCountByMemberId(orderParam.getMemberId(), goods.getId(), OrderStatus.TRADE_SUCCESS.getValue());
                    if (ValidatorUtils.empty(buyCount)) {
                        buyCount = 0;
                    }
                }
                if (goods.getIsSustainedUse() == 1 && buyCount > 0) {
                    orderItem.setPriceDiscount(dicont);
                    cartPrice = cartPrice.multiply(dicont);
                }
                if (goods.getIsSustainedUse() == 0 && buyCount == 1) {
                    orderItem.setPriceDiscount(dicont);
                    cartPrice = cartPrice.multiply(dicont);
                }
            }
            //查询定制服务  去除刻字服务费
            EsShopCustomizedBasic esShopCustomizedBasic = new EsShopCustomizedBasic();
            EsShopCustomizedApplet applet = new EsShopCustomizedApplet();
            if (cart.getId() != null && cart.getCustId() != null) {
                applet = esShopCustomizedAppletMapper.selectById(cart.getCustId());
                if (applet != null && applet.getId() != null) {
                    applet.setCartId(cart.getId());
                    cart.setEsShopCustApplet(applet);
                    //查询刻字服务费
                    esShopCustomizedBasic = goodsFegin.detailBasics(applet.getBasicId());
                    //判断是否满额立减
                    if (esShopCustomizedBasic.getIsFull() == 1) {
                        if (cartPrice.compareTo(esShopCustomizedBasic.getFullPrice()) >= 0
                                && (ValidatorUtils.empty(esShopCustomizedBasic.getStartTime()) ||
                                (Long.parseLong(esShopCustomizedBasic.getStartTime()) <= nowD.getTime() &&
                                        Long.parseLong(esShopCustomizedBasic.getEndTime()) >= nowD.getTime()))) {
                            //刻字免费
                        } else {
                            moneyBasic = moneyBasic.add(esShopCustomizedBasic.getPrice().multiply(new BigDecimal(cart.getTotal())));
                        }
                    }
                }
            }
            goodsTotalPrice = goodsTotalPrice.add(cartPrice.multiply(new BigDecimal(cart.getTotal())));
            totalAmount = totalAmount.add(cartPrice.multiply(new BigDecimal(cart.getTotal())));

            if (!ValidatorUtils.empty(cart.getOptionId()) && cart.getOptionId() > 0) {
                checkGoods(goods, false, cart.getTotal());
                EsShopGoodsOption skuStock = getSkuByGoods(goods, cart.getOptionId());
                checkSkuGoods(skuStock, cart.getTotal());
            } else {
                checkGoods(goods, true, cart.getTotal());
            }
            //生成下单商品信息
            orderItem.setPriceUnit(cart.getPrice());//设置订单商品单价信息
            orderItem.setMemberDiscountPrice(cartPrice);
            orderItem.setCustId(cart.getCustId());
            orderItemList.add(orderItem);
            cartIds.append(cart.getId() + ",");
        }
        stopWatch.stop();
        stopWatch.start("查询优惠计算");
        cartIds.deleteCharAt(cartIds.length() - 1);
        order.setCartIds(cartIds.toString());

        // 优惠计算 取 优惠更大的
        CartMarkingVo vo = new CartMarkingVo();
        vo.setCartList(cartPromotionItemList);

        BigDecimal manjianAmount = new BigDecimal("0");
        BigDecimal discountAmount = new BigDecimal("0");
        MjDcVo manjianList = markingFegin.matchManjian(cartPromotionItemList);
        if (ValidatorUtils.notEmpty(manjianList)) {
            manjianAmount = manjianList.getBasicAmount();
        }
        MjDcVo discountList = markingFegin.matchDiscount(cartPromotionItemList);
        if (ValidatorUtils.notEmpty(discountList)) {
            discountAmount = discountList.getBasicAmount();
        }
        markingAmount = discountAmount;
        if (manjianAmount.compareTo(discountAmount) > 0) {
            if (ValidatorUtils.notEmpty(manjianList)) {
                order.setManjianOrDiscountid(manjianList.getId());
                order.setManjianInfo(JsonUtils.toJsonStr(manjianList));
                markingAmount = manjianAmount;
            }
        } else {
            if (ValidatorUtils.notEmpty(discountList)) {
                order.setManjianOrDiscountid(discountList.getId());
                order.setDiscountInfo(JsonUtils.toJsonStr(discountList));
            }
        }

        addGiftGoodsOrder(orderParam, order, cartPromotionItemList, orderItemList, vo);

        //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额

        order.setCode(orderParam.getCode());
        BigDecimal payAmount2 = totalAmount.subtract(markingAmount);
        BigDecimal payAmount1 = payAmount2;
        order.setOriginalPrice(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        order.setOriginalGoodsPrice(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        order.setGoodsPrice(goodsTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        order.setRemarkBuyer(orderParam.getRemark());
        order.setReceiveType("快递");//默认快递
        order.setMemberRealname(member.getRealname());  //这里
        order.setMemberNickname(member.getNickname());
        order.setMemberMobile(member.getMobile());
        if (orderSettings != null) {
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE, orderSettings.getAutoCloseTime());
            order.setCloseTime(nowTime.getTime());
        } else {
            EsShopOrderSettings querySet = new EsShopOrderSettings();
            querySet.setShopId(orderParam.getShopId());
            EsShopOrderSettings orderSettings1 = orderSettingsMapper.selectOne(new QueryWrapper<>(querySet));
            redisRepository.set(orderParam.getShopId() + "", orderSettings1);
        }

        order.setGoodsIds(goodsIds.toString());//设置订单商品id
        order.setShopId(orderParam.getShopId());

        stopWatch.stop();

        //判断使用使用了优惠券
        if (ValidatorUtils.notEmpty(orderParam.getCouponId())) {
            stopWatch.start("优惠券");
            vo.setPayAmount(payAmount1);
            vo.setMemberCouponId(orderParam.getCouponId());
            markingAmount = markingAmount.add(payAmount1);
            payAmount1 = markingFegin.selectById(vo);
            markingAmount = markingAmount.subtract(payAmount1);
            order.setCouponId(orderParam.getCouponId());

            if (orderParam.getCouponType() == 4) {
                List<EsShopCouponGoodsMap> gifs = markingFegin.selectSendCouponGift(orderParam.getCouponId());
                if (gifs != null && gifs.size() > 0) {
                    for (EsShopCouponGoodsMap gift : gifs) {
                        EsShopOrderGoods orderItem = new EsShopOrderGoods();
                        EsShopGoods goods = goodsFegin.getGoodsById(gift.getGoodsId());
                        insertGiftOrderGoods(orderParam, orderItem, goods);
                        orderItem.setGiftType(5);
                        orderItem.setGoodsCode(gift.getCouponId() + "");
                        orderItemList.add(orderItem);
                    }
                }
            }
            stopWatch.stop();
        }
        stopWatch.start("插入订单");
        order.setDiscountAmount(markingAmount);
        if (payAmount1.compareTo(new BigDecimal(0)) < 0) {
            payAmount1 = new BigDecimal(0);
        }
        payAmount1 = payAmount1.add(order.getDispatchPrice()).add(moneyBasic);
        if (payAmount1.compareTo(new BigDecimal(0)) > 0) {
            order.setPayPrice(payAmount1.setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            order.setPayPrice(new BigDecimal(0.01));
        }
        order.setMoneyBasic(moneyBasic.setScale(2, BigDecimal.ROUND_HALF_UP));
        //插入order表和order_item表
        order.setOrderType(1);
        orderMapper.insert(order);
        for (EsShopOrderGoods orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderGoodsMapper.insert(orderItem);
        }
        stopWatch.stop();
        if (ValidatorUtils.notEmpty(orderParam.getCouponId())) {
            stopWatch.start("更新优惠券");
            markingFegin.updateMemberCoupon(orderParam.getCouponId(), order.getId(), order.getOrderNo(), 0);
            stopWatch.stop();
        }
        OmsOrderVo orderVo = new OmsOrderVo();
        orderVo.setMember(member);
        orderVo.setOrder(order);
        orderVo.setOrderGoodsList(orderItemList);
        //  sender.createOrderMq(orderVo);
        stopWatch.start("锁库存");
        for (EsShopOrderGoods orderItem : orderItemList) {
            lockStock(orderItem, 0);
        }
        stopWatch.stop();
        //删除购物车中的下单商品
        // setCartNumToCache
        stopWatch.start("删除购物车");
        deleteCartItemList(cartPromotionItemList);
        stopWatch.stop();
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);

        log.info(stopWatch.prettyPrint());

        CompletableFuture.runAsync(() -> {
            try {
                push(member, order, orderParam.getFormid(), 1, orderItemList);

                if (!hasOrder(orderParam.getMemberId())) {
                    markingFegin.sendNewCoupon(orderParam.getMemberId(), 2);
                }
               /* CartMarkingVo vo1 = new CartMarkingVo();
                vo1.setMemberId(orderParam.getMemberId());
                vo1.setPayAmount(order.getPayPrice());
                vo1.setShopOrderGoodsList(orderItemList);
                vo1.setScope(2);
                vo1.setOpenId(member.getOpenid());
                markingFegin.sendManualCoupon(vo1);
                markingFegin.sendFillFillCoupon(vo1);
                markingFegin.sendShopCoupon(vo1);*/

            } catch (Exception e) {
                log.error("发送新人券下单失败：{}", e.getMessage());
            }

        });
        return new CommonResult().success("下单成功", result);

    }

    @Override
    public Integer updateaddress(EsShopOrder entity) {
        return orderMapper.updateaddress(entity);
    }

    private void addGiftGoodsOrder(BookOrderParam orderParam, EsShopOrder order, List<EsShopCart> cartPromotionItemList, List<EsShopOrderGoods> orderItemList, CartMarkingVo vo) throws Exception {
        //首赠礼
        if (ValidatorUtils.notEmpty(orderParam.getFirstPurchaseId())) {
            vo.setRuleIds(orderParam.getFirstPurchaseId());
            List<EsShopFirstPurchaseRule> rules = markingFegin.isFirstPurchaseUseAble(vo);
            if (rules != null && rules.size() > 0) {
                order.setFirstpurchaseInfo(JsonUtils.toJsonStr(rules));
                for (EsShopFirstPurchaseRule gift : rules) {
                    EsShopOrderGoods orderItem = new EsShopOrderGoods();
                    EsShopGoods goods = goodsFegin.getGoodsById(gift.getGiftsId());
                    insertGiftOrderGoods(orderParam, orderItem, goods);
                    orderItem.setGiftType(1);
                    orderItem.setGoodsCode(gift.getFirstPurchaseId() + "");
                    orderItemList.add(orderItem);
                }
            }
        }
        //满赠礼
        if (ValidatorUtils.notEmpty(orderParam.getFullGiftId())) {
            CartMarkingVo vo1 = new CartMarkingVo();
            vo1.setCartList(cartPromotionItemList);
            vo1.setRuleIds(orderParam.getFullGiftId());
            List<EsShopFullGiftGoodsMap> rules = markingFegin.isFullGiftGoodsUseAble(vo1);
            if (rules != null && rules.size() > 0) {
                for (EsShopFullGiftGoodsMap gift : rules) {
                    EsShopOrderGoods orderItem = new EsShopOrderGoods();
                    EsShopGoods goods = goodsFegin.getGoodsById(gift.getGoodsId());
                    insertGiftOrderGoods(orderParam, orderItem, goods);
                    orderItem.setGiftType(2);
                    orderItem.setGoodsCode(gift.getFullGiftId() + "");
                    orderItemList.add(orderItem);
                }
            }
        }
        //选赠礼
        if (ValidatorUtils.notEmpty(orderParam.getChoosGiftId())) {
            CartMarkingVo vo1 = new CartMarkingVo();
            vo1.setCartList(cartPromotionItemList);
            vo1.setRuleIds(orderParam.getChoosGiftId());
            List<EsShopFullGiftGoodsMap> rules = markingFegin.isChooseGiftGoodsUseAble(vo1);
            if (rules != null && rules.size() > 0) {
                for (EsShopFullGiftGoodsMap gift : rules) {
                    EsShopOrderGoods orderItem = new EsShopOrderGoods();
                    EsShopGoods goods = goodsFegin.getGoodsById(gift.getGoodsId());
                    insertGiftOrderGoods(orderParam, orderItem, goods);
                    orderItem.setGiftType(6);
                    orderItem.setGoodsCode(gift.getFullGiftId() + "");
                    orderItemList.add(orderItem);
                }
            }
        }
        //单品赠礼
        if (ValidatorUtils.notEmpty(orderParam.getSingleGiftIds())) {
            CartMarkingVo vo2 = new CartMarkingVo();
            vo2.setCartList(cartPromotionItemList);
            vo2.setRuleIds(orderParam.getSingleGiftIds());
            List<EsShopSingleGiftGoodsMap> rules = markingFegin.isSingleGiftUseAble(vo2);
            if (rules != null && rules.size() > 0) {
                for (EsShopSingleGiftGoodsMap gift : rules) {
                    EsShopOrderGoods orderItem = new EsShopOrderGoods();
                    EsShopGoods goods = goodsFegin.getGoodsById(gift.getGoodsId());
                    insertGiftOrderGoods(orderParam, orderItem, goods);
                    orderItem.setCount(gift.getCount());
                    orderItem.setGoodsCode(gift.getSingleGiftId() + "");
                    orderItem.setGiftType(3);
                    orderItemList.add(orderItem);
                }
            }
        }
        //验证码
        if (ValidatorUtils.notEmpty(orderParam.getCode())) {
            CartMarkingVo vo1 = new CartMarkingVo();
            vo1.setCartList(cartPromotionItemList);
            vo1.setCode(orderParam.getCode());
            CodeResult rules1 = markingFegin.getCodeGoods(vo1);
            if (rules1 != null && rules1.getStatus() == 3 &&
                    rules1.getGiftGoodsMaps() != null && rules1.getGiftGoodsMaps().size() > 0) {
                markingFegin.updateCodeStatus(orderParam.getCode(), 2);
                for (EsShopCodeGiftGoodsMap gift : rules1.getGiftGoodsMaps()) {
                    EsShopOrderGoods orderItem = new EsShopOrderGoods();
                    EsShopGoods goods = goodsFegin.getGoodsById(gift.getGoodsId());
                    insertGiftOrderGoods(orderParam, orderItem, goods);
                    orderItem.setGoodsCode(gift.getCodeGiftId() + "");
                    orderItem.setGiftType(4);
                    orderItemList.add(orderItem);
                }
            }
        }
    }

    private boolean hasOrder(Long memberId) {
        try {
            return orderMapper.hasOrder(memberId) > 1;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 插入礼物数据到订单
     *
     * @param orderParam
     * @param orderItem
     * @param goods
     */
    private void insertGiftOrderGoods(BookOrderParam orderParam, EsShopOrderGoods orderItem, EsShopGoods goods) {
        if (ValidatorUtils.empty(goods.getCargoTime())) {
            orderItem.setCargoTime("0");
        } else {
            orderItem.setCargoTime(goods.getCargoTime());
        }
        orderItem.setGoodsId(goods.getId());
        orderItem.setCreateTime(new Date());
        orderItem.setMemberId(orderParam.getMemberId());
        orderItem.setPriceOriginal(goods.getPrice());
        orderItem.setPrice(goods.getPrice());
        orderItem.setMemberPrice(goods.getPrice());
        orderItem.setCount(1);
        orderItem.setThumb(goods.getThumb());
        orderItem.setStatus(OrderStatus.INIT.getValue());
        orderItem.setIsGifts(1);
        orderItem.setTitle(goods.getTitle());
    }

    private void checkGoods(EsShopGoods goods, boolean falg, int count) {
        if (goods == null || goods.getId() == null) {
            throw new BusinessException("商品已删除");
        }
        if (goods.getStatus() != 1) {
            throw new BusinessException("商品已售罄");//        //商品状态（1为出售中，3为已售罄，-2为仓库中，-1为回收站）
        }
        if (falg && (goods.getVituralStock() <= 0 || goods.getVituralStock() < count)) {
            throw new BusinessException("库存不足!");
        }


    }

    private void checkSkuGoods(EsShopGoodsOption goods, int count) {
        if (goods == null || goods.getId() == null) {
            throw new BusinessException("商品已删除");
        }
        if (goods.getVirtualStock() <= 0 || goods.getVirtualStock() < count) {
            throw new BusinessException("库存不足!");
        }
    }

    private EsShopGoodsOption getSkuByGoods(EsShopGoods goods, Long optionId) {
        List<EsShopGoodsOption> options = goods.getOptions();
        if (options != null && options.size() > 0) {
            for (EsShopGoodsOption o : options) {
                if (o.getId().equals(optionId)) {
                    return o;
                }
            }
        }
        return null;
    }

    @Override
    public void lockStock(EsShopOrderGoods item, Integer stockCnf) {
        StopWatch stopWatch = new StopWatch("lockStock");
        stopWatch.start("goodsFegin.getGoodsById");
        EsShopGoods goods = goodsFegin.getGoodsById(item.getGoodsId());
        stopWatch.stop();
        if (goods != null && goods.getId() != null) {

            if (stockCnf.equals(goods.getStockCnf())) {
                redisRepository.delete(String.format(RedisConstant.GOODSDETAIL, goods.getId() + ""));
                redisRepository.delete(String.format(RedisConstant.GOODS, goods.getId() + ""));
                EsShopGoods newGoods = new EsShopGoods();
                newGoods.setId(goods.getId());
                if (!ValidatorUtils.empty(item.getOptionId()) && item.getOptionId() > 0) {
                    stopWatch.start("decrSkuStock");
                    int num = goodsFegin.decrSkuStock(item.getOptionId(), item.getCount(), goods.getId(), goods.getType());
                    stopWatch.stop();
                    if (num == 0) {
                        throw new BusinessException("goods is stock out. goodsId=" + item.getGoodsId() + ", skuId=" + item.getOptionId());
                    } else {
                        stopWatch.start("updateGoodsById");
                        Integer stocks = 0;
                        List<EsShopGoodsOption> list = goods.getOptions();
                        for (EsShopGoodsOption op : list) {
                            stocks += op.getVirtualStock();
                        }
                        if (stocks - item.getCount() == 0) {
                            newGoods.setStatus(3);
                            newGoods.setSelloutTime(new Date());
                        }
                        newGoods.setVituralStock(stocks - item.getCount());
                        goodsFegin.updateGoodsById(newGoods);
                        stopWatch.stop();
                    }
                } else {
                    stopWatch.start("decrGoodsStock");
                    int num = goodsFegin.decrGoodsStock(item.getGoodsId(), item.getCount());
                    stopWatch.stop();
                    if (num == 0) {
                        throw new BusinessException("goods is stock out. goodsId=" + item.getGoodsId() + ", goodsId=" + item.getGoodsId());
                    } else {
                        stopWatch.start("updateGoodsById");
                        if (goods.getVituralStock() - item.getCount() == 0) {
                            newGoods.setStatus(3);
                            newGoods.setSelloutTime(new Date());
                            goodsFegin.updateGoodsById(newGoods);
                        }
                        stopWatch.stop();
                    }
                }
            }
        }
        log.info(stopWatch.prettyPrint());
    }

    @Override
    public void releaseStock(long orderId) {
        List<EsShopOrderGoods> itemList = orderGoodsMapper.selectList(new QueryWrapper<>(new EsShopOrderGoods()).eq("order_id", orderId));
        if (itemList != null && itemList.size() > 0) {
            for (EsShopOrderGoods item : itemList) {
                EsShopGoods goods = goodsFegin.getGoodsById(item.getGoodsId());
                if (goods != null && goods.getId() != null && goods.getStockCnf() != 2) {
                    redisRepository.delete(String.format(RedisConstant.GOODSDETAIL, goods.getId() + ""));
                    redisRepository.delete(String.format(RedisConstant.GOODS, goods.getId() + ""));
                    if (goods.getStatus() == 3) {
                        EsShopGoods newGoods = new EsShopGoods();
                        newGoods.setId(goods.getId());
                        newGoods.setStatus(1);
                        newGoods.setSelloutTime(null);
                        goodsFegin.updateGoodsById(newGoods);
                    }
                    if (!ValidatorUtils.empty(item.getOptionId()) && item.getOptionId() > 0) {
                        goodsFegin.addSkuStock(item.getOptionId(), item.getCount(), goods.getId());
                    }
                    goodsFegin.addGoodsStock(item.getGoodsId(), item.getCount());
                }

            }
        }
    }

    @Override
    public void closeOrdder(Long id) {
        releaseStock(id);
        CompletableFuture.runAsync(() -> {
            try {
                EsShopOrder vo = orderMapper.selectById(id);
                if (ValidatorUtils.notEmpty(vo.getCode())) {
                    markingFegin.updateCodeStatus(vo.getCode(), 1);
                }
                if (ValidatorUtils.notEmpty(vo.getCouponId())) {
                    markingFegin.updateMemberCoupon(vo.getCouponId(), vo.getMemberId(), null, 1);
                }
                String formid = this.getFormIdByMemberId(vo.getMemberId());

                if (ValidatorUtils.notEmpty(formid)) {
                    push(membersFegin.getMemberById(vo.getMemberId()), vo, formid, 3, null);
                } else {
                    log.error("关闭订单状态消息推送formId不够,memberId=" + vo.getMemberId());
                }

                if (vo.getStatus() == OrderStatus.INIT.getValue()) {
                    vo.setStatus(OrderStatus.CLOSED.getValue());
                    orderMapper.updateById(vo);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }

    @Override
    public int hasCountByMemberById(Long memberId) {
        Integer count = orderMapper.hasCountByMemberById(memberId);
        if (ValidatorUtils.empty(count)) {
            return 0;
        }
        return count;
    }

    @Override
    public int hasPayied(Long memberId) {
        Integer count = orderMapper.hasPayied(memberId);
        if (ValidatorUtils.empty(count)) {
            return 0;
        }
        return count;
    }

    private EsShopOrder createOrderObj(BookOrderParam orderParam) {
        EsShopOrder order = new EsShopOrder();
        order.setIsPartDelivery(0);
        //转化为订单信息并插入数据库
        order.setMemberId(orderParam.getMemberId());
        order.setCreateTime(new Date());
        //支付方式：1->微信小程序
        order.setPayType(0);//创建订单的时候 默认未支付
        //订单来源：0->小程序
        order.setSoureType(1);
        //订单状态：
        order.setStatus(OrderStatus.INIT.getValue());
        //订单类型：
        //  order.setOrderType(0);
        //收货人信息：姓名、电话、邮编、地址
        order.setRemarkBuyer(orderParam.getRemarkBuyer());
        order.setAddressArea(orderParam.getAddressArea());
        order.setAddressProvince(orderParam.getAddressProvince());
        order.setBuyerMobile(orderParam.getBuyerMobile());
        order.setAddressCity(orderParam.getAddressCity());
        order.setAddressDetail(orderParam.getAddressDetail());
        order.setBuyerName(orderParam.getBuyerName());
        //生成订单号
        order.setOrderNo(generateOrderSn(order));
        //订单备注
        order.setRemark(orderParam.getRemark());

        return order;
    }

    private EsShopOrderGoods createOrderItemObj(EsShopCart cart, EsShopGoods goods) {
        EsShopOrderGoods orderItem = new EsShopOrderGoods();
        orderItem.setGroupId(goods.getGroupId());
        orderItem.setCategoryId(goods.getCategoryId());
        orderItem.setGoodsCode(goods.getGoodsCode());
        orderItem.setUnit(goods.getUnit());
        if (ValidatorUtils.empty(goods.getCargoTime())) {
            orderItem.setCargoTime("0");
        } else {
            orderItem.setCargoTime(goods.getCargoTime());
        }


        if (!ValidatorUtils.empty(cart.getOptionId()) && cart.getOptionId() > 0) {
            EsShopGoodsOption option = getSkuByGoods(goods, cart.getOptionId());
            if (option != null) {
                orderItem.setOptionTitle(option.getTitle());
                orderItem.setGoodsCode(option.getGoodsCode());
            }
        }

        orderItem.setGoodsId(cart.getGoodsId());
        orderItem.setCreateTime(new Date());
        orderItem.setMemberId(cart.getMemberId());
        orderItem.setPriceOriginal(cart.getPrice());
        orderItem.setPrice(cart.getPrice());

        orderItem.setPrice(cart.getPrice().multiply(BigDecimal.valueOf(cart.getTotal())).setScale(2, BigDecimal.ROUND_HALF_UP));
        orderItem.setOptionId(cart.getOptionId());
        orderItem.setMemberPrice(cart.getPrice());
        orderItem.setShopId(cart.getShopId());
        orderItem.setTotal(cart.getPrice().multiply(BigDecimal.valueOf(cart.getTotal())).setScale(2, BigDecimal.ROUND_HALF_UP));
        orderItem.setThumb(cart.getPic());
        orderItem.setStatus(OrderStatus.INIT.getValue());
        orderItem.setIsGifts(0);
        orderItem.setCount(cart.getTotal());
        orderItem.setTitle(cart.getGoodsName());
        orderItem.setOptionId(cart.getOptionId());


        orderItem.setCartId(cart.getId());//赋值购物车id 为了获得于定制服务相关联系

        return orderItem;
    }

    /**
     * 删除下单商品的购物车信息
     */
    private void deleteCartItemList(List<EsShopCart> cartPromotionItemList) {

        if (cartPromotionItemList != null && cartPromotionItemList.size() > 0) {
            List<Long> ids = new ArrayList<>();
            for (EsShopCart cartPromotionItem : cartPromotionItemList) {
                ids.add(cartPromotionItem.getId());
            }
            cartMapper.deleteBatchIds(ids);

        }
    }

    /**
     * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
     */
    private String generateOrderSn(EsShopOrder order) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(r.nextInt(10));
        sb.append(System.currentTimeMillis());
        return sb.toString();
    }

    @Override
    public Object addCart(CartParam cartParam) {
        if (ValidatorUtils.empty(cartParam.getTotal())) {
            cartParam.setTotal(1);
        }
        EsShopCart cart = new EsShopCart();
        EsShopCart exit = new EsShopCart();
        EsShopGoods goods = goodsFegin.getGoodsById(cartParam.getGoodsId());
        if (ValidatorUtils.empty(cartParam.getOptionId())) {
            if (ValidatorUtils.empty(cartParam.getGoodsId())) {
                return new CommonResult().paramFailed();
            }
            checkGoods(goods, true, cartParam.getTotal());

            cart.setGoodsId(cartParam.getGoodsId());
            cart.setMemberId(cartParam.getMemberId());
            cart.setShopId(goods.getShopId());
            cart.setCustId(0L);
            exit = cartMapper.selectOne(new QueryWrapper<>(cart));
            cart.setType(cartParam.getTypeOption());
            cart.setTypeword(cartParam.getTypeword());
            cart.setCategoryId(goods.getCategoryId());
            cart.setPic(goods.getThumb());
            cart.setGoodsName(goods.getTitle());
            cart.setPrice(goods.getPrice());
            cart.setTotal(cartParam.getTotal());
            if (exit == null) {
                cart.setCreateTime(new Date());
                cartMapper.insert(cart);
            } else {
                exit.setPrice(goods.getPrice());
                exit.setTotal(exit.getTotal() + cart.getTotal());
                cartMapper.updateById(exit);
                return new CommonResult().success(exit);
            }

        } else {
            checkGoods(goods, false, cartParam.getTotal());
            EsShopGoodsOption sku = getSkuByGoods(goods, cartParam.getOptionId());

            checkSkuGoods(sku, cartParam.getTotal());
            cart.setGoodsId(cartParam.getGoodsId());
            cart.setMemberId(cartParam.getMemberId());
            cart.setShopId(sku.getShopId());
            cart.setOptionId(cartParam.getOptionId());
            cart.setCustId(0L);
            exit = cartMapper.selectOne(new QueryWrapper<>(cart));
            cart.setCategoryId(goods.getCategoryId());
            cart.setType(cartParam.getTypeOption());
            cart.setTypeword(cartParam.getTypeword());
            cart.setPic(sku.getThumb());
            cart.setGoodsName(sku.getGoodsName());
            cart.setOptionName(sku.getTitle());
            cart.setPrice(sku.getPrice());
            cart.setTotal(cartParam.getTotal());

            if (exit == null) {
                cart.setCreateTime(new Date());
                cartMapper.insert(cart);
            } else {
                exit.setPrice(sku.getPrice());
                exit.setTotal(exit.getTotal() + cart.getTotal());
                cartMapper.updateById(exit);
                return new CommonResult().success(exit);
            }
        }
        return new CommonResult().success(cart);
    }

    @Override
    public int updateQuantity(EsShopCart cartParam) {

        return cartMapper.updateById(cartParam);
    }

    @Override
    public int clearCart(Long memberId) {

        return cartMapper.delete(new QueryWrapper<EsShopCart>().eq("member_id", memberId));
    }

    @Override
    public int deleteCartIds(List<Long> resultList) {

        return cartMapper.deleteBatchIds(resultList);
    }

    @Override
    public int isCheckCart(Integer isSelected, List<Long> resultList) {
        EsShopCart record = new EsShopCart();
        record.setIsSelected(isSelected);
        return cartMapper.update(record, new QueryWrapper<EsShopCart>().in("id", resultList));
    }

    @Override
    public Page<EsShopOrder> selectPageExtByApplet(AppletOrderParam appletOrderParam) {
        Page<EsShopOrder> page = new Page<EsShopOrder>(appletOrderParam.getCurrent(), appletOrderParam.getSize());
        //  page.setAsc(appletOrderParam.getIsAsc() == 0 ? false : true);
        List<EsShopOrder> orderList = null;
        if (appletOrderParam.getOrderType() != null && appletOrderParam.getOrderType() == 2) {
            List<EsShopOrder> orderfriend = orderMapper.selectFriendListByApplet(appletOrderParam);
            orderList = orderfriend;
            System.out.println(orderList);
        } else {
            List<EsShopOrder> orderListapp = orderMapper.selectOrderListByApplet(appletOrderParam);
            orderList = orderListapp;
        }

        for (EsShopOrder order : orderList) {
            List<EsShopOrderGoods> orderGoodsList = this.shopOrderGoodsService.list(new QueryWrapper<>(new EsShopOrderGoods())
                            .eq("order_id", order.getId())
                            .eq("is_gifts", 0)
                    //不查询赠品信息
            );
            order.setOrderGoodsList(orderGoodsList);
            order.setStatusEn(OrderUtils.orderStatus(order.getStatus()));
            int goodsCount = 0;
            for (EsShopOrderGoods goods : order.getOrderGoodsList()) {
                goodsCount += goods.getCount();
            }
            order.setGoodsCount(goodsCount);
        }
        page.setRecords(orderList);
        page.setTotal(orderMapper.selectOrderCountByApplet(appletOrderParam));
        return page;
    }

    @Override
    public Map<String, Object> cartList(Long memberId) {
        List<EsShopCart> cartList = cartMapper.selectList(new QueryWrapper<EsShopCart>().eq("member_id", memberId).orderByDesc("id"));
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        Integer isCheckAll = 1;
        Integer isEmpty = 1;
        if (cartList != null && cartList.size() > 0) {
            isEmpty = 2;
            for (EsShopCart cart : cartList) {
                EsShopGoods goods = goodsFegin.getGoodsById(cart.getGoodsId());
                if (goods == null || goods.getId() == null) {
                    cartMapper.deleteById(cart.getId());
                    continue;
                }
                if (cart.getIsSelected() == 2) {
                    isCheckAll = 2;
                }
                CartPromotionItem item = new CartPromotionItem();
                if (ValidatorUtils.notEmpty(cart.getOptionId()) && cart.getOptionId() > 0) {
                    EsShopGoodsOption option = goodsFegin.getSkuById(cart.getOptionId());
                    if (option == null || option.getId() == null) {
                        cartMapper.deleteById(cart.getId());
                        continue;
                    }
                    if (option.getVirtualStock() <= 0) {
                        cartMapper.deleteById(cart.getId());
                        continue;
                    }
                    item.setGoodsOption(option);
                } else {
                    if (goods.getVituralStock() <= 0) {
                        cartMapper.deleteById(cart.getId());
                        continue;
                    }
                }
                item.setEsShopCart(cart);

                //查询定制服务
                if (cart.getId() != null && cart.getCustId() != null) {
                    EsShopCustomizedApplet applet = new EsShopCustomizedApplet();
                    applet = esShopCustomizedAppletMapper.selectById(cart.getCustId());
                    if (applet != null && applet.getId() != null) {
                        applet.setCartId(cart.getId());
                        item.setEsShopCustApplet(applet);
                    }
                }
                cartPromotionItemList.add(item);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("cartList", cartPromotionItemList);
        map.put("isCheckAll", isCheckAll);
        map.put("isEmpty", isEmpty);
        return map;
    }

    @Override
    public List<EsShopCart> cartPromotionList(Long memberId) {
        return cartMapper.selectList(new QueryWrapper<EsShopCart>().eq("member_id", memberId));
    }

    public EsDeliveryAddresser getDefaultAddress(Long memberId) {
        EsDeliveryAddresser q = new EsDeliveryAddresser();
        q.setIsDefault(1);
        q.setMemberId(memberId);
        return addresserMapper.selectOne(new QueryWrapper<>(q));
    }

    @Override
    public Integer cartGoodsCount(Long memberId) {
        return cartMapper.cartGoodsCount(memberId);
    }

    @Override
    public List<OrderStstic> listOrderGroupByMemberId() {
        return orderMapper.listOrderGroupByMemberId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confimPay(Long id) {
        EsShopOperationLog log = new EsShopOperationLog();
        EsShopOrder vo = orderMapper.selectById(id);
        int status = OrderStatus.TO_DELIVER.getValue();
        StringBuffer sb = new StringBuffer();
        sb.append(id + "订单状态由'" + OrderUtils.orderStatus(vo.getStatus()) + "'(" + vo.getStatus() + ")变为'"
                + OrderUtils.orderStatus(status) + "(" + status + ")'");
        sb.append("订单id为{" + id + "}被管理人员点击确认付款");
        log.setCreateTime(new Date());
        log.setOperationDesc(sb.toString());
        vo.setPayType(3);//后台支付
        vo.setPayTime(new Date());
        vo.setStatus(status);
        logMapper.insert(log);


        List<EsShopOrderGoods> orderGoodsList = this.shopOrderGoodsService.list(new QueryWrapper<>(new EsShopOrderGoods())
                        .eq("order_id", vo.getId())
//                        .eq("is_gifts", 0)
////                //不查询赠品信息
        );
        if (orderGoodsList != null && orderGoodsList.size() != 0) {
            for (EsShopOrderGoods item : orderGoodsList) {
                this.lockStock(item, 1);//后台点击付款 减库存。
            }
        }
        return orderMapper.updateById(vo) > 0;
    }

    @Override
    public EsShopOrder getOrderBuyerByMemberId(Long memberId) {

        return orderMapper.getOrderBuyerByMemberId(memberId);
    }

    @Override
    public List<OrderStstic> listOrderGroupByManJianId() {
        return orderMapper.listOrderGroupByManJianId();
    }

    @Override
    public List<OrderStstic> listOrderGroupByDiscountId() {
        return orderMapper.listOrderGroupByDiscountId();
    }

    @Override
    public List<EsShopOrderGoods> selOrderGoodsByPart(Long orderId) {
        return shopOrderGoodsService.list(new QueryWrapper<>(new EsShopOrderGoods())
                .eq("order_id", orderId).eq("status", OrderGoodsStatus.PARTDELIVERY.getValue()));
    }

    @Override
    public UserDiffOrderStatusCount getDiffStatusCount(Long userId) {
        Integer initCount = 0;//待付款
        Integer toDeliveryCount = 0;//待发货
        Integer deliveredCount = 0;// 待收货
        Integer tradeSuccessedCount = 0; // 已完成
        Integer closedCount = 0;// 已关闭 // 已取消 统一
        List<EsShopOrder> orders = this.list(new QueryWrapper<>(new EsShopOrder()).eq("member_id", userId));

        for (EsShopOrder order : orders) {
            Integer status = order.getStatus();
            if (status == OrderStatus.INIT.getValue()) {
                ++initCount;
            } else if (status == OrderStatus.TO_DELIVER.getValue()) {
                ++toDeliveryCount;
            } else if (status == OrderStatus.DELIVERED.getValue()) {
                ++deliveredCount;
            } else if (status == OrderStatus.TRADE_SUCCESS.getValue()) {
                ++tradeSuccessedCount;
            } else if (status == OrderStatus.CLOSED.getValue()) {
                ++closedCount;
            }
        }
        UserDiffOrderStatusCount obj = new UserDiffOrderStatusCount();
        obj.setInitCount(initCount);
        obj.setToDeliveryCount(toDeliveryCount);
        obj.setDeliveredCount(deliveredCount);
        obj.setTradeSuccessedCount(tradeSuccessedCount);
        obj.setClosedCount(closedCount);
        return obj;
    }

    @Override
    public List<TableColumnInfo> selOrderColumnInfo() {
        return tableMapper.getColumnInfoByTableName("es_shop_order");
    }

    @Override
    public OrderCustTotalVo getOldConsumeInfo(TradeAnalyzeParam param) {
        return orderMapper.getOldConsumeInfo(param);
    }

    @Override
    public OrderCustTotalVo getTotalInfo(TradeAnalyzeParam param) {
        return orderMapper.getTotalInfo(param);
    }

    @Override
    public Integer selectUCustoCount(TradeAnalyzeParam param) {
        return orderMapper.selectUCustoCount(param);
    }

    /**
     * 推送消息
     *
     * @param member
     * @param order
     * @param formId
     * @param infoId 1下单成功通知 2支付成功通知3订单取消通知4订单发货通知 5订单状态更新通知
     */
    @Override
    public void push(EsMember member, EsShopOrder order, String formId, int infoId, List<EsShopOrderGoods> orderGoodsList) {
        Long userId = order.getMemberId();
        Long orderId = order.getId();
        log.info("发送模版消息：userId=" + order.getMemberId() + ",orderId=" + orderId + ",formId=" + formId);
        if (ValidatorUtils.empty(formId)) {
            log.error("发送模版消息：userId=" + userId + ",orderId=" + orderId + ",formId=" + formId);
        }
        String accessToken = null;
        try {
            EsMiniprogram min = membersFegin.getByShopId(order.getShopId());
            Integer id = 0;
            if (infoId == 1) {
                log.info("下单成功发送模版消息：userId=" + order.getMemberId() + ",orderId=" + orderId + ",formId=" + formId);
                id = min.getPlaceOrderInform();
            } else if (infoId == 2) {
                log.info("支付成功发送模版消息：userId=" + order.getMemberId() + ",orderId=" + orderId + ",formId=" + formId);
                id = min.getPaySuccedInform();
            } else if (infoId == 3) {
                log.info("订单取消发送模版消息：userId=" + order.getMemberId() + ",orderId=" + orderId + ",formId=" + formId);
                id = min.getOrderCancelInform();
            } else if (infoId == 4) {
                log.info("订单发货发送模版消息：userId=" + order.getMemberId() + ",orderId=" + orderId + ",formId=" + formId);
                id = min.getOrderShipmentsInform();
            } else if (infoId == 5) {
                log.info("订单状态更发送模版消息：userId=" + order.getMemberId() + ",orderId=" + orderId + ",formId=" + formId);
                id = min.getOrderStatusInform();
            }
            EsCoreMessageTemplate query = new EsCoreMessageTemplate();
            query.setId((long) id);
            query.setStatus(1);
            EsCoreMessageTemplate messageTemplate = esCoreMessageTemplateMapper.selectOne(new QueryWrapper<>(query));
            JSONArray a = (JSONArray) JSONArray.parse(messageTemplate.getTemplate());
            Map<String, TemplateData> param = new HashMap<String, TemplateData>();
            int count = 0;
            for (int i = 0; i < a.size(); i++) {
                count++;
                Map map = JsonUtils.readJsonToMap(a.get(i).toString());
                // {"color0":"#000","number0":"[订单号]","title0":"订单编号"}
                param.put("keyword" + count, new TemplateData(getV(map.get("number" + i).toString(), order), map.get("color" + i).toString()));
            }
            accessToken = wechatApiService.getAccessToken(min.getAppid(), min.getAppSecret());
            /* param.put("keyword1", new TemplateData(oderIterm.getTitle(), "#EE0000"));*/
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(param));
//        //调用发送微信消息给用户的接口    ********这里写自己在微信公众平台拿到的模板ID
            String ret = WX_TemplateMsgUtil.sendWechatMsgToUser(member.getOpenid(), messageTemplate.getOriginalTemplateId(), messageTemplate.getAddress(),
                    formId, jsonObject, accessToken);
            if ("success".equals(ret)) {
                EsShopFormid shopFormid = new EsShopFormid();
                shopFormid.setStatus(2);
                formidMapper.update(shopFormid, new QueryWrapper<EsShopFormid>().eq("form_id", formId));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getFormIdByMemberId(Long id) {
        List<EsShopFormid> formidList = formidMapper.getFormIdByMemberId(id, DateUtils.addDay(new Date(), -5));
        if (formidList != null && formidList.size() > 0) {
            return formidList.get(0).getFormId();
        }
        return null;
    }

    public List<EsShopOrder> selOrderDataInfoByGoodsParam(GoodsAnalyzeParam param) {
        //条件
        QueryWrapper<EsShopOrder> condition = new QueryWrapper();
        condition.notIn("status", OrderStatus.INIT.getValue(), OrderStatus.CLOSED.getValue());

        if (ValidatorUtils.notEmpty(param.getStartTime()))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(param.getEndTime()))
            condition.le("create_time", Timestamp.valueOf(DateUtil.format(param.getEndTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(param.getSource()))
            condition.eq("soure_type", param.getSource());

        return this.list(condition);
    }


    private String getV(String str, EsShopOrder order) {
        EsShopOrderBatchSendDetail vo = new EsShopOrderBatchSendDetail();
        vo.setOrderId(order.getId());
        EsShopOrderBatchSendDetail obs = orderBatchSendDetailMapper.selectOne(new QueryWrapper<>(vo));
        if (!str.startsWith("[")) {
            return str;
        }
        if ("[订单号]".equals(str)) {
            return order.getOrderNo();
        }
        if ("[订单状态]".equals(str)) {
            //   订单状态：0->待付款；1->待发货；2->待收货；3->已完成；->4已退款；->5维权中；->6维权已完成；->7已取消；->8已关闭；->9无效订单；
            if (order.getStatus() == 0) {
                return "待付款";
            } else if (order.getStatus() == 1) {
                return "待发货";
            } else if (order.getStatus() == 2) {
                return "待收货";
            } else if (order.getStatus() == 3) {
                return "已完成";
            } else if (order.getStatus() == 4) {
                return "已退款";
            } else if (order.getStatus() == 5) {
                return "维权中";
            } else if (order.getStatus() == 6) {
                return "维权已完成";
            } else if (order.getStatus() == 7) {
                return "已取消";
            } else if (order.getStatus() == 8) {
                return "已关闭";
            } else if (order.getStatus() == 9) {
                return "无效订单";
            }
        }
        if ("[支付时间]".equals(str)) {
            return DateUtils.format(order.getPayTime(), "yyyy-MM-dd HH:mm:ss");
        }
        if ("[下单时间]".equals(str)) {
            return DateUtils.format(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
        }
        if ("[订单金额]".equals(str)) {
            return order.getPayPrice().setScale(2, BigDecimal.ROUND_HALF_UP) + "";
        }
        if ("[商品名称]".equals(str)) {
            return order.getGoodsName();
        }
        if ("[购买者姓名]".equals(str)) {
            return order.getBuyerName();
        }
        if ("[购买者电话]".equals(str)) {
            return order.getBuyerMobile();
        }
        if ("[温馨提示]".equals(str)) {
            return order.getRemark();
        }
        if ("[运费]".equals(str)) {
            return order.getDispatchPrice().setScale(2, BigDecimal.ROUND_HALF_UP) + "";
        }
        if ("[备注信息]".equals(str)) {
            return order.getRemark() + "";
        }
        if ("[快递公司]".equals(str)) {
            return obs.getExpressName();
        }
        if ("[粉丝昵称]".equals(str)) {
            return order.getMemberNickname() + "";
        }
        if ("[快递单号]".equals(str)) {
            return obs.getExpressSn() + "";
        }
        if ("[发货时间]".equals(str)) {
            return DateUtils.format(obs.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }


    @Override
    public Integer sumByGoods(Long memberId, Long goodsId, Date startTime, Date endTime) {
        Integer count = orderMapper.sumByGoods(memberId, goodsId, startTime, endTime);
        if (ValidatorUtils.empty(count)) {
            return 0;
        }
        return count;
    }

    @Override
    public Date getMinCreateTime(Long shopId) {
        return orderMapper.getMinCreateTime(shopId);
    }

    @Override
    public List<EsShopOrder> selCountDateDetail(EsShopOrder entity) {
        return orderMapper.selCountDateDetail(entity);
    }

    @Override
    public List<EsShopOrder> selOrderListByGoodsAnay(GoodsAnalyzeParam param) {
        //条件
        QueryWrapper<EsShopOrder> condition = new QueryWrapper();
        condition.notIn("status", OrderStatus.INIT.getValue(), OrderStatus.CLOSED.getValue());
        if (ValidatorUtils.notEmpty(param.getStartTime()))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(param.getEndTime()))
            condition.le("create_time", Timestamp.valueOf(param.getEndTime()));
        if (ValidatorUtils.notEmpty(param.getSource()))
            condition.eq("soure_type", param.getSource());

        return orderMapper.selectList(condition);
    }

    @Override
    public void sendTemplate(String opedid, String formId, Long shopId, String ids, String name, Long id) {
        Long userId = id;
        log.info("发送模版消息：userId=" + id + "formId=" + formId);
        if (ValidatorUtils.empty(formId)) {
            log.error("发送模版消息：userId=" + userId + ",formId=" + formId);
        }
        String accessToken = null;
        try {
            EsMiniprogram min = membersFegin.getByShopId(shopId);
            System.out.println("min    +" + min);
            EsCoreMessageTemplate messageTemplate = esCoreMessageTemplateMapper.selectById(ids);
            JSONArray a = (JSONArray) JSONArray.parse(messageTemplate.getTemplate());
            Map<String, TemplateData> param = new HashMap<String, TemplateData>();
            int count = 0;
            for (int i = 0; i < a.size(); i++) {
                count++;
                Map map = JsonUtils.readJsonToMap(a.get(i).toString());
                // {"color0":"#000","number0":"[订单号]","title0":"订单编号"}
                if (i == 0) {
                    param.put("keyword" + count, new TemplateData(name, map.get("color" + i).toString()));
                } else {
                    param.put("keyword" + count, new TemplateData(map.get("number" + i).toString(), map.get("color" + i).toString()));
                }

            }
            System.out.println("appid + " + min.getAppid() + ", appSecret + " + min.getAppSecret());
            accessToken = wechatApiService.getAccessToken(min.getAppid(), min.getAppSecret());
            /* param.put("keyword1", new TemplateData(oderIterm.getTitle(), "#EE0000"));*/
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(param));
//        //调用发送微信消息给用户的接口    ********这里写自己在微信公众平台拿到的模板ID
            String ret = WX_TemplateMsgUtil.sendWechatMsgToUser(opedid, messageTemplate.getOriginalTemplateId(), messageTemplate.getAddress(),
                    formId, jsonObject, accessToken);
            if ("success".equals(ret)) {
                EsShopFormid shopFormid = new EsShopFormid();
                shopFormid.setStatus(2);
                formidMapper.update(shopFormid, new QueryWrapper<EsShopFormid>().eq("form_id", formId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updvituralStock(Long smallBeautyBoxId, Integer total) {
        EsActivatySmallBeautyBoxGiftBox giftBox = esActivatySmallBeautyBoxGiftBoxService.getById(smallBeautyBoxId);
        giftBox.setVituralStock(giftBox.getVituralStock() - total);
        esActivatySmallBeautyBoxGiftBoxService.updateById(giftBox);
    }

}
