package com.mei.zhuang.controller.marking;


import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import com.arvato.service.marking.api.service.FirstPurchaseService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.mei.zhuang.entity.marking.EsShopFirstPurchase;
import com.mei.zhuang.entity.marking.EsShopFirstPurchaseRule;
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
 * 首购礼管理
 */

@Slf4j
@Api(value = "首购礼管理", description = "", tags = {"首购礼管理"})
@RestController
@RequestMapping("/api/firstPurchase")
public class FirstPurchaseController {


    @Resource
    private FirstPurchaseService firstPurchaseService;


    @SysLog(MODULE = "首购礼管理", REMARK = "根据条件查询所有首购礼列表")
    @ApiOperation("根据条件查询所有首购礼列表")
    @PostMapping(value = "/list")
    public Object getDiscountByPage(EsShopFirstPurchase entity,
                                    @RequestParam(value = "current", defaultValue = "1") Integer current,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        try {
            PageHelper.startPage(current, size);
            List<EsShopFirstPurchase> esShopDiscount = firstPurchaseService.slelectPurchase();
            entity.setTotal((int) PageHelper.freeTotal());
            Map<String, Object> map = new HashMap<>();
            map.put("rows", esShopDiscount);
            map.put("size", size);
            map.put("total", entity.getTotal());
            map.put("current", current);
            return new CommonResult().success(map);
        } catch (Exception e) {
            log.error("根据条件查询所有首购礼列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "首购礼管理", REMARK = "保存首购礼")
    @ApiOperation("保存首购礼")
    @PostMapping(value = "/save")
    public Object saveDiscount(EsShopFirstPurchase entity) {
        try {
            List<GoodsSepcVo> list = JSONObject.parseArray(entity.getSelectgoods(), GoodsSepcVo.class);
            entity.setGoodsSepcVoList(list);
            List<EsShopFirstPurchaseRule> rulelist = JSONObject.parseArray(entity.getPurchaseRuleList(), EsShopFirstPurchaseRule.class);
            entity.setFirstPurchaseRuleList(rulelist);
            if (firstPurchaseService.save(entity) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存首购礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "首购礼管理", REMARK = "更新首购礼")
    @ApiOperation("更新首购礼")
    @PostMapping(value = "/update")
    public Object updateDiscount(EsShopFirstPurchase entity) {
        try {
            List<GoodsSepcVo> list = JSONObject.parseArray(entity.getSelectgoods(), GoodsSepcVo.class);
            entity.setGoodsSepcVoList(list);
            List<EsShopFirstPurchaseRule> rulelist = JSONObject.parseArray(entity.getPurchaseRuleList(), EsShopFirstPurchaseRule.class);
            entity.setFirstPurchaseRuleList(rulelist);
            if (firstPurchaseService.update(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新首购礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "首购礼管理", REMARK = "删除首购礼")
    @ApiOperation("删除首购礼")
    @PostMapping(value = "/delete")
    public Object deleteDiscount(@ApiParam("首购礼id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("首购礼id");
            }
            if (firstPurchaseService.deleteid(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除首购礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "首购礼管理", REMARK = "查询首购礼明细")
    @ApiOperation("查询首购礼明细")
    @PostMapping(value = "/detail")
    public Object getDiscountById(@ApiParam("首购礼id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("首购礼id");
            }
            EsShopFirstPurchase coupon = firstPurchaseService.selectById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询首购礼明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "首购礼管理", REMARK = "修改状态")
    @ApiOperation("修改状态 status 为列表的状态")
    @PostMapping(value = "/updateStatus")
    public Object updateShowStatus(@RequestParam("id") Long id,
                                   @RequestParam("status") Integer status) {
        int count = firstPurchaseService.updateShowStatus(id, status);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "首购礼管理", REMARK = "查询首购商品明细")
    @ApiOperation("查询首购商品明细")
    @PostMapping(value = "/detailgoods")
    public Object getManJiangoodsById(@ApiParam("满减id") @RequestParam Long firstPurchaseId) {
        try {
            if (ValidatorUtils.empty(firstPurchaseId)) {
                return new CommonResult().paramFailed("满减商品id");
            }
            return new CommonResult().success(firstPurchaseService.selectgoodsid(firstPurchaseId));
        } catch (Exception e) {
            log.error("查询首购明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "首购礼管理", REMARK = "查询首购赠品明细")
    @ApiOperation("查询首购赠品明细")
    @PostMapping(value = "/detailcouponid")
    public Object getManJiancouponById(@ApiParam("满减id") @RequestParam Long firstPurchaseId) {
        try {
            if (ValidatorUtils.empty(firstPurchaseId)) {
                return new CommonResult().paramFailed("查询首购赠品明细");
            }
            return new CommonResult().success(firstPurchaseService.selectcouponid(firstPurchaseId));

        } catch (Exception e) {
            log.error("查询首购赠品明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "首购礼管理", REMARK = "删除编辑的商品")
    @ApiOperation("删除编辑的商品")
    @PostMapping(value = "/deletegoodsid")
    public Object deletegoodsid(@ApiParam("商品id") @RequestParam Long goodsId) {
        try {
            if (ValidatorUtils.empty(goodsId)) {
                return new CommonResult().paramFailed("商品id");
            }
            return new CommonResult().success(firstPurchaseService.deletegoodsid(goodsId));

        } catch (Exception e) {
            log.error("删除首购礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "首购礼管理", REMARK = "删除编辑的赠品")
    @ApiOperation("删除编辑的赠品")
    @PostMapping(value = "/deletegiftsid")
    public Object deletegiftid(@ApiParam("商品id") @RequestParam Long giftsId) {
        try {
            if (ValidatorUtils.empty(giftsId)) {
                return new CommonResult().paramFailed("商品id");
            }
            return new CommonResult().success(firstPurchaseService.deletegiftid(giftsId));

        } catch (Exception e) {
            log.error("删除首购礼赠品：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    // -------------------------------------------- Ivan ---------------------------------------------------
//    @ApiOperation("编辑时加载首购礼信息")
//    @PostMapping(value = "/detail2")
//    public Object detail(@ApiParam("首购礼id") @RequestParam Long id) {
//        try {
//            if (ValidatorUtils.empty(id)) {
//                return new CommonResult().paramFailed("ID IS NOT!");
//            }
//
//            // 1.基本信息
//            EsShopFirstPurchase firstPurchase = firstPurchaseService.selectById(id);
//            // 2.指定商品信息
//            List<EsShopFirstPurchaseGoodsMap> firstPurchaseGoodsMapList = firstPurchaseService.selectgoodsid(id);
//            List<Long> collect = firstPurchaseGoodsMapList.stream().map(EsShopFirstPurchaseGoodsMap::getGoodsId).collect(Collectors.toList());
//            String ids = "";
//            for (int i = 0; i < collect.size(); i++) {
//                ids += collect.get(i) + ",";
//                System.out.println("ids"+ ids);
//            }
//            ids = ids.substring(0, ids.length() - 1);
//            // String ids = Arrays.toString(collect.toArray());
//            log.info("ids", ids);
//            Map ddetailObj = (Map) goodsServiceFegin.selGoodsIds(ids);
//
//            List<EsShopGoods> shopGoodsList = (List<EsShopGoods>) ddetailObj.get("data");
//            log.info("data", JSONObject.toJSON(shopGoodsList));
//            System.out.println("data"+ shopGoodsList);
//            //
//            List<EsShopGoods> list = new ArrayList<>();
//
//            shopGoodsList.forEach(shopGoods -> {
//                String goodsId = shopGoods.getId() + "";
//                // goodsId.substring(0, goodsId.indexOf("."));
//                // 查询规格
//                List<EsShopGoodsOption> shopGoodsOptionList = (List<EsShopGoodsOption>) goodsServiceFegin.selGoodsSKU(Long.valueOf(goodsId.substring(0, goodsId.indexOf("."))));
//                shopGoods.setOptions(shopGoodsOptionList);
//                list.add(shopGoods);
//            });
//
//            Map<String, Object> resultObj =new HashMap<>();
//            resultObj.put("firstPurchase", firstPurchase);
//            resultObj.put("firstPurchaseGoodsMapList", list);
//
//            return new CommonResult().success(resultObj);
//        } catch (Exception e) {
//            log.error("查询首购礼明细：%s", e.getMessage(), e);
//            return new CommonResult().failed();
//        }
//    }

}
