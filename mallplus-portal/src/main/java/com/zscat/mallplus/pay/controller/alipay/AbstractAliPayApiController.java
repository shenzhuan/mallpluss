package com.zscat.mallplus.pay.controller.alipay;


import com.zscat.mallplus.alipay.AliPayApiConfig;

/**
 * @author Javen
 */
public abstract class AbstractAliPayApiController {
    /**
     * 获取支付宝配置
     *
     * @return {@link AliPayApiConfig} 支付宝配置
     */
    public abstract AliPayApiConfig getApiConfig();
}
