package com.mei.zhuang.vo.order;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-05-21 18:48
 * @Description:
 */
@Data
public class AppletOrderParam {

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     *页码
     */
    private Integer current = 1;

    /**
     * 页码数量
     */
    private Integer size = 10;

    //订单类型
    private Integer orderType;


    //送礼类型
    private Integer giftGiving;

    /**
     *是否顺序
     */
//    private Boolean isAsc = false;
    private Integer isAsc = 0;
}
