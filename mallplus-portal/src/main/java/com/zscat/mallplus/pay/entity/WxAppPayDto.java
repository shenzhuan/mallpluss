package com.zscat.mallplus.pay.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>mallplus Pay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 * <p>
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 * <p>
 * <p>mallplus Pay 交流群: 320860169</p>
 * <p>
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 * <p>
 * <p>微信配置 Bean</p>
 *
 * @author Javen
 */

@Setter
@Getter
public class WxAppPayDto {
    //{""partnerid":"1533901051","prepayid":"wx05195730369109c4705757441163586100","noncestr":"1575547050392","timestamp":"1575547050"}

    private String packages;
    private String appid;
    private String noncestr;

    private String partnerid;
    private String prepayid;
    private String timestamp;
    private String sign;
}
