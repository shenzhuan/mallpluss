package com.mei.zhuang.vo.order;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-05-29 11:39
 * @Description: 自定义订单基础设置类
 */
@Data
public class OrderSettings {

    /**
     * 自动关闭时间
     */
    private String autoCloseTime;

    /**
     * 自动完成确认天数
     */
    private Integer autoFinshDay;

    /**
     * 是否在下单的时候获取微信收货地址
     */
    private Integer getWechatAddresInfo;






}
