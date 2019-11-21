package com.mei.zhuang.controller.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.arvato.common.redis.template.RedisRepository;
import com.arvato.ec.common.exception.BusinessException;
import com.arvato.ec.common.utils.JsonUtils;
import com.arvato.ec.common.vo.EsMiniprogram;
import com.arvato.ec.common.vo.OmsOrderVo;
import com.arvato.ec.common.vo.order.*;
import com.arvato.service.order.api.enums.OrderStatus;
import com.arvato.service.order.api.feigin.MembersFegin;
import com.arvato.service.order.api.mq.Sender;
import com.arvato.service.order.api.orm.dao.EsCoreMessageTemplateMapper;
import com.arvato.service.order.api.orm.dao.EsShopFormidMapper;
import com.arvato.service.order.api.service.EsShopCustomizedAppletService;
import com.arvato.service.order.api.service.ShopOrderGoodsService;
import com.arvato.service.order.api.service.ShopOrderService;
import com.arvato.service.order.api.service.impl.WechatApiService;
import com.arvato.service.order.api.utils.TemplateData;
import com.arvato.service.order.api.utils.WX_TemplateMsgUtil;
import com.arvato.utils.CommonResult;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.mei.zhuang.entity.order.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/13 14:38
 * @Description:
 */
@Slf4j
@Api(value = "小程序订单管理", description = "", tags = {"小程序订单管理"})
@RestController
@RequestMapping("/applet")
public class AppletOrderController {
    @Resource
    private MembersFegin memberFegin;
    @Resource
    private ShopOrderService orderService;
    @Resource
    private ShopOrderGoodsService orderGoodsService;
    @Resource
    private EsShopFormidMapper formidMapper;
    @Resource
    private RedisRepository redisRepository;
    @Resource
    private EsShopCustomizedAppletService esShopCustomizedAppletService;
    @Autowired
    private Sender sender;
    @Resource
    private EsCoreMessageTemplateMapper esCoreMessageTemplateMapper;
    @Resource
    private WechatApiService wechatApiService;
/*    @ApiOperation("根据条件查询所有订单列表")
    @PostMapping(value = "/orderList")
    public Object orderList(OrderParam entity) {
        try {
            Page<EsShopOrder> orderList = orderService.selectPageExt(entity);
            return new CommonResult().success(orderList);
        } catch (Exception e) {
            log.error("根据条件查询所有订单列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }*/

