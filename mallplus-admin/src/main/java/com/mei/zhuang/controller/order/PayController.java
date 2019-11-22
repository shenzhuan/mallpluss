package com.mei.zhuang.controller.order;


import com.arvato.ec.common.vo.EsMiniprogram;
import com.mei.zhuang.vo.order.CartMarkingVo;
import com.arvato.service.order.api.enums.OrderStatus;
import com.arvato.service.order.api.feigin.GoodsFegin;
import com.arvato.service.order.api.feigin.MarkingFegin;
import com.arvato.service.order.api.feigin.MembersFegin;
import com.mei.zhuang.dao.order.EsShopOrderGoodsMapper;
import com.mei.zhuang.dao.order.EsShopOrderMapper;
import com.mei.zhuang.dao.order.EsShopPaymentMapper;
import com.mei.zhuang.service.order.ShopOrderService;
import com.arvato.service.order.api.utils.*;
import com.arvato.service.order.api.utils.DateUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.utils.*;
import com.arvato.utils.util.CharUtil;
import com.arvato.utils.util.IpAddressUtil;
import com.mei.zhuang.utils.ValidatorUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.entity.order.EsShopPayment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.CompletableFuture;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "商户支付管理")
@Slf4j
@RestController
@RequestMapping("/applet/pay")
public class PayController {
    @Resource
    private GoodsFegin goodsFegin;
    String getCode = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STAT#wechat_redirect";

    String orderquery = "https://api.mch.weixin.qq.com/pay/orderquery";
    String refundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    String refundqueryUrl = "https://api.mch.weixin.qq.com/pay/refundquery";

    String uniformorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    String userMessage = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    String webAccessTokenhttps = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    @Resource
    private MembersFegin membersFegin;

    @Resource
    private ShopOrderService orderService;
    @Resource
    private EsShopOrderGoodsMapper orderGoodsMapper;

    @Resource
    private MarkingFegin markingFegin;
    @Resource
    private EsShopOrderMapper orderMapper;

    @Resource
    private EsShopPaymentMapper paymentMapper;

