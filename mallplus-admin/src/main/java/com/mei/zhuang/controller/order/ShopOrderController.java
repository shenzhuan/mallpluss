package com.mei.zhuang.controller.order;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mei.zhuang.constant.OrderConstant;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.order.EsDeliveryAddresser;
import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.entity.order.EsShopOrderBatchSendDetail;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.enums.OrderStatus;
import com.mei.zhuang.service.order.ShopDeliveryService;
import com.mei.zhuang.service.order.ShopOrderGoodsService;
import com.mei.zhuang.service.order.ShopOrderService;
import com.mei.zhuang.utils.*;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.data.customer.CustTendencyParam;
import com.mei.zhuang.vo.data.customer.CustTradeSuccessParam;
import com.mei.zhuang.vo.data.goods.GoodsAnalyzeParam;
import com.mei.zhuang.vo.data.goods.GoodsTrendMapParam;
import com.mei.zhuang.vo.data.trade.OrderCustTotalVo;
import com.mei.zhuang.vo.order.ExportParam;
import com.mei.zhuang.vo.order.OrderParam;
import com.mei.zhuang.vo.order.UserOrderDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单管理
 */
@Slf4j
@Api(value = "订单管理", description = "", tags = {"订单管理"})
@RestController
@RequestMapping("/api/order")
public class ShopOrderController {


    @Resource
    private ShopOrderService shopOrderService;
    @Resource
    private ShopDeliveryService shopDeliveryService;

    @Resource
    private ShopOrderGoodsService goodsService;


    @SysLog(MODULE = "订单管理", REMARK = "首页订单统计")
    @ApiOperation("首页订单统计")
    @RequestMapping(value = "/orderStatic", method = RequestMethod.POST)
    public Object orderStatic() throws Exception {
        return new CommonResult().success(shopOrderService.homeStatic());
    }

