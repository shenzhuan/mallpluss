package com.mei.zhuang.controller.order;

import com.arvato.ec.common.vo.order.OrderStstic;
import com.arvato.service.order.api.service.ShopOrderService;
import com.arvato.utils.annotation.SysLog;
import com.mei.zhuang.entity.order.EsShopOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-05-18 9:46
 * @Description:
 */
@Api(value = "订单对外接口管理", description = "", tags = {"订单对外接口管理"})
@Slf4j
@RestController
@RequestMapping("/api/rpc")
public class OrderRpcController {


    @Resource
    private ShopOrderService orderService;

    @SysLog(MODULE = "订单对外接口管理", REMARK = "根据用户id获得订单组信息")
    @ApiOperation("根据用户id获得订单组信息")
    @PostMapping("/listOrderGroupByMemberId")
    public List<OrderStstic> listOrderGroupByMemberId() {
        try {
            //非空处理
            return orderService.listOrderGroupByMemberId();
        } catch (Exception e) {
            log.error("根据会员id分组订单信息：%s", e.getMessage(), e);
        }
        return null;
    }

    @SysLog(MODULE = "订单对外接口管理", REMARK = "根据满减id获得订单组信息")
    @ApiOperation("根据满减id获得订单组信息")
    @PostMapping("/listOrderGroupByManJianId")
    List<OrderStstic> listOrderGroupByManJianId() {
        try {
            //非空处理
            return orderService.listOrderGroupByManJianId();
        } catch (Exception e) {
            log.error("根据会员id分组订单信息：%s", e.getMessage(), e);
        }
        return null;
    }

    @SysLog(MODULE = "订单对外接口管理", REMARK = "根据折扣id获得订单组信息")
    @ApiOperation("根据折扣id获得订单组信息")
    @PostMapping("/listOrderGroupByDiscountId")
    List<OrderStstic> listOrderGroupByDiscountId() {
        try {
            //非空处理
            return orderService.listOrderGroupByDiscountId();
        } catch (Exception e) {
            log.error("根据会员id分组订单信息：%s", e.getMessage(), e);
        }
        return null;
    }

    @SysLog(MODULE = "订单对外接口管理", REMARK = "根据会员id查询订单信息")
    @ApiOperation("根据会员id查询订单信息")
    @PostMapping("/getOrderByMemberId")
    public EsShopOrder getOrderByMemberId(@RequestParam("memberId") Long memberId) {
        try {
            //非空处理
            return orderService.getOrderBuyerByMemberId(memberId);
        } catch (Exception e) {
            log.error("根据会员id查询订单信息：%s", e.getMessage(), e);
        }
        return null;
    }

    @SysLog(MODULE = "订单对外接口管理", REMARK = "根据会员id查询订单数量")
    @ApiOperation("根据会员id查询订单数量")
    @PostMapping("/sumByGoods")
    Integer sumByGoods(@RequestParam("memberId") Long memberId, @RequestParam("goodsId") Long goodsId,
                       @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime) {
        return orderService.sumByGoods(memberId, goodsId,startTime,endTime);
    }

    @SysLog(MODULE = "订单对外接口管理", REMARK = "根据会员id查询订单数量")
    @ApiOperation("根据会员id查询订单数量")
    @GetMapping("/test")
    Integer test() {
        Integer c=  orderService.sumByGoods(1l,1l,new Date(),new Date());
        System.out.println(c);

        Integer c1=  orderService.sumByGoods(1l,1l,null,null);
        System.out.println(c1);
        return 1;
    }
}
