package com.mei.zhuang.utils;


import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.enums.OrderStatus;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/21 15:46
 * @Description:
 */
public class OrderUtils {

    public static boolean isPayStatus(EsShopOrder order) {
        if ((order.getStatus() == OrderStatus.TO_DELIVER.getValue() || order.getStatus() == OrderStatus.DELIVERED.getValue() || order.getStatus() == OrderStatus.TRADE_SUCCESS.getValue())) {
            return true;
        }
        return false;
    }
    public static String orderStatus(int status){
        if (status == OrderStatus.INIT.getValue()) {
            return "待付款";
        }
        if (status == OrderStatus.TO_DELIVER.getValue()) {
            return "待发货";
        }
        if (status == OrderStatus.DELIVERED.getValue()) {
            return "待收货";
        }
        if (status == OrderStatus.TRADE_SUCCESS.getValue()) {
            return "已完成";
        }
        if (status == OrderStatus.REFUND.getValue()) {
            return "已退款";
        }
        if (status == OrderStatus.RIGHT_APPLY.getValue()) {
            return "维权中";
        }
        if (status == OrderStatus.RIGHT_APPLYF_SUCCESS.getValue()) {
            return "维权已完成";
        }
       /* if (status == OrderStatus.CANCELED.getValue()) {
            return "已取消";
        }*/
        if (status == OrderStatus.CLOSED.getValue()) {
            return "已关闭";
        }
        if (status == OrderStatus.INVALID.getValue()) {
            return "无效订单";
        }
        if (status == OrderStatus.DELETED.getValue()) {
            return "已删除";
        }
        if (status == OrderStatus.PARTDELIVE.getValue()) {
            return "部分发货状态";
        }
        if(status ==OrderStatus.RECEIVE.getValue()){
            return "待领取";
        }
        return null;
    }

    public boolean isExist(Integer status) {
//        if(status == 0 ||status == 0 ||status == 0 ||status == 0 ||status == 0 ||status == 0 ||status == 0 ||){
//    }
        return false;
    }

}
