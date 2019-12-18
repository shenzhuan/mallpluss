package com.zscat.mallplus.b2c;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.enums.AllEnum;
import com.zscat.mallplus.enums.OrderStatus;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.entity.OmsOrderOperateHistory;
import com.zscat.mallplus.oms.service.IOmsOrderItemService;
import com.zscat.mallplus.oms.service.IOmsOrderOperateHistoryService;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.OrderParam;
import com.zscat.mallplus.single.ApiBaseAction;
import com.zscat.mallplus.sms.mapper.SmsGroupMapper;
import com.zscat.mallplus.ums.entity.SysAppletSet;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.mapper.SysAppletSetMapper;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.util.CharUtil;
import com.zscat.mallplus.util.DateUtils;
import com.zscat.mallplus.util.MapUtils;
import com.zscat.mallplus.util.XmlUtil;
import com.zscat.mallplus.util.applet.WechatRefundApiResult;
import com.zscat.mallplus.util.applet.WechatUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.vo.BalancePayParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 作者: @author mallplus <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "商户支付")
@Slf4j
@RestController
public class BPayController extends ApiBaseAction {
    private static Logger LOGGER = LoggerFactory.getLogger(BPayController.class);
    String tradeType = "JSAPI";
    String uniformorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    String orderquery = "https://api.mch.weixin.qq.com/pay/orderquery";
    String refundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    String refundqueryUrl = "https://api.mch.weixin.qq.com/pay/refundquery";
    @Resource
    private IOmsOrderService orderService;
    @Resource
    private IUmsMemberService memberService;
    @Resource
    private IOmsOrderItemService orderItemService;
    @Resource
    private SmsGroupMapper groupMapper;
    @Resource
    private SysAppletSetMapper appletSetMapper;
    @Autowired
    private IOmsOrderOperateHistoryService orderOperateHistoryService;

    /**
     * 订单退款请求
     */
    /*
    @SysLog(MODULE = "pay", REMARK = "订单退款请求")
    @ApiOperation(value = "订单退款请求")
    @PostMapping("refund")
    public Object refund(Integer orderId) {
        //
        OrderVo orderInfo = orderService.queryObject(orderId);

        if (null == orderInfo) {
            return toResponsObject(400, "订单已取消", "");
        }

        if (orderInfo.getOrder_status() == 401 || orderInfo.getOrder_status() == 402) {
            return toResponsObject(400, "订单已退款", "");
        }

        WechatRefundApiResult result = WechatUtil.wxRefund(orderInfo.getId().toString(),
                10.01, 10.01);
        if (result.getResult_code().equals("SUCCESS")) {
            if (orderInfo.getOrder_status() == 201) {
                orderInfo.setOrder_status(401);
            } else if (orderInfo.getOrder_status() == 300) {
                orderInfo.setOrder_status(402);
            }
            orderService.updateAll(orderInfo);
            return toResponsObject(400, "成功退款", "");
        } else {
            return toResponsObject(400, "退款失败", "");
        }
    }*/


    //返回微信服务
    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    //模拟微信回调接口
    public static String callbakcXml(String orderNum) {
        return "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type> <is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid> <out_trade_no><![CDATA[" + orderNum + "]]></out_trade_no>  <result_code><![CDATA[SUCCESS]]></result_code> <return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id> <time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
    }

    /**
     * 余额支付
     */
    @SysLog(MODULE = "pay", REMARK = "余额支付")
    @ApiOperation(value = "余额支付")
    @PostMapping("balancePay")
    public Object balancePay(BalancePayParam payParam) {
        if (payParam.getPayAmount().compareTo(payParam.getBalance()) > 0) {
            return new CommonResult().failed("余额不足！");
        } else {
            OmsOrder order = orderService.blancePay(orderService.getById(payParam.getOrderId()));
            return new CommonResult().success(order);
        }
    }

    /**
     * 积分兑换
     */
    @SysLog(MODULE = "pay", REMARK = "积分兑换")
    @ApiOperation(value = "积分兑换")
    @PostMapping("jifenPay")
    public Object jifenPay(OrderParam payParam) {
        return orderService.jifenPay(payParam);
    }

