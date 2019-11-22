package com.mei.zhuang.dao.order;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.vo.data.customer.CustTendencyParam;
import com.mei.zhuang.vo.data.customer.CustTradeSuccessParam;
import com.mei.zhuang.vo.data.goods.GoodsAnalyzeParam;
import com.mei.zhuang.vo.data.trade.OrderCustTotalVo;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import com.mei.zhuang.vo.order.AppletOrderParam;
import com.mei.zhuang.vo.order.OrderParam;
import com.mei.zhuang.vo.order.OrderStstic;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-04-15
 */
public interface EsShopOrderMapper extends BaseMapper<EsShopOrder> {

    //修改拆礼物信息
    Integer updateaddress(EsShopOrder or);

    List<EsShopOrder> selOrderByPage(OrderParam param);

    List<EsShopOrder> selectfriend(OrderParam entity);

    List<EsShopOrder> selectOrderList(OrderParam entity);

    int selectOrderCount(OrderParam entity);

    EsShopOrder selectOrderItemById(Long orderId);

    boolean hasOrderBefore(@Param("userId") Long userId);

    Integer orderonnumber(Integer memberId);

    List<EsShopOrder> selectOrderListByApplet(AppletOrderParam appletOrderParam);

    List<EsShopOrder> selectFriendListByApplet(AppletOrderParam appletOrderParam);

    int selectOrderCountByApplet(AppletOrderParam appletOrderParam);

    List<EsShopOrder> queryAutoCloseOrders();

    List<EsShopOrder> queryAutoConfirmOrders(@Param("days") int days);

    int hasCountByMemberById(@Param("memberId") Long memberId);

    int hasPayied(@Param("memberId") Long memberId);

    List<OrderStstic> listOrderGroupByMemberId();

    List<OrderStstic> listOrderGroupByManJianId();

    List<OrderStstic> listOrderGroupByDiscountId();


    EsShopOrder getOrderBuyerByMemberId(Long memberId);

    Integer getBuyCountByUserId(@Param("memberId") Long memberId, @Param("goodsId") Long goodsId);


    OrderCustTotalVo getOldConsumeInfo(TradeAnalyzeParam param);

    OrderCustTotalVo getTotalInfo(TradeAnalyzeParam param);

    Integer selectUCustoCount(TradeAnalyzeParam param);

    List<EsShopOrder> getNewOrderList(CustTendencyParam param);

    //公用时间查orderid
    List<Long> orderIdList(GoodsAnalyzeParam param);

    List<Long> orderIdList2(TradeAnalyzeParam param);

    //付款人数
    List<Long> memberIdList(TradeAnalyzeParam param);

    List<EsShopOrder> getOldOrderList(CustTendencyParam param);


    Integer hasOrder(Long memberId);

    Date getMinCreateTime(Long shopId);

    Integer getBuyCountByMemberId(@Param("memberId") Long memberId, @Param("goodsId") Long goodsId, @Param("status") Integer status);

    List<EsShopOrder> getOrderListByCon(OrderParam entity);


    Integer getCountByPage(OrderParam entity);

    Integer getCountByCon(OrderParam entity);


    List<EsShopOrder> getNewOrderInfo(CustTradeSuccessParam param);

    List<EsShopOrder> getOldOrderIfo(CustTradeSuccessParam param);

    List<EsShopOrder> getAllOrderInfo(CustTradeSuccessParam param);

    List<Long> selOrderIdsByGoodsAnParam(GoodsAnalyzeParam param);

    Integer sumByGoods(@Param("memberId") Long memberId, @Param("goodsId") Long goodsId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<Integer> paymentNumber(EsShopOrder entity);

    List<EsShopOrder> selCountDateDetail(EsShopOrder entity);
}
