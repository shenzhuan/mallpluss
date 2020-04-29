package com.zscat.mallplus.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.vo.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface IOmsOrderService extends IService<OmsOrder> {
    /**
     * 修改订单收货人信息
     */
    @Transactional
    int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam);

    /**
     * 修改订单费用信息
     */
    @Transactional
    int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam);

    /**
     * 修改订单备注
     */
    @Transactional
    int updateNote(Long id, String note, Integer status);


    /**
     * 批量发货
     */
    @Transactional
    int delivery(List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 批量关闭订单
     */
    @Transactional
    int close(List<Long> ids, String note);

    @Transactional
    int singleDelivery(OmsOrderDeliveryParam deliveryParamList);

    /**
     * 订单日统计
     *
     * @param date
     * @return
     */
    Map orderDayStatic(String date);



    Object dayStatic(String date, Integer type);

    /**@Valid
     * 根据商品分类统计订单占比
     */
    OrderCountDto getOrderCount();

    Object getOrderTimeData(Integer status);

    Object chartCount();

    List<OrderStstic> listOrderGroupByStatus(Integer status);

    Map orderMonthStatic(String date,  Integer status);
}
