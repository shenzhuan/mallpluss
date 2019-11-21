package com.mei.zhuang.vo.order;

import lombok.Data;

import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/15 23:00
 * @Description:
 */
@Data
public class OrderParam {
    private String timeType; // 1下单时间  2付款时间  3发货时间  4完成时间
    private String startTime;
    private String endTime;
    private String source;// 1微信小程序
    private String keyType; //1订单号  2收货人信息 3快递单号 4商品名称  5商品编码 6.核销员 7.核销店铺 8.自提门店 9.商户名称
    private String keyword;
    private Integer status;//订单状态
    private Long userId ;
    private Integer current = 1;
    private Integer size = 10;
    private Integer isAsc = 0;
    private Integer orderType;
    private Integer giftGiving;

    private List<Long> orderIds;

}
