package com.mei.zhuang.controller.marking;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsShopSingleGift;
import com.mei.zhuang.entity.marking.EsShopSingleGiftGoodsMap;
import com.mei.zhuang.entity.marking.EsShopSingleGiftRule;
import com.mei.zhuang.service.marking.SingleGiftService;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 单品赠礼管理
 */

@Slf4j
@Api(value = "单品赠礼管理", description = "", tags = {"单品赠礼管理"})
@RestController
@RequestMapping("/api/singleGift")
public class SingleGiftController {


    @Resource
    private SingleGiftService singleGiftService;

    @SysLog(MODULE = "单品赠礼管理", REMARK = "根据条件查询所有单品赠礼列表")
    @ApiOperation("根据条件查询所有单品赠礼列表")
    @PostMapping(value = "/list")
    public Object getSingleGiftByPage(EsShopSingleGift entity,
                                      @RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        try {
            PageHelper.startPage(current, size);
           // List<EsShopSingleGift> esShopDiscount = singleGiftService.slelectPurchase();
            return new CommonResult().success(PageInfo.of(singleGiftService.slelectPurchase()));
        } catch (Exception e) {
            log.error("根据条件查询所有单品赠礼列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "单品赠礼管理", REMARK = "保存单品赠礼")
    @ApiOperation("保存单品赠礼")
    @PostMapping(value = "/save")
    public Object saveSingleGift(EsShopSingleGift entity) {
        try {
            List<GoodsSepcVo> list1 = JSONObject.parseArray(entity.getSepcVoList1(), GoodsSepcVo.class);
            List<GoodsSepcVo> list2 = JSONObject.parseArray(entity.getSepcVoList2(), GoodsSepcVo.class);
            List<EsShopSingleGiftRule> list3 = JSONObject.parseArray(entity.getSelectrule(), EsShopSingleGiftRule.class);
            List<EsShopSingleGiftGoodsMap> list4 = JSONObject.parseArray(entity.getGiftList(), EsShopSingleGiftGoodsMap.class);

            entity.setGoodsSepcVoList1(list1);
            entity.setGoodsSepcVoList2(list2);
            entity.setRuleList(list3);
            entity.setGiftGoodsList(list4);
            if (singleGiftService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存单品赠礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "单品赠礼管理", REMARK = "更新单品赠礼")
    @ApiOperation("更新单品赠礼")
    @PostMapping(value = "/update")
    public Object updateSingleGift(EsShopSingleGift entity) {
        try {
            List<GoodsSepcVo> list1 = JSONObject.parseArray(entity.getSepcVoList1(), GoodsSepcVo.class);
            List<GoodsSepcVo> list2 = JSONObject.parseArray(entity.getSepcVoList2(), GoodsSepcVo.class);
            List<EsShopSingleGiftRule> list3 = JSONObject.parseArray(entity.getSelectrule(), EsShopSingleGiftRule.class);
            List<EsShopSingleGiftGoodsMap> list4 = JSONObject.parseArray(entity.getGiftList(), EsShopSingleGiftGoodsMap.class);

            entity.setGoodsSepcVoList1(list1);
            entity.setGoodsSepcVoList2(list2);
            entity.setRuleList(list3);
            entity.setGiftGoodsList(list4);
            if (singleGiftService.update(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新单品赠礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "单品赠礼管理", REMARK = "删除单品赠礼")
    @ApiOperation("删除单品赠礼")
    @PostMapping(value = "/delete")
    public Object deleteSingleGift(@ApiParam("单品赠礼id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("单品赠礼id");
            }
            if (singleGiftService.deleteid(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除单品赠礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "单品赠礼管理", REMARK = "查询单品赠礼明细")
    @ApiOperation("查询单品赠礼明细")
    @PostMapping(value = "/detail")
    public Object getSingleGiftById(@ApiParam("单品赠礼id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("单品赠礼id");
            }
            return new CommonResult().success(singleGiftService.getById(id));
        } catch (Exception e) {
            log.error("查询单品赠礼明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "单品赠礼管理", REMARK = "查询单品规格明细")
    @ApiOperation("查询单品规格明细")
    @PostMapping(value = "/detailsing")
    public Object SingleGiftById(@ApiParam("单品赠礼id") @RequestParam Long singleGiftId) {
        try {
            if (ValidatorUtils.empty(singleGiftId)) {
                return new CommonResult().paramFailed("单品赠礼id");
            }
            return new CommonResult().success(singleGiftService.selectsing(singleGiftId));
        } catch (Exception e) {
            log.error("查询单品规格明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "单品赠礼管理", REMARK = "查询单品和多组商品明细")
    @ApiOperation("查询单品和多组商品明细")
    @PostMapping(value = "/detailsinggoods")
    public Object SingleGiftByIdgoods(@ApiParam("单品赠礼id") @RequestParam Long singleGiftId) {
        try {
            if (ValidatorUtils.empty(singleGiftId)) {
                return new CommonResult().paramFailed("单品赠礼id");
            }
            return new CommonResult().success(singleGiftService.selectsinggoods(singleGiftId));
        } catch (Exception e) {
            log.error("查询单品规格明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "单品赠礼管理", REMARK = "查询赠品明细")
    @ApiOperation("查询赠品明细")
    @PostMapping(value = "/detailsinggoods3")
    public Object SingleGiftByIdgoods3(@ApiParam("单品赠礼id") @RequestParam Long singleGiftId) {
        try {
            if (ValidatorUtils.empty(singleGiftId)) {
                return new CommonResult().paramFailed("单品赠礼id");
            }
            return new CommonResult().success(singleGiftService.selectsinggoods3(singleGiftId));
        } catch (Exception e) {
            log.error("查询单品规格明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "单品赠礼管理", REMARK = "修改状态")
    @ApiOperation("修改状态 status 为列表的状态")
    @PostMapping(value = "/updateStatus")
    public Object updateShowStatus(@RequestParam("id") Long id,
                                   @RequestParam("status") Integer status) {
        int count = singleGiftService.updateShowStatus(id, status);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

}
