package com.mei.zhuang.controller.order;

import com.arvato.common.redis.template.RedisRepository;
import com.arvato.service.order.api.constant.OrderConstant;
import com.arvato.service.order.api.service.ShopOrderSettingsService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.mei.zhuang.entity.order.EsShopOrderSettings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-05-18 9:46
 * @Description:
 */
@Api(value = "订单设置管理", description = "", tags = {"订单设置管理"})
@Slf4j
@RestController
@RequestMapping("/api/order/settings")
public class OrderSettingsController {

    @Resource
    private ShopOrderSettingsService shopOrderSettingsService;

    @Resource
    private RedisRepository redisRepository;

    @SysLog(MODULE = "订单设置管理", REMARK = "查询订单设置")
    @ApiOperation("查询订单设置")
    @PostMapping("/list")
    public Object list() {
        try {
            EsShopOrderSettings settings = new EsShopOrderSettings();
            settings.setShopId(1l);
//            settings.setShopId(shopId);
            List<EsShopOrderSettings> list = shopOrderSettingsService.selectList(new QueryWrapper<>(settings));
            return new CommonResult().success(list.get(0));
        } catch (Exception e) {
            log.error("查询订单设置：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单设置管理", REMARK = "保存|修改 订单设置")
    @ApiOperation("保存|修改 订单设置")
    @PostMapping("/save")
    public Object savePaySettings(EsShopOrderSettings entity) {
        try {
            Integer autoCloseTime = entity.getAutoCloseTime();
            Integer autoFinshDay = entity.getAutoFinshDay();
//            订单下单未付款，在规定时间后自动关闭，0或者空为不自动关闭
//            订单发货后，用户收货的天数，如果在期间未确认收货，系统自动完成收货，0或空为不自动收货

            if (autoCloseTime != null && autoCloseTime != 0) {
                if (autoCloseTime < OrderConstant.AUTOC_LOSE_TIME_MIN || autoCloseTime > OrderConstant.AUTOC_LOSE_TIME_MAX) {
                    return new CommonResult().paramFailed("自动关闭时间只能在-->" + OrderConstant.AUTOC_LOSE_TIME_MIN + "-" + OrderConstant.AUTOC_LOSE_TIME_MAX + "范围内");
                }
            }
            if (autoFinshDay != null && autoFinshDay != 0) {
                if (autoFinshDay < OrderConstant.AUTOFI_NSH_DAY_MIN || autoFinshDay > OrderConstant.AUTOFI_NSH_DAY_MAX) {
                    return new CommonResult().paramFailed("自动确认收货时间只能在-->" + OrderConstant.AUTOFI_NSH_DAY_MIN + "-" + OrderConstant.AUTOFI_NSH_DAY_MAX + "范围内");
                }
            }

//            数据库只能留一个
            Long id = entity.getId();
            if (id != null) {
                this.shopOrderSettingsService.updateById(entity);
                return new CommonResult().success();
            }
            if (id == null || id == 0l) {
                //非空
                entity.setShopId(1l);
                entity.setId(IdWorker.getId());
                entity.setCreateTime(new Date());
                entity.setCreateById(1l);//待完善
                entity.setUpdateTime(new Date());
                entity.setUpdateById(1l);//待完善
                entity.setIsDelete(0);
                redisRepository.set(entity.getShopId() + "", entity);
                if (shopOrderSettingsService.insert(entity)) {
                    return new CommonResult().success();
                }
            }
        } catch (Exception e) {
            log.error("保存|修改 订单设置：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "订单设置管理", REMARK = "删除订单设置")
    @ApiOperation("删除订单设置")
    @PostMapping("/delete")
    public Object deletePayment(@ApiParam("订单设置id") @RequestParam Long id) {
        try {
            //非空处理
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("参数订单设置id不能为空");
            }
            if (shopOrderSettingsService.deleteById(id)) {
                return new CommonResult().success("成功删除id为：" + id);
            }
        } catch (Exception e) {
            log.error("删除订单设置：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }


}
