package com.mei.zhuang.service.order;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.Table.TableColumnInfo;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.entity.order.EsShopOrderBatchSendDetail;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.vo.data.customer.CustTendencyParam;
import com.mei.zhuang.vo.data.customer.CustTradeSuccessParam;
import com.mei.zhuang.vo.data.goods.GoodsAnalyzeParam;
import com.mei.zhuang.vo.data.goods.GoodsTrendMapParam;
import com.mei.zhuang.vo.data.trade.OrderCustTotalVo;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import com.mei.zhuang.vo.order.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
public interface ShopOrderService extends IService<EsShopOrder> {
    Map<String, Object> homeStatic();

    int delivery(EsShopOrderBatchSendDetail orderBatchSendDetail) throws Exception;

    int partDelivery(EsShopOrderBatchSendDetail orderBatchSendDetail) throws Exception;

    UserOrderDetail selectByUserId(Long userId);

    EsShopOrder detail(Long id);

    OrderDetail orderDetail(Long id);
    Page<EsShopOrder> selectPageExt(OrderParam entity);
    Page<EsShopOrder> selectPageExtByApplet(AppletOrderParam appletOrderParam);

    List<EsShopOrder> selectfriend(OrderParam entity);

    boolean updateStatus(Long id, Integer status);

    boolean closeOrder(Long id);

    boolean updatePrice(EsShopOrder newE, BigDecimal price);

    Object updateDelivery(EsShopOrderBatchSendDetail orderBatchSendDetail);

    int cancleDelivery(EsShopOrder order, String remark);

    boolean exportOrderList(OrderParam entity, ExportParam exportParam, HttpServletResponse response);

    boolean saveOrder(EsShopOrder order) throws Exception;

    boolean deleteOrder(Long id);

    Object testFind();

    Object preOrder(BookOrderParam orderParam);

    //好友赠礼订单详情预览
    Object friendOrder(BookOrderParam orderParam);

    //好友下单
    Object friendbookOrder(BookOrderParam orderParam) throws Exception;

    //下单
    Object bookOrder(BookOrderParam orderParam) throws Exception;

    Integer updateaddress(EsShopOrder entity);


    Object addCart(CartParam cartParam);

    int updateQuantity(EsShopCart cartParam);

    int clearCart(Long memberId);

    int deleteCartIds(List<Long> resultList);

    Map<String,Object> cartList(Long memberId);

    List<EsShopCart> cartPromotionList(Long memberId);

    Integer cartGoodsCount(Long memberId);



    int isCheckCart(Integer isSelected, List<Long> resultList);


    Object linkToBuy(CartParam cartParam);



    Object confimDelivery(Long id);

    void lockStock(EsShopOrderGoods item, Integer stockCnf);

    void releaseStock(long orderId);

    void closeOrdder(Long id);

    /**
     * 查看有没有下过单(支付成功过的)
     * @param memberId
     * @return
     */
    int hasCountByMemberById(Long memberId);

    /**
     * 查询用户有没有支付过
     * @return
     */
    int hasPayied(Long memberId);

    List<OrderStstic> listOrderGroupByMemberId();

    boolean confimPay(Long id);


    EsShopOrder getOrderBuyerByMemberId(Long memberId);

    List<OrderStstic> listOrderGroupByManJianId();

    List<OrderStstic> listOrderGroupByDiscountId();

    List<EsShopOrderGoods> selOrderGoodsByPart(Long orderId);

    UserDiffOrderStatusCount  getDiffStatusCount(Long userId);

    List<TableColumnInfo> selOrderColumnInfo();

    OrderCustTotalVo getOldConsumeInfo(TradeAnalyzeParam param);

    OrderCustTotalVo getTotalInfo(TradeAnalyzeParam param);

    Integer selectUCustoCount(TradeAnalyzeParam param);

    Object codeToGift(BookOrderParam orderParam);

    Integer sumByGoods(Long memberId, Long goodsId, Date startTime, Date endTime);

    Date getMinCreateTime(Long shopId);

    public void push(EsMember member, EsShopOrder order, String formId, int infoId, List<EsShopOrderGoods> orderGoodsList);

    String getFormIdByMemberId(Long id);

    BigDecimal addressToFee(BookOrderParam orderParam);

    List<EsShopOrder> getOldCustOrderList(CustTendencyParam param) ;

    List<EsShopOrder> getNewCustOrderList(CustTendencyParam param) ;

    List<EsShopOrder> getAllOrderInfo(CustTradeSuccessParam param);

    List<EsShopOrder> getOldOrderInfo(CustTradeSuccessParam param);

    List<EsShopOrder> getNewOrderInfo(CustTradeSuccessParam param);

    OrderCustTotalVo getCustOrderInfoByCon(CustTradeSuccessParam param);

    Long selectExist(EsMember entity);

    int selGoodsTotalSaleCount(GoodsAnalyzeParam param);

    List<EsShopOrderGoods> orderGoodsList(GoodsAnalyzeParam param);

    List<EsShopOrderGoods> orderGoodsList(long goodsId);

    List<EsShopOrderGoods> selOrderGoodsByGoodsAnaly(GoodsTrendMapParam param);

    Integer inserts(EsShopCart entity);

    List<Integer> paymentNumber(EsShopOrder entity);

    List<EsShopOrder> selCountDateDetail(EsShopOrder entity);

    List<EsShopOrder> selOrderListByGoodsAnay(GoodsAnalyzeParam param);

    void sendTemplate(String openid, String formId, Long shopId, String s, String nickname, Long id);

    void updvituralStock(Long smallBeautyBoxId, Integer total);
}


