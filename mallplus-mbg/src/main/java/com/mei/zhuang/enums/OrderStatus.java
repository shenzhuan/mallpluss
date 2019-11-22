package com.mei.zhuang.enums;

/**
 * @Auther: Tiger
 * @Date: 2019-04-26 16:04
 * @Description:
 */
public enum OrderStatus {


    //    订单状态：0->待付款；1->待发货；2->待收货；3->已完成；->4已退款；->5维权中；->6维权已完成；->7已取消；->8已关闭；->9无效订单；
    INIT(0),//待付款
    TO_DELIVER(1),//待发货
    DELIVERED(2),  // 待收货
    TRADE_SUCCESS(3), // 已完成
    REFUND(4),  // 已退款
    RIGHT_APPLY(5), // 维权中
    RIGHT_APPLYF_SUCCESS(6), // 维权已完成
    //    CANCELED(7),
    CLOSED(8), // 已关闭 // 已取消 统一
    INVALID(9),//无效订单
    DELETED(10),//已删除
    PARTDELIVE(11),//部分发货状态
    RECEIVE(12); //待领取

    private int value;

    private OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
