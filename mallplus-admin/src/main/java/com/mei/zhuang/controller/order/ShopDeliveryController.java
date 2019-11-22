package com.mei.zhuang.controller.order;

import com.mei.zhuang.service.order.ShopDeliveryService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.entity.order.EsDeliveryAddresser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单管理
 */
@Slf4j
@Api(value = "订单物流管理", description = "", tags = {"订单物流管理"})
@RestController
@RequestMapping("/api/order/delivery")
public class ShopDeliveryController {

    @Resource
    ShopDeliveryService deliveryService;

    @SysLog(MODULE = "订单物流管理", REMARK = "查询收货信息")
    @ApiOperation("查询收货信息")
    @RequestMapping(value = "/selectDeliveryAddressInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object selectDeliveryAddressInfo(@ApiParam("收货id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("id is empty");
            }
            EsDeliveryAddresser entity = deliveryService.getById(id);
            if (!ValidatorUtils.empty(entity)) {
                return new CommonResult().success(entity);
            } else {
                return new CommonResult().failed("没有找到该id:" + id + "的收货信息");
            }
        } catch (Exception e) {
            log.error("查询收货信息：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单物流管理", REMARK = "保存收货信息")
    @ApiOperation("保存收货信息")
    @RequestMapping(value = "/saveAddressInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object saveAddressInfo(@ApiParam("收货地址信息") @RequestBody EsDeliveryAddresser entity) {
        try {
            if (deliveryService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存收货信息：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单物流管理", REMARK = "修改收货信息")
    @ApiOperation("修改收货信息")
    @RequestMapping(value = "/update/deliveryAddressInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object updateDeliveryAddressInfo(@ApiParam("修改的收货地址信息") @RequestBody EsDeliveryAddresser addresser) {
        if (ValidatorUtils.empty(addresser)) {
            return new CommonResult().paramFailed("实体 is empty");
        }
        if (deliveryService.updateById(addresser)) {
            return new CommonResult().success();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单物流管理", REMARK = "删除收货信息")
    @ApiOperation("删除收货信息")
    @RequestMapping(value = "/deleteAddressInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteAddressInfo(@ApiParam("id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("id is empty");
            }
            if (deliveryService.deleteById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除收货信息：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

}
