/**
 * <p>mallplus Pay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 * <p>
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 * <p>
 * <p>mallplus Pay 交流群: 320860169</p>
 * <p>
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 * <p>
 * <p>自定义 Http 客户端</p>
 *
 * @author Javen
 */
package com.zscat.mallplus.pay.custom;

import com.zscat.mallplus.core.http.AbstractHttpDelegate;

import java.io.InputStream;
import java.util.Map;

public class OkHttpKit extends AbstractHttpDelegate {
    @Override
    public String get(String url) {
        // 替换具体实现
        return super.get(url);
    }

    @Override
    public String post(String url, String data) {
        // 替换具体实现
        return super.post(url, data);
    }

    @Override
    public String post(String url, String data, String certPath, String certPass) {
        // 替换具体实现
        return super.post(url, data, certPath, certPass);
    }

    @Override
    public String post(String url, Map<String, Object> paramMap) {
        // 替换具体实现
        return super.post(url, paramMap);
    }

    @Override
    public String post(String url, String data, InputStream certFile, String certPass) {
        // 替换具体实现
        return super.post(url, data, certFile, certPass);
    }

}