    @ApiOperation("根据条件查询送礼")
    @PostMapping(value = "/friendList")
    public Object friendList(OrderParam entity) {
        try {
            List<EsShopOrder> selectfriend = orderService.selectfriend(entity);
            return new CommonResult().success(selectfriend);
        } catch (Exception e) {
            log.error("根据条件查询送礼：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("订单详情")
    @PostMapping(value = "/orderDetail")
    public Object orderDetail(@ApiParam("订单id") @RequestParam("id") Long id) {

        if (ValidatorUtils.empty(id)) {
            return new CommonResult().paramFailed();
        }
        return new CommonResult().success(orderService.orderDetail(id));
    }

    /**
     * 验证码换礼物
     *
     * @param orderParam
     * @return
     */
    @ApiOperation("验证码换礼物")
    @PostMapping(value = "/codeToGift")
    public Object codeToGift(BookOrderParam orderParam) {
        return orderService.codeToGift(orderParam);
    }

    /**
     * 计算邮费
     *
     * @param orderParam
     * @return
     */
    @ApiOperation("计算邮费")
    @PostMapping(value = "/addressToFee")
    public BigDecimal addressToFee(BookOrderParam orderParam) {
        return orderService.addressToFee(orderParam);
    }
    /**
     * 商品详情预览订单
     *
     * @param orderParam
     * @return
     */
    @ApiOperation("购物车预览订单")
    @PostMapping(value = "/preOrder")
    public Object preOrder(BookOrderParam orderParam) {
        try {
            return orderService.preOrder(orderParam);
        } catch (BusinessException e) {
            return new CommonResult().failed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
}

    /**
     * 提交订单
     *
     * @param orderParam·
     * @return
     */
    @ApiOperation("商品详情生成订单")
    @PostMapping(value = "/bookOrder")
    public Object bookOrder(BookOrderParam orderParam) {
        try {
            return orderService.bookOrder(orderParam);
        } catch (BusinessException e) {
            return new CommonResult().failed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation("好友详情预览")
    @PostMapping(value = "/friendOrder")
    @ResponseBody
    public Object friendOrder( BookOrderParam orderParam) {
        try {
            return orderService.friendOrder(orderParam);
        } catch (BusinessException e) {
            return new CommonResult().failed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @ApiOperation("好友下订单")
    @PostMapping(value = "/friendbookOrder")
    public Object friendbookOrder(BookOrderParam orderParam) {
        try {
            return orderService.friendbookOrder(orderParam);
        } catch (BusinessException e) {
            return new CommonResult().failed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation("好友拆礼物")
    @PostMapping(value = "/orderGoodslist")
    public Object orderGoodslist(long orderId) {
        try {
            return orderGoodsService.orderlist(orderId);
        } catch (BusinessException e) {
            return new CommonResult().failed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @ApiOperation("添加商品到购物车")
    @RequestMapping(value = "/addCart")
    @ResponseBody
    public Object addCart(CartParam cartParam) {
        if (ValidatorUtils.empty(cartParam.getTotal())) {
            return new CommonResult().paramFailed("数量为空");
        }
        if (ValidatorUtils.empty(cartParam.getGoodsId())) {
            return new CommonResult().paramFailed("商品id为空");
        }

        if (ValidatorUtils.empty(cartParam.getMemberId())) {
            return new CommonResult().paramFailed("用户id为空");
        }
        try {
            return orderService.addCart(cartParam);
        } catch (BusinessException e) {
            return new CommonResult().failed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @ApiOperation("立即购买")
    @RequestMapping(value = "/linkToBuy")
    @ResponseBody
    public Object linkToBuy(CartParam cartParam) {
        if (ValidatorUtils.empty(cartParam.getTotal())) {
            return new CommonResult().paramFailed("数量为空");
        }
        if (ValidatorUtils.empty(cartParam.getGoodsId())) {
            return new CommonResult().paramFailed("商品id为空");
        }
        if (ValidatorUtils.empty(cartParam.getOptionId())) {
            return new CommonResult().paramFailed("optionId");
        }
        if (ValidatorUtils.empty(cartParam.getMemberId())) {
            return new CommonResult().paramFailed("用户id为空");
        }
        return orderService.linkToBuy(cartParam);

    }

    @ApiOperation("获取某个会员的购物车列表")
    @RequestMapping(value = "/cartList", method = RequestMethod.POST)
    public Object cartList(Long memberId) {
        return new CommonResult().success(orderService.cartList(memberId));
    }

    @ApiOperation("获取某个会员的带营销购物车列表")
    @RequestMapping(value = "/cartPromotionList", method = RequestMethod.POST)
    public Object cartPromotionList(Long memberId) {
        return new CommonResult().success(orderService.cartPromotionList(memberId));
    }

    @ApiOperation("获取某个会员的某个购物车数量")
    @RequestMapping(value = "/cartGoodsCount", method = RequestMethod.POST)
    public Object cartGoodsCount(Long memberId) {

        Integer goodsCount = orderService.cartGoodsCount(memberId);
        if (goodsCount != null &&  goodsCount>0) {
            return new CommonResult().success(goodsCount);
        } else {
            return new CommonResult().success(0);
        }

    }

    @ApiOperation("修改购物车中某个商品的数量")
    @RequestMapping(value = "/update/quantity", method = RequestMethod.POST)
    public Object updateQuantity(EsShopCart cartParam) {
        if (ValidatorUtils.empty(cartParam.getTotal())) {
            return new CommonResult().paramFailed("数量为空");
        }
        if (ValidatorUtils.empty(cartParam.getId())) {
            return new CommonResult().paramFailed("购物车id为空");
        }
        int count = orderService.updateQuantity(cartParam);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("删除购物车中的某个商品")
    @RequestMapping(value = "/delete")
    public Object delete(CartParam cartParam) {

        if (ValidatorUtils.empty(cartParam.getIds())) {
            return new CommonResult().paramFailed("购物车ids为空");
        }
        List<Long> resultList = new ArrayList<>(cartParam.getIds().split(",").length);
        for (String s : cartParam.getIds().split(",")) {
            resultList.add(Long.valueOf(s));
        }
        int count = orderService.deleteCartIds(resultList);
        if (count > 0) {
            for (Long id:resultList) {
                EsShopCustomizedApplet applet = new EsShopCustomizedApplet();
                applet.setCartId(id);
                esShopCustomizedAppletService.delete(new QueryWrapper<>(applet));//删除定制服务
            }

            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("批量选中取消购物车商品")
    @RequestMapping(value = "/isCheckCart")
    public Object isCheckCart(CartParam cartParam) {

        if (ValidatorUtils.empty(cartParam.getIds())) {
            return new CommonResult().paramFailed("购物车ids为空");
        }
        if (ValidatorUtils.empty(cartParam.getIsSelected())) {
            return new CommonResult().paramFailed("勾选状态");
        }
        List<Long> resultList = new ArrayList<>(cartParam.getIds().split(",").length);
        for (String s : cartParam.getIds().split(",")) {
            resultList.add(Long.valueOf(s));
        }
        int count = orderService.isCheckCart(cartParam.getIsSelected(), resultList);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("清空购物车")
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    @ResponseBody
    public Object clear(EsShopCart cartParam) {
        if (ValidatorUtils.empty(cartParam.getMemberId())) {
            return new CommonResult().paramFailed("用户id为空");
        }
        int count = orderService.clearCart(cartParam.getMemberId());

        return new CommonResult().success(count);

    }

    @ApiOperation("获取某个会员的订单列表")
    @RequestMapping(value = "/orderListByMemberId", method = RequestMethod.POST)
    public Object orderList(AppletOrderParam appletOrderParam) {
        return new CommonResult().success(orderService.selectPageExtByApplet(appletOrderParam));
    }
    @ApiOperation("获取某个会员的送礼订单列表")
    @RequestMapping(value = "/orderFriendByMemberId", method = RequestMethod.POST)
    public Object orderFriendByMemberId(AppletOrderParam appletOrderParam) {
        return new CommonResult().success(orderService.selectPageExtByApplet(appletOrderParam));
    }

    @ApiOperation("修改订单状态")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
     public Object updateStatus(Long orderId, Integer status) {
        return new CommonResult().success(orderService.updateStatus(orderId, status));
    }

    @ApiOperation("保存订单")
    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
    public Object saveOrder(@RequestBody EsShopOrder order) {
        try {
//            orderService.insert(order);
            if (orderService.saveOrder(order)) {
                return new CommonResult().success("保存成功");
            }
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("保存订单：%s", e.getMessage(), e);

        }
        return new CommonResult().failed("操作失败");
    }

    @ApiOperation("关闭订单")
    @RequestMapping(value = "/closeOrder", method = RequestMethod.POST)
    public Object closeOrder(@ApiParam("订单id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("订单id is empty");
            }
            EsShopOrder newE = orderService.selectById(id);
            if (newE.getStatus() != OrderStatus.INIT.getValue()) {
                return new CommonResult().paramFailed("订单已支付，不能关闭");
            }
            if (orderService.closeOrder(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新订单：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @ApiOperation("根据用户id获得不同订单状态的数量")
    @RequestMapping(value = "/getDiffStatusCount", method = RequestMethod.POST)
    public Object getDiffStatusCount(@ApiParam("用户id") @RequestParam Long memberId) {
        try {
            if (ValidatorUtils.empty(memberId)) {
                return new CommonResult().paramFailed("用户id is empty");
            }
            return new CommonResult().success(orderService.getDiffStatusCount(memberId));
        } catch (Exception e) {
            log.error("根据用户id获得不同订单状态的数量：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    /**
     * 提交小程序推送formid
     *
     * @param request
     * @param response
     * @param formId   小程序推送formId
     * @return
     */
    @ApiOperation(value = "提交小程序推送formid")
    @RequestMapping(value = "/submitFormId", method = RequestMethod.POST)
    public Object submitFormId(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam String formId,
                               @RequestParam Long memberId) {

        EsShopFormid query = new EsShopFormid();

        if (ValidatorUtils.empty(formId)) {
            return new CommonResult().failed("前置参数错误，formId不能为空");
        }
        if ("the formId is a mock one".equals(formId)){
            return new CommonResult().success("需要打开调试模式或者上线");
        }
        if (ValidatorUtils.empty(memberId)) {
            return new CommonResult().failed("前置参数错误，memberId不能为空");
        }
        query.setFormId(formId);
        EsShopFormid entity = formidMapper.selectOne(query);
        //校验formId是否已经存在
        if (entity != null) {
            return new CommonResult().failed("前置参数错误，formId已经存在 formId：" + formId);
        }
        entity = new EsShopFormid();

        entity.setMemberId(memberId);
        entity.setFormId(formId);
        entity.setStatus(1);
        entity.setCreateTime(new Date());
        formidMapper.insert(entity);

        return new CommonResult().success("添加成功");
    }

    @ApiOperation("保存商品定制服务")
    @PostMapping("/insCustService")
    public Object insCustService(EsShopCustAppletParam entity) {
        try {
            if (ValidatorUtils.empty(entity.getGoodsId())) {
                return new CommonResult().failed("商品编号为空");
            }
            if (ValidatorUtils.empty(entity.getMemberId())) {
                return new CommonResult().failed("请指定用户");
            }
            if (ValidatorUtils.empty(entity.getBasicId())) {
                return new CommonResult().failed("请指定刻字服务编号");
            }
            return new CommonResult().success("success", esShopCustomizedAppletService.saveCust(entity));
        } catch (Exception e) {
            log.error("保存商品定制服务异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @ApiOperation("修改商品定制服务")
    @PostMapping("/updCustService")
    public Object updCustService(EsShopCustomizedApplet entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("请指定定制服务编号");
            }
            esShopCustomizedAppletService.updateById(entity);
            return new CommonResult().success("success",esShopCustomizedAppletService.detailCustService(entity));
        } catch (Exception e) {
            log.error("修改商品定制服务异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @ApiOperation("商品定制服务详情")
    @PostMapping("/detailCustService")
    public Object detailCustService(EsShopCustomizedApplet entity) {
        try {
            if (ValidatorUtils.empty(entity.getGoodsId())) {
                return new CommonResult().failed("请指定商品ID");
            }
            if (ValidatorUtils.empty(entity.getCartId())) {
                return new CommonResult().failed("请指定购物车商品ID");
            }
            return new CommonResult().success("success", esShopCustomizedAppletService.detailCustService(entity));
        } catch (Exception e) {
            log.error("商品定制服务详情异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @ApiOperation("同步失敗訂單到MQ")
    @PostMapping("/synOrder")
    public Object synOrder() {
        List<Object> ids =  redisRepository.getList("order_error_ids",1,100);
        for (Object id : ids){
            log.info("同步失敗訂單:"+id);
            OmsOrderVo orderVo = new OmsOrderVo();
            EsShopOrder order = orderService.selectById((Serializable) id);
            orderVo.setOrder(order);
            orderVo.setMember(memberFegin.getMemberById(order.getMemberId()));
            orderVo.setOrderGoodsList(orderGoodsService.selectList(new QueryWrapper<>(new EsShopOrderGoods())
                    .eq("order_id", order.getId())));
            sender.createOrderMq(orderVo);
        }
        return new CommonResult().success();
    }

    @ApiOperation("好友赠礼")
    @PostMapping("/friendsGift")
    public Object friendsGift() {
        List<Object> ids =  redisRepository.getList("order_error_ids",1,100);
        for (Object id : ids){
            log.info("同步失敗訂單:"+id);
            OmsOrderVo orderVo = new OmsOrderVo();
            EsShopOrder order = orderService.selectById((Serializable) id);
            orderVo.setOrder(order);
            orderVo.setMember(memberFegin.getMemberById(order.getMemberId()));
            orderVo.setOrderGoodsList(orderGoodsService.selectList(new QueryWrapper<>(new EsShopOrderGoods())
                    .eq("order_id", order.getId())));
            sender.createOrderMq(orderVo);
        }
        return new CommonResult().success();
    }

    @ApiOperation("发送模版消息")
    @PostMapping("/sendTemplate")
    public Object sendTemplate(@RequestParam("opedid")String opedid,
                               @RequestParam("formId")String formId,
                               @RequestParam("shopId")Long shopId,
                               @RequestParam("ids")Long ids,
                               @RequestParam("name")String name,
                               @RequestParam("id")Long id) {
        System.out.println("进来了");
        System.out.println("memberId =="+id+"==  openid  =="+opedid+" == shopId=="+shopId+" == ids =="+ids+"  == formId =="+formId);
        System.out.println();
        Long userId = id;
        log.info("发送模版消息：userId=" + id + "formId=" + formId);
        if (ValidatorUtils.empty(formId)) {
            log.error("发送模版消息：userId=" + userId + ",formId=" + formId);
        }
        String accessToken = null;
        try {
            EsMiniprogram min = memberFegin.getByShopId(shopId);
            System.out.println("min    +"+min);
            EsCoreMessageTemplate messageTemplate = esCoreMessageTemplateMapper.selectById( ids);
            JSONArray a = (JSONArray) JSONArray.parse(messageTemplate.getTemplate());
            Map<String, TemplateData> param = new HashMap<String, TemplateData>();
            int count = 0;
            for (int i = 0; i < a.size(); i++) {
                count++;
                Map map = JsonUtils.readJsonToMap(a.get(i).toString());
                // {"color0":"#000","number0":"[订单号]","title0":"订单编号"}
                if(i == 0){
                    param.put("keyword" + count, new TemplateData(name, map.get("color" + i).toString()));
                }else{
                    param.put("keyword" + count, new TemplateData(map.get("number" + i).toString(), map.get("color" + i).toString()));
                }

            }
            System.out.println("appid + "+min.getAppid()+", appSecret + "+min.getAppSecret());
            accessToken = wechatApiService.getAccessToken(min.getAppid(), min.getAppSecret());
            /* param.put("keyword1", new TemplateData(oderIterm.getTitle(), "#EE0000"));*/
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(param));
//        //调用发送微信消息给用户的接口    ********这里写自己在微信公众平台拿到的模板ID
             String ret = WX_TemplateMsgUtil.sendWechatMsgToUser(opedid, messageTemplate.getOriginalTemplateId(), messageTemplate.getAddress(),
                    formId, jsonObject, accessToken);
            if ("success".equals(ret)) {
                EsShopFormid shopFormid = new EsShopFormid();
                shopFormid.setStatus(2);
                formidMapper.update(shopFormid, new QueryWrapper<EsShopFormid>().eq("form_id", formId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }


}
