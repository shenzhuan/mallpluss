package com.mei.zhuang.controller.marking;


import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsShopCouponNew;
import com.mei.zhuang.entity.marking.EsShopCouponNewRule;
import com.mei.zhuang.service.marking.CouponNewService;
import com.mei.zhuang.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(value = "新人发券管理", description = "", tags = {"新人发券管理"})
@RestController
@RequestMapping("/api/couponnew")
public class CouponNewController {

    @Resource
    private CouponNewService couponService;

    @SysLog(MODULE = "新人发券管理", REMARK = "添加新人发券")
    @ApiOperation("添加新人发券")
    @PostMapping(value = "/save")
    public Object saveCouponNew(EsShopCouponNew entity) {
        try {
            List<EsShopCouponNewRule> list = JSONObject.parseArray(entity.getSelectrule(), EsShopCouponNewRule.class);
            if (entity.getStatus() == 1) {
                if (list == null && list.size() < 0) {
                    return new CommonResult().failed("优惠券不能为空");
                }
                entity.setRulesList(list);
                if (entity.getSingleCoupon() == null) {
                    return new CommonResult().failed("推券数量不能为空");
                }
            }
            if (entity.getRulesList() != null && entity.getRulesList().size() > 0) {
                for (EsShopCouponNewRule newrule : entity.getRulesList()) {
                    if (entity.getSingleCoupon() > newrule.getInventory()) {
                        return new CommonResult().failed("单次推券不可大于优惠库存");
                    }
                }
            }
            return new CommonResult().success(couponService.save(entity));
        } catch (Exception e) {
            log.error("添加新人发券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "新人发券管理", REMARK = "新人发券查询")
    @ApiOperation("新人发券查询")
    @PostMapping(value = "/listCouponnew")
    public Object listCouponnew() {
        try {
            return new CommonResult().success(couponService.listcouponnew());
        } catch (Exception e) {
            log.error("新人发券查询：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "新人发券管理", REMARK = "通用删除优惠券id")
    @ApiOperation("通用删除优惠券id")
    @PostMapping(value = "/delete")
    public Object saveCoupon(@RequestParam Long couponid, @RequestParam String typeid) {
        try {
            if (couponService.deletetypeid(couponid, typeid) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除优惠券id：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "新人发券管理", REMARK = "修改新人发券")
    @ApiOperation("修改新人发券")
    @PostMapping(value = "/update")
    public Object updateCouponNew(EsShopCouponNew entity) {
        try {
            List<EsShopCouponNewRule> list = JSONObject.parseArray(entity.getSelectrule(), EsShopCouponNewRule.class);
            if (entity.getStatus() == 1) {
                if (list == null && list.size() < 0) {
                    return new CommonResult().failed("优惠券不能为空");
                }
                entity.setRulesList(list);
                if (entity.getSingleCoupon() == null) {
                    return new CommonResult().failed("推券数量不能为空");
                }
            }
            if (entity.getRulesList() != null && entity.getRulesList().size() > 0) {
                for (EsShopCouponNewRule newrule : entity.getRulesList()) {
                    if (entity.getSingleCoupon() > newrule.getInventory()) {
                        return new CommonResult().failed("单次推券不可大于优惠库存");
                    }
                }
            }
            return new CommonResult().success(couponService.update(entity));
        } catch (Exception e) {
            log.error("修改新人发券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }


}
