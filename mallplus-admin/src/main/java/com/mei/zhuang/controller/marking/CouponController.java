package com.mei.zhuang.controller.marking;


import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import com.arvato.service.marking.api.orm.dao.EsShopCouponNewRuleMapper;
import com.arvato.service.marking.api.service.CouponService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.mei.zhuang.entity.marking.EsShopCoupon;
import com.mei.zhuang.entity.marking.EsShopCouponNewRule;
import com.mei.zhuang.entity.marking.EsShopCouponRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券管理
 */

@Slf4j
@Api(value = "优惠券管理", description = "", tags = {"优惠券管理"})
@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Resource
    private CouponService couponService;
    @Resource
    private EsShopCouponNewRuleMapper couponNewRuleMapper;

    @SysLog(MODULE = "优惠券管理", REMARK = "根据条件查询所有优惠券列表")
    @ApiOperation("根据条件查询所有优惠券列表")
    @PostMapping(value = "/list")
    public Object getCouponByPage(EsShopCoupon entity, @RequestParam(value = "current", defaultValue = "1") Integer current,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            PageHelper.startPage(current, size);
            Map<String, Object> map = couponService.selectmapcoupon(entity);
            entity.setTotal((int) PageHelper.freeTotal());
            map.put("current", current);
            map.put("size", size);
            map.put("total", entity.getTotal());
            return new CommonResult().success(map);


        } catch (Exception e) {
            log.error("根据条件查询所有优惠券列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "优惠券管理", REMARK = "保存优惠券")
    @ApiOperation("保存优惠券")
    @PostMapping(value = "/save")

    public Object saveCoupon( EsShopCoupon entity,EsShopCouponRule rule) {
        try {
            if(entity.getType()==3) {
                System.out.println(entity.getGoodsList());
                if (entity.getGoodsList().equals("")) {
                    return new CommonResult().failed("指定商品不可为空");
                }
            }
            if(entity.getType()==4){
                if (entity.getGoodsList().equals("")) {
                    return new CommonResult().failed("指定赠品不可为空");
                }
            }
            List<GoodsSepcVo> list2 = JSONObject.parseArray(entity.getGoodsList(), GoodsSepcVo.class);
            entity.setGoodsSepcVoLists(list2);

            if(rule.getGoodsLimitedId()==2||rule.getGoodsLimitedId()==3){
                if(rule.getExtra()==1&&rule.getClassificationId()==null){
                    return new CommonResult().failed("商品分类不能为空");
                }
                if(rule.getExtra()==2&&rule.getRulegoodslist().equals("")){
                    return new CommonResult().failed("指定商品不能为空");
                }
            }
            List<GoodsSepcVo> list = JSONObject.parseArray(rule.getRulegoodslist(), GoodsSepcVo.class);

            rule.setGoodsSepcVoList(list);

            if (couponService.save(entity, rule) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存优惠券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "优惠券管理", REMARK = "更新优惠券")
    @ApiOperation("更新优惠券")
    @PostMapping(value = "/update")
    public Object updateCoupon(EsShopCoupon entity, EsShopCouponRule rule) {
        try {
            if(entity.getType()==3) {
                System.out.println(entity.getGoodsList());
                if (entity.getGoodsList().equals("")) {
                    return new CommonResult().failed("指定商品不可为空");
                }
            }
            if(entity.getType()==4){
                if (entity.getGoodsList().equals("")) {
                    return new CommonResult().failed("指定赠品不可为空");
                }
            }
            List<GoodsSepcVo> list2 = JSONObject.parseArray(entity.getGoodsList(), GoodsSepcVo.class);
            entity.setGoodsSepcVoLists(list2);

            if(rule.getGoodsLimitedId()==2||rule.getGoodsLimitedId()==3){
                if(rule.getExtra()==1&&rule.getClassificationId()==null){
                    return new CommonResult().failed("商品分类不能为空");
                }
                if(rule.getExtra()==2&&rule.getRulegoodslist().equals("")){
                    return new CommonResult().failed("指定商品不能为空");
                }
            }
            List<GoodsSepcVo> list = JSONObject.parseArray(rule.getRulegoodslist(), GoodsSepcVo.class);

            rule.setGoodsSepcVoList(list);
            if (couponService.update(entity, rule)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新优惠券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "优惠券管理", REMARK = "删除优惠券")
    @ApiOperation("删除优惠券")
    @PostMapping(value = "/delete")
    public Object deleteCoupon(@ApiParam("优惠券id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("优惠券id");
            }
            EsShopCoupon esCoupon = couponService.selectById(id);
            if(esCoupon.getStatusOpen().equals("1")){
                return new CommonResult().failed("启用不可删除");
            }else{
                if (couponService.detetecouponid(id) > 0) {
                    return new CommonResult().success();
                }
            }

        } catch (Exception e) {
            log.error("删除优惠券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "优惠券管理", REMARK = "查询是否发放优惠券")
    @ApiOperation("查询是否发放优惠券")
    @PostMapping(value = "/selectcoupon")
    public Object seleccoupon(@ApiParam("优惠券id") @RequestParam Long id,@RequestParam String name) {
        try {

            List<EsShopCouponNewRule> couponlist = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("coupon_name", name).eq("couponid",id));
            //已发送的删除状态
            if(couponlist!=null&&couponlist.size()>0) {
                    return new CommonResult().success(3);
            }else{
                    return new CommonResult().success(4);
            }
        } catch (Exception e) {
            log.error("查询是否发放优惠券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "优惠券管理", REMARK = "查询优惠券明细")
    @ApiOperation("查询优惠券明细")
    @PostMapping(value = "/detail")
    public Object getCouponById(@ApiParam("优惠券id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("优惠券id");
            }
            EsShopCoupon esCoupon = couponService.selectById(id);
            if(esCoupon.getStatusOpen()=="1"){
                return new CommonResult().failed("活动开启不可编辑");
            }else {
                List<Map<String, Object>> couponlimit = couponService.couponlimit(id);
                Map<String, Object> map = new HashMap<>();
                map.put("rows", couponlimit);
                return new CommonResult().success(map);
            }
        } catch (Exception e) {
            log.error("查询优惠券明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "优惠券管理", REMARK = "查询优惠券限制商品明细")
    @ApiOperation("查询优惠券限制商品明细")
    @PostMapping(value = "/coupongoods")
    public Object coupongoods(@ApiParam("优惠券id") @RequestParam Long couponId) {
        try {
            if (ValidatorUtils.empty(couponId)) {
                return new CommonResult().paramFailed("优惠券id");
            }
            return new CommonResult().success(couponService.selectgoods(couponId));
        } catch (Exception e) {
            log.error("查询优惠券商品明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "优惠券管理", REMARK = "查询优惠券实物商品明细")
    @ApiOperation("查询优惠券实物商品明细")
    @PostMapping(value = "/physicalgoods")
    public Object physicalgoods(@RequestParam Long couponId) {
        try {
            return new CommonResult().success(couponService.selectgoods2(couponId));
        } catch (Exception e) {
            log.error("查询优惠券实物商品明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "优惠券管理", REMARK = "查询优惠券赠品商品明细")
    @ApiOperation("查询优惠券赠品商品明细")
    @PostMapping(value = "/selectcoupongoods")
    public Object selectcoupon(@RequestParam Long couponId) {
        try {
            return new CommonResult().success(couponService.selcetcoupongoods(couponId));
        } catch (Exception e) {
            log.error("查询优惠券赠品商品明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "优惠券管理", REMARK = "删除添加的优惠指定商品")
    @ApiOperation("删除添加的优惠指定商品")
    @PostMapping(value = "/deletegoodsid")
    public Object deletegoodsid(@ApiParam("商品id") @RequestParam Long goodsid) {
        try {
            if (ValidatorUtils.empty(goodsid)) {
                return new CommonResult().paramFailed("商品id");
            }
            return new CommonResult().success(couponService.deleteid(goodsid));

        } catch (Exception e) {
            log.error("删除满减：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "优惠券管理", REMARK = "优惠券选择查询")
    @ApiOperation("优惠券选择查询")
    @PostMapping(value = "/couponselection")
    public Object couponselection(EsShopCoupon entity, @RequestParam(value = "current", defaultValue = "1") Integer current,
                                  @RequestParam(value = "size", defaultValue = "5") Integer size) {
        try {
            PageHelper.startPage(current, size);
            List<EsShopCoupon> selectcoupon = couponService.selectcoupon(entity);
            entity.setTotal((int) PageHelper.freeTotal());
            Map<String, Object> map = new HashMap();
            map.put("rows", selectcoupon);
            map.put("total", entity.getTotal());
            map.put("size", size);
            map.put("current", current);
            return new CommonResult().success(map);
        } catch (Exception e) {
            log.error("优惠券选择查询：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "优惠券管理", REMARK = "状态修改")
    @ApiOperation("状态修改")
    @PostMapping(value = "/updatestatus")
    public Object updatestatus(@ApiParam("优惠券id") @RequestParam Long id, @RequestParam String statusOpen) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("优惠券id");
            }
            //EsShopCoupon esShopCoupon = couponService.selectById(id);
         /*   if (esShopCoupon.getStatus() == 1) {
                return new CommonResult().failed("属于未生效状态,不可直接更改状态，请到编辑修改活动时间");
            } else if (esShopCoupon.getStatus() == 3) {
                return new CommonResult().failed("属于失效状态,不可直接更改状态，请到编辑修活动时间");
            } else {*/
         return new CommonResult().success(couponService.updatestatus(id,statusOpen));


        } catch (Exception e) {
            log.error("状态修改：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation("批量查询优惠券信息")
    @PostMapping(value = "/couponbacth")
    public Object couponbacth(@ApiParam("优惠券id") @RequestParam String id) {
        try {
            return new CommonResult().success(couponService.couponbatch(id));

        } catch (Exception e) {
            log.error("状态修改：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }
}
