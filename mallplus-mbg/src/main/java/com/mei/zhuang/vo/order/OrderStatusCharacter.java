package com.mei.zhuang.vo.order;

/**
 * @Auther: Tiger
 * @Date: 2019-04-28 10:15
 * @Description: 订单状态字符
 */
public class OrderStatusCharacter {

    /**
     * 根据数字获得该数字对应的订单状态中文名
     */
    public static String getStatusCharByNum(Integer status) {
        if (status == null) {
            return null;
        }
        String result;
        switch (status) {
            case 0:
                result = "待付款";
                break;
            case 1:
                result = "待发货";
                break;
            case 2:
                result = "待收货";
                break;
            case 3:
                result = "已完成";
                break;
            case 4:
                result = "以退款";
                break;
            case 5:
                result = "维权中";
                break;
            case 6:
                result = "维权已完成";
                break;
            case 7:
                result = "已取消";
                break;
            case 8:
                result = "已关闭";
                break;
            case 9:
                result = "无效订单";
                break;
            case 10:
                result = "已删除";
                break;
            default:
                result = null;
                break;
        }
        return result;

    }


}