    //返回微信服务
    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }


    //模拟微信回调接口
    public static String callbakcXml(String orderNum) {
        return "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type> <is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid> <out_trade_no><![CDATA[" + orderNum + "]]></out_trade_no>  <result_code><![CDATA[SUCCESS]]></result_code> <return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id> <time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
    }

    /**
     * 获取支付的请求参数
     */
    @SysLog(MODULE = "商户支付管理", REMARK = "获取支付的请求参数")
    @ApiOperation(value = "获取支付的请求参数")
    @PostMapping("prepay")
    public Object payPrepay(@RequestParam(value = "orderId", required = false, defaultValue = "0") Long orderId,
                            @RequestParam(value = "shopId", required = false, defaultValue = "0") Long shopId,
                            @RequestParam(value = "memberId", required = false, defaultValue = "0") Long memberId,
                            @RequestParam(value = "rePay", required = false, defaultValue = "0") Integer rePay,
                            HttpServletRequest request) {
        EsMiniprogram miniprogram = membersFegin.getByShopId(shopId);
        EsMember user = membersFegin.getMemberById(memberId);
        EsShopPayment queryPay = new EsShopPayment();
        queryPay.setStatus(0);
        queryPay.setShopId(shopId);
        queryPay.setType(1);
        queryPay.setIsDelete(0);
        EsShopPayment payment = paymentMapper.selectOne(queryPay);
        EsShopOrder orderInfo = orderService.getById(orderId);
        if (null == miniprogram || payment==null) {
            return new CommonResult().failed("支付参数为空");
        }
        log.info("prepay");
        if (null == orderInfo) {
            return new CommonResult().failed("订单已取消");
        }

        if (orderInfo.getStatus() != OrderStatus.INIT.getValue()) {
            return new CommonResult().failed("订单已支付，请不要重复操作");
        }

        String nonceStr = CharUtil.getRandomString(32);

        Map<Object, Object> resultObj = new TreeMap();

        try {
            Map<Object, Object> parame = new TreeMap<Object, Object>();
            parame.put("appid", miniprogram.getAppid());
            // 商家账号。
            parame.put("mch_id", payment.getMchId());
            String randomStr = CharUtil.getRandomNum(18).toUpperCase();
            // 随机字符串
            parame.put("nonce_str", randomStr);
            // 商户订单编号
            parame.put("out_trade_no", orderInfo.getOrderNo());
            parame.put("body", "小程序支付");
            //订单的商品
            List<EsShopOrderGoods> orderGoods = orderGoodsMapper.selectList(new QueryWrapper<>(new EsShopOrderGoods()).eq("order_id", orderId));
            System.out.println(orderGoods+"商品订单");
            if (null != orderGoods) {
                String body = "";
                int count = 0;
                for (EsShopOrderGoods goodsVo : orderGoods) {
                    if (rePay == 1 && goodsVo.getIsGifts()==0) {
                        EsShopGoods goods = goodsFegin.getGoodsById(goodsVo.getGoodsId());
                        if (goods.getNumMax() > 0 && goodsVo.getCount() > goods.getNumMax()) {
                            return new CommonResult().failed("单次最多购买:" + goods.getNumMax() + "件,当前件数:" + goodsVo.getCount());
                        }
                        if (goods.getNumMin() > 0 && goodsVo.getCount() < goods.getNumMin()) {
                            return new CommonResult().failed("单次最少购买:" + goods.getNumMin() + "件,当前件数:" + goodsVo.getCount());
                        }
                        if (goods.getMostPurchases() > 0) {
                            Integer buyCount = orderMapper.getBuyCountByUserId(goodsVo.getMemberId(), goods.getId());
                            if (ValidatorUtils.empty(buyCount)) {
                                buyCount = 0;
                            }
                            if (buyCount >= goods.getMostPurchases()) {
                                return new CommonResult().failed("最多购买:" + goods.getMostPurchases() + "件,当前已经购买件数:" + buyCount);
                            }
                            if (goodsVo.getCount() > (goods.getMostPurchases() - buyCount)) {
                                return new CommonResult().failed("最多购买:" + goods.getMostPurchases() + "件,已经购买：" + buyCount + ",当前选择件数:" + goodsVo.getCount());
                            }
                        }
                    }
                    if (count == 0) {
                        body = goodsVo.getTitle();
                    }
                    count++;
                }
                // 商品描述
                parame.put("body", body);
            }
            //支付金额
            parame.put("total_fee", orderInfo.getPayPrice().multiply(new BigDecimal(100)).intValue());
            // 回调地址
            parame.put("notify_url", payment.getNotifyUrl());
            // 交易类型APP
            parame.put("trade_type", "JSAPI");
            parame.put("spbill_create_ip", IpAddressUtil.getIpAddr(request));
            parame.put("openid", user.getOpenid());
            String sign = WechatUtil.arraySign(parame, miniprogram.getSignature());
            // 数字签证
            parame.put("sign", sign);
            System.out.println(parame.toString());
            String xml = MapUtils.convertMap2Xml(parame);
            log.info("xml:" + xml);
            Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(uniformorder, xml));
            String prepay_id = MapUtils.getString("prepay_id", resultUn);
            /*if (prepay_id.equals(orderInfo.getOutTradeNo())) {
                return new CommonResult().failed("订单已提交，请不要重复操作");
            }*/

            // 响应报文
            String return_code = MapUtils.getString("return_code", resultUn);
            String return_msg = MapUtils.getString("return_msg", resultUn);
            //
            if (return_code.equalsIgnoreCase("FAIL")) {
                orderService.releaseStock(orderId);
                return new CommonResult().failed("支付失败," + return_msg);
            } else if (return_code.equalsIgnoreCase("SUCCESS")) {
                // 返回数据
                String result_code = MapUtils.getString("result_code", resultUn);
                String err_code_des = MapUtils.getString("err_code_des", resultUn);
                if (result_code.equalsIgnoreCase("FAIL")) {
                    return new CommonResult().failed("支付失败," + err_code_des);
                } else if (result_code.equalsIgnoreCase("SUCCESS")) {

                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
                    resultObj.put("appId", miniprogram.getAppid());
                    resultObj.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
                    resultObj.put("nonceStr", nonceStr);
                    resultObj.put("package", "prepay_id=" + prepay_id);
                    resultObj.put("signType", "MD5");
                    String paySign = WechatUtil.arraySign(resultObj, miniprogram.getSignature());
                    resultObj.put("paySign", paySign);
                    // 业务处理
                    orderInfo.setOutTradeNo(prepay_id);

                    // 付款中
                    orderService.updateById(orderInfo);
                    return new CommonResult().success("微信统一订单下单成功", resultObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("下单失败,error=" + e.getMessage());
        }
        return new CommonResult().failed("下单失败");
    }

    /**
     * 微信订单回调接口
     *
     * @return
     */
    @SysLog(MODULE = "商户支付管理", REMARK = "微信订单回调接口")
    @ApiOperation(value = "微信订单回调接口")
    @RequestMapping(value = "/notify", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("微信订单回调接口");
        try {
            log.info("微信订单回调接口");
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
            //订单编号
            String out_trade_no = result.getOut_trade_no();


            // 业务处理
            EsShopOrder param = new EsShopOrder();
            param.setOrderNo(out_trade_no);
            System.out.println(param);
            List<EsShopOrder> list = orderService.selectList(new QueryWrapper<EsShopOrder>().eq("order_no",out_trade_no));
            EsShopOrder orderInfo = list.get(0);
            if (result_code.equalsIgnoreCase("FAIL")) {
                //订单编号
                log.error("订单" + out_trade_no + "支付失败");
                orderService.releaseStock(orderInfo.getId());
                response.getWriter().write(setXml("SUCCESS", "OK"));
            } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                log.error("订单" + out_trade_no + "支付成功");
                if(orderInfo.getOrderType()==2){
                    orderInfo.setStatus(OrderStatus.RECEIVE.getValue());
                }else {
                    orderInfo.setStatus(OrderStatus.TO_DELIVER.getValue());
                }
                orderInfo.setPayTime(new Date());
                orderInfo.setPayType(1);
                orderService.updateById(orderInfo);
                    CompletableFuture.runAsync(() -> {
                        try {
                            List<EsShopOrderGoods> orderGoods = orderGoodsMapper.selectList(
                                    new QueryWrapper<>(new EsShopOrderGoods()).eq("order_id", orderInfo.getId()).eq("is_gifts",0));
                            EsMember user = membersFegin.getMemberById(orderInfo.getMemberId());
                          if (orderService.hasPayied(user.getId())>0){
                              markingFegin.sendNewCoupon(user.getId(), 3);
                          }
                            CartMarkingVo vo = new CartMarkingVo();
                            vo.setMemberId(orderInfo.getMemberId());
                            vo.setPayAmount(orderInfo.getPayPrice());
                            vo.setShopOrderGoodsList(orderGoods);
                            vo.setScope(2);
                            vo.setOpenId(user.getOpenid());
                            markingFegin.sendManualCoupon(vo);
                            markingFegin.sendFillFillCoupon(vo);
                            markingFegin.sendShopCoupon(vo);

                            for (EsShopOrderGoods goodsVo : orderGoods) {
                                EsShopGoods goods = goodsFegin.getGoodsById(goodsVo.getGoodsId());
                                goods.setSalesCount(goods.getSalesCount() + goodsVo.getCount());
                                goodsFegin.updateGoodsById(goods);
                                // 锁库存
                                orderService.lockStock(goodsVo, 1);
                            }
                            orderService.push(user,orderInfo,orderInfo.getOutTradeNo(),2,orderGoods);
                           /* String formid=orderService.getFormIdByMemberId(user.getId());
                            if (ValidatorUtils.notEmpty(formid)){
                                orderService.push(user,orderInfo,formid,2,orderGoods);
                            }else {
                                log.error("支付消息推送formId不够,memberId="+user.getId());
                            }*/
                        } catch (Exception e) {
                            log.error("异步更新失败：{}", e.getMessage());
                        }
                    });
                response.getWriter().write(setXml("SUCCESS", "OK"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    /**
     * 微信查询订单状态
     */
   /* @ApiOperation(value = "查询订单状态")
    @PostMapping("query")
    public Object orderQuery( @RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        UmsMember user = UserUtils.getCurrentMember();

        OmsOrder orderDetail = orderService.getById(id);
        if (id == null) {
            return new CommonResult().failed("订单不存在");
        }
        Map<Object, Object> parame = new TreeMap<Object, Object>();
        parame.put("appid", wxAppletProperties.getAppId());

        parame.put("mch_id", wxAppletProperties.getMchId());
        String randomStr = CharUtil.getRandomNum(18).toUpperCase();

        parame.put("nonce_str", randomStr);

        parame.put("out_trade_no", orderDetail.getOrderSn());

        String sign = WechatUtil.arraySign(parame, wxAppletProperties.getPaySignKey());

        parame.put("sign", sign);

        String xml = MapUtils.convertMap2Xml(parame);
        log.info("xml:" + xml);
        Map<String, Object> resultUn = null;
        try {
            resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(wxAppletProperties.getOrderquery(), xml));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("查询失败,error=" + e.getMessage());
        }

        String return_code = MapUtils.getString("return_code", resultUn);
        String return_msg = MapUtils.getString("return_msg", resultUn);

        if (!"SUCCESS".equals(return_code)) {
            return new CommonResult().failed("查询失败,error=" + return_msg);
        }

        String trade_state = MapUtils.getString("trade_state", resultUn);
        if ("SUCCESS".equals(trade_state)) {

            OmsOrder orderInfo = new OmsOrder();
            orderInfo.setId(id);
            orderInfo.setStatus(2);
            orderInfo.setConfirmStatus(1);
            orderInfo.setPaymentTime(new Date());
            orderService.updateById(orderInfo);
            return toResponsMsgSuccess("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {


        } else {

            return new CommonResult().failed("查询失败,error=" + trade_state);
        }

        return new CommonResult().failed("查询失败，未知错误");
    }*/
    public static void main(String[] args) {
        String a ="广东省";
        System.out.println(a.substring(0,2));
        System.out.println(com.mei.zhuang.utils.DateUtils.addDay(new Date(),-5));
        System.out.println(System.currentTimeMillis());
    }
}
