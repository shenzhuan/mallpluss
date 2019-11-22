package com.mei.zhuang.vo.order;

import com.mei.zhuang.entity.order.EsDeliveryAddresser;
import com.mei.zhuang.entity.order.EsShopOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/21 16:15
 * @Description:
 */
@Data
public class UserOrderDetail {
    private List<EsShopOrder> orderList;
    private int statusALl = 0; // 即该用户累计的订单数（以完成支付的订单作为有效订单计算）
    private int statusRight = 0;//维权
    private int statusRefund = 0;//退款数量

    private BigDecimal refundAmout = new BigDecimal(0);//退款金额
    private BigDecimal payAmount = new BigDecimal(0);//累计订单总金额

    private EsDeliveryAddresser addressInfo;//收货信息


}