    @SysLog(MODULE = "订单管理", REMARK = "根据条件查询所有订单列表")
    @ApiOperation("根据条件查询所有订单列表")
    @PostMapping(value = "/list")
    public Object getOrderByPage(OrderParam entity) {
        try {
            if (entity.getKeyword() != null) {
                entity.setKeyword(entity.getKeyword().trim());
            }
            Page<EsShopOrder> orderList = shopOrderService.selectPageExt(entity);
            return new CommonResult().success(orderList);
        } catch (Exception e) {
            log.error("根据条件查询所有订单列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单管理", REMARK = "修改订单备注")
    @ApiOperation("修改订单备注")
    @PostMapping(value = "/updateRemark")
    public Object updateRemark(@ApiParam("订单id") @RequestParam Long id,
                               @ApiParam("订单备注") @RequestParam String remark) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("订单id is empty");
            }
            if (ValidatorUtils.empty(remark)) {
                return new CommonResult().paramFailed("订单备注 is empty");
            }
            EsShopOrder newE = shopOrderService.getById(id);
            if (newE.getRemarkBuyer() != null) {
                newE.setRemarkBuyer(newE.getRemarkBuyer() + "|" + remark);
            } else {
                newE.setRemarkBuyer(remark);
            }
            if (shopOrderService.updateById(newE)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新订单：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单管理", REMARK = "修改订单状态")
    @ApiOperation("修改订单状态")
    @PostMapping(value = "/updateStatus")
    public Object updateStatus(@ApiParam("订单id") @RequestParam Long id,
                               @ApiParam("订单状态") @RequestParam Integer status) {
        try {
            if (id == null) {
                return new CommonResult().paramFailed("订单id不能为空");
            }

            if (status == null) {
                return new CommonResult().paramFailed("订单状态不能为空");
            }
            if (shopOrderService.getById(id) == null) {
                return new CommonResult().failed("不存在id为{" + id + "}的订单");
            }


            if (shopOrderService.updateStatus(id, status)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新订单状态：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单管理", REMARK = "修改订单价格")
    @ApiOperation("修改订单价格")
    @PostMapping(value = "/updatePrice")
    public Object updatePrice(@ApiParam("订单id") @RequestParam Long id,
                              @ApiParam("订单价格") @RequestParam BigDecimal price) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("订单id 不能是空");
            }
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                return new CommonResult().paramFailed("订单价格 不能为0或者是负数");
            }
            EsShopOrder newE = shopOrderService.getById(id);
            if (newE.getStatus() != OrderStatus.INIT.getValue()) {
                return new CommonResult().paramFailed("订单已支付，不能修改价格");
            }
            if (shopOrderService.updatePrice(newE, price)) {
                return new CommonResult().success();
            }

        } catch (Exception e) {
            log.error("修改订单价格：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单管理", REMARK = "关闭订单")
    @ApiOperation("关闭订单")
    @RequestMapping(value = "/closeOrder", method = RequestMethod.POST)
    public Object closeOrder(@ApiParam("订单id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("订单id is empty");
            }
            EsShopOrder newE = shopOrderService.getById(id);
            if (newE.getStatus() != OrderStatus.INIT.getValue()) {
                return new CommonResult().paramFailed("订单已支付，不能关闭");
            }
            if (shopOrderService.closeOrder(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("关闭订单：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单管理", REMARK = "更新订单")
    @ApiOperation("更新订单")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateOrder(EsShopOrder entity) {
        try {
            if (shopOrderService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新订单：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单管理", REMARK = "删除订单")
    @ApiOperation("删除订单")
    @DeleteMapping(value = "/delete")
    public Object deleteOrder(@ApiParam("订单id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("订单id is empty");
            }
            if (shopOrderService.deleteOrder(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除订单：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "地址修改", REMARK = "地址修改")
    @ApiOperation("地址修改")
    @PostMapping(value = "/updateaddress")
    public Object updateaddress(EsShopOrder entity) {
        try {
            if (shopOrderService.updateaddress(entity) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("地址修改", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单管理", REMARK = "查询订单明细,只有订单")
    @ApiOperation("查询订单明细,只有订单")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public Object getOrderById(@ApiParam("订单id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("订单id is empty");
            }
            EsShopOrder order = shopOrderService.getById(id);
            if (!ValidatorUtils.empty(order)) {
                return new CommonResult().success(order);
            } else {
                return new CommonResult().failed("沒有此id：" + id + "的数据");
            }
        } catch (Exception e) {
            log.error("查询订单明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "订单管理", REMARK = "查询订单明细")
    @ApiOperation("查询订单明细")
    @RequestMapping(value = "/detailItem", method = RequestMethod.POST)
    @ResponseBody
    public Object getOrderItemById(@ApiParam("订单id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("订单id is empty");
            }
            if (ValidatorUtils.empty(shopOrderService.getById(id))) {
                return new CommonResult().failed("沒有此id：" + id + "的数据");
            }
            return new CommonResult().success(shopOrderService.detail(id));
        } catch (Exception e) {
            log.error("查询订单明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "订单管理", REMARK = "订单确认发货")
    @ApiOperation("订单确认发货")
    @RequestMapping(value = "/delivery", method = RequestMethod.POST)
    @ResponseBody
    public Object delivery(EsShopOrderBatchSendDetail orderBatchSendDetail) {
        try {
            if (ValidatorUtils.empty(orderBatchSendDetail.getOrderId())) {
                return new CommonResult().paramFailed("订单id不能为空");
            }

            //业务处理
            EsShopOrder tempOrder = shopOrderService.getById(orderBatchSendDetail.getOrderId());
            if (tempOrder == null) {
                return new CommonResult().failed("不存在id为{" + orderBatchSendDetail.getOrderId() + "}的订单");
            }

            if (tempOrder.getStatus() != OrderStatus.TO_DELIVER.getValue()) {
                return new CommonResult().paramFailed("订单状态不是待发货状态不能发货");
            }

            if (ValidatorUtils.empty(tempOrder.getBuyerName()))
                return new CommonResult().paramFailed("收货人姓名不能为空");
            if (ValidatorUtils.empty(tempOrder.getBuyerMobile()))
                return new CommonResult().paramFailed("收货人电话不能为空");
            if (ValidatorUtils.empty(tempOrder.getAddressDetail()))
                return new CommonResult().paramFailed("收货人地址不能为空");
            //验证电话， 地址正确性
           /* if(tempOrder.getBuyerMobile().length() != 11)
                return new CommonResult().paramFailed("收货人电话只能11位");*/
            if (ValidatorUtils.empty(orderBatchSendDetail.getExpressSn())) {
                return new CommonResult().paramFailed("快递单号不能为空");
            }
            if (ValidatorUtils.empty(orderBatchSendDetail.getExpressName())) {
                return new CommonResult().paramFailed("快递公司不能为空");
            }
            if (orderBatchSendDetail.getDeliverType() == null) {
                return new CommonResult().paramFailed("发货类型不能为空");
            }
            if (orderBatchSendDetail.getDeliverType() != 1 && orderBatchSendDetail.getDeliverType() != 2) {
                return new CommonResult().paramFailed("发货类型值输入非法：只能是{1和2}");
            }
            if (orderBatchSendDetail.getExpressSn().length() > OrderConstant.EXPRESS_SN_LENGTH) {
                return new CommonResult().paramFailed("快递单号长度不能超过20位");
            }

            //w 校验， 字母 数字 下划线
            if (!RegexUtils.veryStr(orderBatchSendDetail.getExpressSn(), "^\\w+$")) {
                return new CommonResult().paramFailed("快递单号不能输入非法字符!");
            }
/*

            if(!RegexUtils.veryStr(orderBatchSendDetail.getExpressName(),"^\\w+$")){
                return new CommonResult().paramFailed("快递名称不能输入非法字符!");
            }
*/

//            if(orderBatchSendDetail.getDeliverType() == 1){
            int count = shopOrderService.delivery(orderBatchSendDetail);
            if (count > 0) {
                return new CommonResult().success();
            }
//            }

            /*if(orderBatchSendDetail.getDeliverType() == 2){
                if(ValidatorUtils.empty(orderBatchSendDetail.getGoodsIds())){
                    return new CommonResult().paramFailed("订单部分发货需要给到发货商品的id");
                }

                int count = shopOrderService.partDelivery(orderBatchSendDetail);
                if (count > 0) {
                    return new CommonResult().success();
                }
            }*/


        } catch (Exception e) {
            log.error("订单确认发货：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单管理", REMARK = "订单确认收货")
    @ApiOperation("订单确认收货")
    @RequestMapping(value = "/confimDelivery", method = RequestMethod.POST)
    @ResponseBody
    public Object confimDelivery(@ApiParam("订单id") @RequestParam Long id) {
        try {
            return new CommonResult().success(shopOrderService.confimDelivery(id));
        } catch (Exception e) {
            log.error("订单确认收货：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "订单管理", REMARK = "取消发货")
    @ApiOperation("取消发货")
    @RequestMapping(value = "/cancleDelivery", method = RequestMethod.POST)
    @ResponseBody
    public Object cancleDelivery(@ApiParam("订单id") @RequestParam Long id,
                                 @ApiParam(value = "订单备注", defaultValue = "我就是想取消") @RequestParam String remark) {
        EsShopOrder order = shopOrderService.getById(id);
        if (order == null) {
            return new CommonResult().paramFailed("没有找到id为{" + id + "}的订单");
        }

        if (order.getStatus() != OrderStatus.DELIVERED.getValue()) {
            return new CommonResult().paramFailed("已发货订单的物流信息才能取消发货");
        }
        int count = shopOrderService.cancleDelivery(order, remark);

        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();

    }

    @SysLog(MODULE = "订单管理", REMARK = "修改物流信息")
    @ApiOperation("修改物流信息")
    @RequestMapping(value = "/update/delivery", method = RequestMethod.POST)
    @ResponseBody
    public Object updateDelivery(EsShopOrderBatchSendDetail orderBatchSendDetail) {

        if (ValidatorUtils.empty(orderBatchSendDetail.getExpressName())) {
            return new CommonResult().paramFailed("快递公司名称不能为空");
        }
        if (ValidatorUtils.empty(orderBatchSendDetail.getExpressSn())) {
            return new CommonResult().paramFailed("快递编号不能为空");
        }
        if (ValidatorUtils.empty(orderBatchSendDetail.getDeliverType())) {
            return new CommonResult().paramFailed("发货类型不能为空");
        }

        if (orderBatchSendDetail.getDeliverType() != 1 && orderBatchSendDetail.getDeliverType() != 2) {
            return new CommonResult().paramFailed("发货类型值输入非法：只能是{1和2}");
        }


        if (ValidatorUtils.empty(orderBatchSendDetail.getOrderId())) {
            return new CommonResult().paramFailed("订单id 不能为空");
        }

        //w 校验， 字母 数字 下划线
        if (!RegexUtils.veryStr(orderBatchSendDetail.getExpressSn(), "^\\w+$")) {
            return new CommonResult().paramFailed("快递单号不能输入非法字符!");
        }
       /*
        if(!RegexUtils.veryStr(orderBatchSendDetail.getExpressName(),"^\\w+$")){
            return new CommonResult().paramFailed("快递名称不能输入非法字符!");
        }

        */

        /*EsShopOrder order = shopOrderService.getById(orderBatchSendDetail.getOrderId());
        if (order.getStatus() != OrderStatus.DELIVERED.getValue()) {
            return new CommonResult().paramFailed("已发货订单的物流信息才能修改");
        }
        Integer isPartDelivery = order.getIsPartDelivery();//是否部分发货标识*/

        return shopOrderService.updateDelivery(orderBatchSendDetail);
    }

    @SysLog(MODULE = "订单管理", REMARK = "查询用户订单明细")
    @ApiOperation("查询用户订单明细")
    @PostMapping(value = "/getOrderByUserId")
    public Object getOrderByUserId(@ApiParam("用户id") @RequestParam Long userId) {
        try {
            if (ValidatorUtils.empty(userId)) {
                return new CommonResult().paramFailed("用户id is empty");
            }
            UserOrderDetail detail = shopOrderService.selectByUserId(userId);
            return new CommonResult().success(detail);
        } catch (Exception e) {
            log.error("查询用户订单明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "订单管理", REMARK = "导出订单列表")
    @ApiOperation("导出订单列表")
    @PostMapping(value = "/export")//换get请求成功 handler ：response out 已被call for IllegalStackException(无需改动)
//    @ResponseBody
    public Object exportOrderList(OrderParam entity,
                                  ExportParam exportParam,
                                  HttpServletResponse response) {
        try {
           /* entity.setTimeType("1");
            entity.setStartTime("2019-04-19");
            entity.setSource("1");
            entity.setStatus(0);*/
            //非空处理
            /*if (ValidatorUtils.empty(exportParam.getColumns())) {
                return new CommonResult().failed("需要导出的字段不能为空！");
            }*/
            exportParam.setFileName(DateUtils.format(new Date(), DateUtil.YYYY_MM_DD) + "订单导出列表");
            exportParam.setSheetName("title");
            exportParam.setPath("D:/A");
            exportParam.setHeaders("编号,状态,订单编号,买家昵称,买家电话,支付金额,配送方式,订单来源");
            exportParam.setColumns("id,status,orderNo,memberNickname,memberMobile,payPrice,receiveType,soureType");
//            exportParam.setHeaders("订单来源,买家,支付配送,价格,状态");
//            exportParam.setColumns("soureType,memberNickname,receiveType,payPrice,status");

            boolean exportFlag = shopOrderService.exportOrderList(entity, exportParam, response);
            if (exportFlag) {
                return new CommonResult().success("导出成功！");
            }
        } catch (Exception e) {
            log.error("导出订单列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("导出失败");
    }

    @SysLog(MODULE = "订单管理", REMARK = "根据订单id查询收货信息")
    @ApiOperation("根据订单id查询收货信息")
    @RequestMapping(value = "/selectDeliveryAddressInfoByOrderId", method = RequestMethod.POST)
    @ResponseBody
    public Object selectDeliveryAddressInfo(@ApiParam("订单id") @RequestParam Long orderId) {
        try {
            if (ValidatorUtils.empty(orderId)) {
                return new CommonResult().paramFailed("orderId is empty");
            }
            EsShopOrder order = shopOrderService.getById(orderId);
            if (order != null) {
                EsDeliveryAddresser esDeliveryAddresser = shopDeliveryService.getById(order.getAddressId());
                if (esDeliveryAddresser != null) {
                    return new CommonResult().success(esDeliveryAddresser);
                } else {
                    return new CommonResult().failed();
                }
            } else {
                return new CommonResult().failed("没有找到订单id为:" + orderId + "的收货信息");
            }
        } catch (Exception e) {
            log.error("根据订单id查询收货信息：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单管理", REMARK = "订单确认付款")
    @ApiOperation("订单确认付款")
    @RequestMapping(value = "/confimPay", method = RequestMethod.POST)
    @ResponseBody
    public Object confimPay(@ApiParam("订单id") @RequestParam Long id) {
        try {
            return new CommonResult().success(shopOrderService.confimPay(id));
        } catch (Exception e) {
            log.error("订单确认付款：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "订单管理", REMARK = "根据用户id查询收货信息")
    @ApiOperation("根据用户id查询收货信息")
    @RequestMapping(value = "/selectDeliveryAddressInfoByUserId", method = RequestMethod.POST)
    @ResponseBody
    public Object selectDeliveryAddressInfoByUserId(@ApiParam("用户id") @RequestParam Long userId) {
        try {
            if (ValidatorUtils.empty(userId)) {
                return new CommonResult().paramFailed("用户id 不能为空");
            }
            //获得收货地址信息
            EsShopOrder orderTemp = shopOrderService.getOrderBuyerByMemberId(userId);
            EsDeliveryAddresser addressInfo = new EsDeliveryAddresser();
            if (orderTemp != null) {
                addressInfo.setName(orderTemp.getBuyerName());
                addressInfo.setPhone(orderTemp.getBuyerMobile());
                StringBuffer sb = new StringBuffer("");
                sb.append(orderTemp.getAddressProvince() != null ? orderTemp.getAddressProvince() : "");
                sb.append(orderTemp.getAddressCity() != null ? orderTemp.getAddressCity() : "");
                sb.append(orderTemp.getAddressArea() != null ? orderTemp.getAddressArea() : "");
                sb.append(orderTemp.getAddressDetail() != null ? orderTemp.getAddressDetail() : "");
                addressInfo.setAddress(sb.toString());
                //
            }
            return new CommonResult().success(addressInfo);
        } catch (Exception e) {
            log.error("根据用户id查询收货信息：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("未查询到收货信息");
    }

    @SysLog(MODULE = "订单管理", REMARK = "查询未发货的订单商品")
    @ApiOperation("查询未发货的订单商品")
    @RequestMapping(value = "/selOrderGoodsByPart", method = RequestMethod.POST)
    @ResponseBody
    public Object selOrderGoodsByPart(Long orderId) {
        try {
            if (ValidatorUtils.empty(orderId)) {
                return new CommonResult().paramFailed("订单id 不能为空");
            }
            return new CommonResult().success(shopOrderService.selOrderGoodsByPart(orderId));
        } catch (Exception e) {
            log.error("查询未发货的订单商品：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单管理", REMARK = "查找订单表字段信息")
    @ApiOperation("查找订单表字段信息")
    @RequestMapping(value = "/selOrderColumnInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object selOrderColumnInfo() {
        try {
            //error
//            return new CommonResult().success(shopOrderService.selOrderColumnInfo());
            return new CommonResult().success(EntityInfoUtils.getTableColumnInfo(EsShopOrder.class));
        } catch (Exception e) {
            log.error("查找订单表字段信息：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单管理", REMARK = "根据商品分析条件获取订单数据")
    @ApiOperation("根据商品分析条件获取订单数据")
    @PostMapping("/selOrderByGoods")
    public List<EsShopOrder> selOrderListByGoodsAnay(@RequestBody GoodsAnalyzeParam param) {
        return shopOrderService.selOrderListByGoodsAnay(param);
    }

    @SysLog(MODULE = "订单管理", REMARK = "查找订单表字段信息")
    @ApiOperation("获得新用户订单数据")
    @PostMapping("/getNewCustOrderList")
    @ResponseBody
    public List<EsShopOrder> getNewCustOrderList(@RequestBody CustTendencyParam param) {
        return shopOrderService.getNewCustOrderList(param);
    }

    @SysLog(MODULE = "订单管理", REMARK = "获取老用户订单数据")
    @ApiOperation("获取老用户订单数据")
    @PostMapping("/getOldCustOrderList")
    public List<EsShopOrder> getOldCustOrderList(@RequestBody CustTendencyParam param) {
        return shopOrderService.getOldCustOrderList(param);
    }

    @SysLog(MODULE = "订单管理", REMARK = "获取老用户订单数据")
    @ApiOperation("获取所有用户订单数据")
    @PostMapping("/getAllOrderInfo")
    public List<EsShopOrder> getAllOrderInfo(@RequestBody CustTradeSuccessParam param) {
        return shopOrderService.getAllOrderInfo(param);

    }

    @SysLog(MODULE = "订单管理", REMARK = "获取老用户订单数据")
    @ApiOperation("获取老用户订单数据")
    @PostMapping("/getOldOrderInfo")
    public List<EsShopOrder> getOldOrderInfo(@RequestBody CustTradeSuccessParam param) {
        return shopOrderService.getOldOrderInfo(param);
    }

    @SysLog(MODULE = "订单管理", REMARK = "获取新用户订单数据")
    @ApiOperation("获取新用户订单数据")
    @PostMapping("/getNewOrderInfo")
    public List<EsShopOrder> getNewOrderInfo(@RequestBody CustTradeSuccessParam param) {
        return shopOrderService.getNewOrderInfo(param);
    }

    @SysLog(MODULE = "订单管理", REMARK = "通过条件获取订单新老客户消费信息")
    @ApiOperation("通过条件获取订单新老客户消费信息")
    @PostMapping("/getCustOrderInfoByCon")
    public OrderCustTotalVo getCustOrderInfoByCon(@RequestBody CustTradeSuccessParam param) {
        return shopOrderService.getCustOrderInfoByCon(param);
    }

    @SysLog(MODULE = "订单管理", REMARK = "查询商品总销售量")
    @ApiOperation("查询商品总销售量")
    @PostMapping("/selGoodsTotalSaleCount")
    public int selGoodsTotalSaleCount(@RequestBody GoodsAnalyzeParam param) {
        return shopOrderService.selGoodsTotalSaleCount(param);
    }

    @PostMapping("/orderList")
    public List<EsShopOrderGoods> orderGoodsList(@RequestBody GoodsAnalyzeParam param) {
        return shopOrderService.orderGoodsList(param);
    }

    @PostMapping("/orderGoodsList")
    public List<EsShopOrderGoods> orderGoodsList(@RequestParam long goodsId) {
        return shopOrderService.orderGoodsList(goodsId);
    }

    @SysLog(MODULE = "订单管理", REMARK = "查询商品总销售量")
    @ApiOperation("根据商品分析条件获得订单商品")
    @PostMapping("/selOrderGoodsByGoodsAnaly")
    public List<EsShopOrderGoods> selOrderGoodsByGoodsAnaly(@RequestBody GoodsTrendMapParam param) {
        return shopOrderService.selOrderGoodsByGoodsAnaly(param);
    }


}
