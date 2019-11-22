package com.mei.zhuang.enums;

/**
 * @Auther: Tiger
 * @Date: 2019-05-09 14:49
 * @Description:订单商品状态
 */
public enum OrderGoodsStatus {

    //0：按订单发货 1:部分发货
    ALLDELIVERY(0),//按订单发货
    PARTDELIVERY(1);//部分发货

    private int value;

    private OrderGoodsStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