    /**
     * 获取支付的请求参数
     */
    @SysLog(MODULE = "pay", REMARK = "获取支付的请求参数")
    @ApiOperation(value = "获取支付的请求参数")
    @PostMapping("weixinAppletPay")
    public Object payPrepay(@RequestParam(value = "orderId", required = false, defaultValue = "0") Long orderId) {
        UmsMember user = memberService.getNewCurrentMember();
        //
        OmsOrder orderInfo = orderService.getById(orderId);
        SysAppletSet appletSet = appletSetMapper.selectOne(new QueryWrapper<>());
        if (null == appletSet) {
            throw new ApiMallPlusException("没有设置支付配置");
        }

        if (null == orderInfo) {
            return toResponsObject(400, "订单已取消", "");
        }

        if (orderInfo.getStatus() == OrderStatus.CLOSED.getValue()) {
            return toResponsObject(400, "订单已已关闭，请不要重复操作", "");
        }
        if (orderInfo.getStatus() != OrderStatus.INIT.getValue()) {
            return toResponsObject(400, "订单已支付，请不要重复操作", "");
        }

        String nonceStr = CharUtil.getRandomString(32);

        //https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=3
        Map<Object, Object> resultObj = new TreeMap();

        try {
            Map<Object, Object> parame = new TreeMap<Object, Object>();
            parame.put("appid", appletSet.getAppid());
            // 商家账号。
            parame.put("mch_id", appletSet.getMchid());
            String randomStr = CharUtil.getRandomNum(18).toUpperCase();
            // 随机字符串
            parame.put("nonce_str", randomStr);
            // 商户订单编号
            parame.put("out_trade_no", orderInfo.getOrderSn());

            // 商品描述
            parame.put("body", "超市-支付");

            //支付金额
            parame.put("total_fee", orderInfo.getPayAmount().multiply(new BigDecimal(100)).intValue());
            // 回调地址
            parame.put("notify_url", appletSet.getNotifyurl());
            // 交易类型APP
            parame.put("trade_type", tradeType);
            parame.put("spbill_create_ip", getClientIp());
            parame.put("openid", user.getWeixinOpenid());
            String sign = WechatUtil.arraySign(parame, appletSet.getPaySignKey());
            // 数字签证
            parame.put("sign", sign);

            String xml = MapUtils.convertMap2Xml(parame);
            log.info("xml:" + xml);
            Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(uniformorder, xml));
            // 响应报文
            String return_code = MapUtils.getString("return_code", resultUn);
            String return_msg = MapUtils.getString("return_msg", resultUn);
            //
            if (return_code.equalsIgnoreCase("FAIL")) {
                return new CommonResult().failed("支付失败," + return_msg);
            } else if (return_code.equalsIgnoreCase("SUCCESS")) {
                // 返回数据
                String result_code = MapUtils.getString("result_code", resultUn);
                String err_code_des = MapUtils.getString("err_code_des", resultUn);
                if (result_code.equalsIgnoreCase("FAIL")) {
                    return new CommonResult().failed("支付失败," + err_code_des);
                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                    String prepay_id = MapUtils.getString("prepay_id", resultUn);
                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
                    resultObj.put("appId", appletSet.getAppid());
                    resultObj.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
                    resultObj.put("nonceStr", nonceStr);
                    resultObj.put("package", "prepay_id=" + prepay_id);
                    resultObj.put("signType", "MD5");
                    String paySign = WechatUtil.arraySign(resultObj, appletSet.getPaySignKey());
                    resultObj.put("paySign", paySign);
                    // 业务处理
                    orderInfo.setPrepayId(prepay_id);
                    // 付款中
                    orderInfo.setStatus(OrderStatus.PayNotNotice.getValue());
                    orderService.updateById(orderInfo);
                    if (orderInfo.getPid() == null) {
                        OmsOrder neworder = new OmsOrder();
                        neworder.setStatus(OrderStatus.TO_DELIVER.getValue());
                        neworder.setPayType(AllEnum.OrderPayType.weixinAppletPay.code());
                        neworder.setPaymentTime(new Date());
                        orderService.update(neworder, new QueryWrapper<OmsOrder>().eq("pid", orderInfo.getId()));
                    }
                    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                    history.setOrderId(orderInfo.getId());
                    history.setCreateTime(new Date());
                    history.setOperateMan("shop");
                    history.setPreStatus(OrderStatus.INIT.getValue());
                    history.setOrderStatus(OrderStatus.TO_DELIVER.getValue());
                    history.setNote("小程序支付");
                    orderOperateHistoryService.save(history);
                    return toResponsObject(200, "微信统一订单下单成功", resultObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("下单失败,error=" + e.getMessage());
        }
        return new CommonResult().failed("下单失败");
    }

    /**
     * 微信查询订单状态
     */
    @SysLog(MODULE = "pay", REMARK = "查询订单状态")
    @ApiOperation(value = "查询订单状态")
    @PostMapping("query")
    public Object orderQuery(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        UmsMember user = memberService.getNewCurrentMember();
        //
        SysAppletSet appletSet = appletSetMapper.selectOne(new QueryWrapper<>());
        if (null == appletSet) {
            throw new ApiMallPlusException("没有设置支付配置");
        }
        OmsOrder orderDetail = orderService.getById(id);
        if (id == null) {
            throw new ApiMallPlusException("订单不存在");
        }
        Map<Object, Object> parame = new TreeMap<Object, Object>();
        parame.put("appid", appletSet.getAppid());
        // 商家账号。
        parame.put("mch_id", appletSet.getMchid());
        String randomStr = CharUtil.getRandomNum(18).toUpperCase();
        // 随机字符串
        parame.put("nonce_str", randomStr);
        // 商户订单编号
        parame.put("out_trade_no", orderDetail.getOrderSn());

        String sign = WechatUtil.arraySign(parame, appletSet.getPaySignKey());
        // 数字签证
        parame.put("sign", sign);

        String xml = MapUtils.convertMap2Xml(parame);
        log.info("xml:" + xml);
        Map<String, Object> resultUn = null;
        try {
            resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(orderquery, xml));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiMallPlusException("查询失败,error=" + e.getMessage());
        }
        // 响应报文
        String return_code = MapUtils.getString("return_code", resultUn);
        String return_msg = MapUtils.getString("return_msg", resultUn);

        if (!"SUCCESS".equals(return_code)) {
            throw new ApiMallPlusException("查询失败,error=" + return_msg);
        }

        String trade_state = MapUtils.getString("trade_state", resultUn);
        if ("SUCCESS".equals(trade_state)) {
            // 更改订单状态
            // 业务处理
            OmsOrder orderInfo = new OmsOrder();
            orderInfo.setId(id);
            orderInfo.setStatus(2);
            orderInfo.setConfirmStatus(1);
            orderInfo.setPaymentTime(new Date());
            orderService.updateById(orderInfo);
            return new CommonResult().success("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {
            // 重新查询 正在支付中
           /* Integer num = (Integer) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + id + "");
            if (num == null) {
                J2CacheUtils.put(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + id + "", 1);
                this.orderQuery(id);
            } else if (num <= 3) {
                J2CacheUtils.remove(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + id);
                this.orderQuery(id);
            } else {
                throw new ApiMallPlusException("查询失败,error=" + trade_state);
            }*/

        } else {
            // 失败
            throw new ApiMallPlusException("查询失败,error=" + trade_state);
        }

        throw new ApiMallPlusException("查询失败，未知错误");
    }

    /**
     * 微信订单回调接口
     *
     * @return
     */
    @SysLog(MODULE = "pay", REMARK = "微信订单回调接口")
    @ApiOperation(value = "微信订单回调接口")
    @RequestMapping(value = "/notify", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            InputStream in = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();
            //xml数据
            String reponseXml = new String(out.toByteArray(), "utf-8");

            WechatRefundApiResult result = (WechatRefundApiResult) XmlUtil.xmlStrToBean(reponseXml, WechatRefundApiResult.class);
            String result_code = result.getResult_code();
            if (result_code.equalsIgnoreCase("FAIL")) {
                //订单编号
                String out_trade_no = result.getOut_trade_no();
                log.error("订单" + out_trade_no + "支付失败");
                response.getWriter().write(setXml("SUCCESS", "OK"));
            } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                //订单编号
                String out_trade_no = result.getOut_trade_no();
                log.error("订单" + out_trade_no + "支付成功");
                // 业务处理
                OmsOrder param = new OmsOrder();
                param.setOrderSn(out_trade_no);
                List<OmsOrder> list = orderService.list(new QueryWrapper<>(param));
                OmsOrder orderInfo = list.get(0);
                orderInfo.setStatus(2);
                orderInfo.setPaymentTime(new Date());
                orderService.updateById(orderInfo);

                response.getWriter().write(setXml("SUCCESS", "OK"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
