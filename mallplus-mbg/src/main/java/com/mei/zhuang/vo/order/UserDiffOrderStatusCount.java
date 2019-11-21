package com.mei.zhuang.vo.order;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-18 10:34
 * @Description:
 */
@Data
public class UserDiffOrderStatusCount {

    private Integer initCount;//待付款
    private Integer toDeliveryCount;//待发货
    private Integer deliveredCount;// 待收货
    private Integer tradeSuccessedCount; // 已完成
    private Integer closedCount;// 已关闭 // 已取消 统一




}
