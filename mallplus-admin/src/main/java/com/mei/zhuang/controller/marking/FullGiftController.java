package com.mei.zhuang.controller.marking;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsShopFullGift;
import com.mei.zhuang.entity.marking.EsShopFullGiftGoodsMap;
import com.mei.zhuang.entity.marking.EsShopFullGiftRule;
import com.mei.zhuang.service.marking.FullGiftService;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 满赠礼管理
 */

@Slf4j
@Api(value = "满赠礼管理", description = "", tags = {"满赠礼管理"})
@RestController
@RequestMapping("/api/fullGift")
public class FullGiftController {


    @Resource
    private FullGiftService fullGiftService;

    @SysLog(MODULE = "满赠礼管理", REMARK = "根据条件查询所有满赠礼列表")
    @ApiOperation("根据条件查询所有满赠礼列表")
    @PostMapping(value = "/list")
    public Object getFullGiftByPage(EsShopFullGift entity,
                                    @RequestParam(value = "current", defaultValue = "1") Integer current,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        try {
            PageHelper.startPage(current, size);
            return new CommonResult().success(PageInfo.of(fullGiftService.slelectPurchase()));
        } catch (Exception e) {
            log.error("根据条件查询所有满赠礼列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }


    @SysLog(MODULE = "满赠礼管理", REMARK = "保存满赠礼")
    @ApiOperation("保存满赠礼")
    @PostMapping(value = "/save")
    public Object saveFullGift(EsShopFullGift entity) {
        try {
            List<GoodsSepcVo> list = JSONObject.parseArray(entity.getSelectgoods(), GoodsSepcVo.class);
            List<EsShopFullGiftRule> list2 = JSONObject.parseArray(entity.getSelectrule(), EsShopFullGiftRule.class);
            List<EsShopFullGiftGoodsMap> list3 = JSONObject.parseArray(entity.getSelectfullgift(), EsShopFullGiftGoodsMap.class);
            List<EsShopFullGift> list4 = JSONObject.parseArray(entity.getSelectfullList(), EsShopFullGift.class);
            List list1=new ArrayList();
            if(list4!=null) {
                for (EsShopFullGift gif : list4) {
                    for (EsShopFullGiftRule rule : gif.getRuleList()) {
                        list1.add(rule.getFullLevel());
                    }
                }
            }
            System.out.println(list1);
            Set set=new HashSet(list1);
            if(list1.size()!=set.size()){
                return new CommonResult().failed("排序出现重复值");
            }
            entity.setGoodsSepcVoList(list);
            entity.setRuleList(list2);
            entity.setFullGiftGoodsList(list3);
            entity.setFullGiftList(list4);

            if (fullGiftService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存满赠礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "满赠礼管理", REMARK = "更新满赠礼")
    @ApiOperation("更新满赠礼")
    @PostMapping(value = "/update")
    public Object updateFullGift(EsShopFullGift entity) {
        try {
            List<GoodsSepcVo> list = JSONObject.parseArray(entity.getSelectgoods(), GoodsSepcVo.class);
            List<EsShopFullGiftRule> list2 = JSONObject.parseArray(entity.getSelectrule(), EsShopFullGiftRule.class);
            List<EsShopFullGiftGoodsMap> list3 = JSONObject.parseArray(entity.getSelectfullgift(), EsShopFullGiftGoodsMap.class);
            List<EsShopFullGift> list4 = JSONObject.parseArray(entity.getSelectfullList(), EsShopFullGift.class);
            List list1=new ArrayList();
            if(list4!=null) {
                for (EsShopFullGift gif : list4) {
                    for (EsShopFullGiftRule rule : gif.getRuleList()) {
                        list1.add(rule.getFullLevel());
                    }
                }
            }
            Set set=new HashSet(list1);
            if(list1.size()!=set.size()){
                return new CommonResult().failed("排序出现重复值");
            }
            entity.setGoodsSepcVoList(list);
            entity.setRuleList(list2);
            entity.setFullGiftGoodsList(list3);
            entity.setFullGiftList(list4);
            if (fullGiftService.update(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新满赠礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "满赠礼管理", REMARK = "删除满赠礼")
    @ApiOperation("删除满赠礼")
    @PostMapping(value = "/delete")
    public Object deleteFullGift(@ApiParam("满赠礼id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("满赠礼id");
            }
            if (fullGiftService.deleteid(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除满赠礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "满赠礼管理", REMARK = "查询满赠礼明细")
    @ApiOperation("查询满赠礼明细")
    @PostMapping(value = "/detail")
    public Object getFullGiftById(@ApiParam("满赠礼id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("满赠礼id");
            }
            EsShopFullGift coupon = fullGiftService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询满赠礼明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "满赠礼管理", REMARK = "查询满赠礼商品明细")
    @ApiOperation("查询满赠礼商品明细")
    @PostMapping(value = "/detailgiftgoods")
    public Object getFullGiftByIdgoods(@ApiParam("满赠礼id") @RequestParam Long fullGiftId) {
        try {
            if (ValidatorUtils.empty(fullGiftId)) {
                return new CommonResult().paramFailed("满赠礼id");
            }
            return new CommonResult().success(fullGiftService.selectgift(fullGiftId));
        } catch (Exception e) {
            log.error("查询满赠礼商品明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "满赠礼管理", REMARK = "查询满赠礼赠品明细与优惠设置")
    @ApiOperation("查询满赠礼赠品明细与优惠设置")
    @PostMapping(value = "/detailgiftgift")
    public Object getFullGiftByIdgift(@ApiParam("满赠礼id") @RequestParam Long fullGiftId) {
        try {
            if (ValidatorUtils.empty(fullGiftId)) {
                return new CommonResult().paramFailed("满赠礼id");
            }
            return new CommonResult().success(fullGiftService.selectgift2(fullGiftId));
        } catch (Exception e) {
            log.error("查询满赠礼赠品明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "满赠礼管理", REMARK = "修改状态")
    @ApiOperation("修改状态 status 为列表的状态")
    @PostMapping(value = "/updateStatus")
    public Object updateShowStatus(@RequestParam("id") Long id,
                                   @RequestParam("status") Integer status) {
        int count = fullGiftService.updateShowStatus(id, status);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "选赠礼管理", REMARK = "修改状态查询")
    @ApiOperation("修改状态查询")
    @PostMapping(value = "/Status")
    public Object Status(@RequestParam Integer status) {
        if (status == 0) {
            int man = fullGiftService.selectstatus();
            if (man >= 1) {
                return new CommonResult().success(3);
            } else {
                return new CommonResult().success(4);
            }
        } else {
            return new CommonResult().success(4);
        }
    }

    @SysLog(MODULE = "满赠礼管理", REMARK = "修改状态查询")
    @ApiOperation("修改状态查询")
    @PostMapping(value = "/Status2")
    public Object Status2(@RequestParam Integer status) {
        if (status == 0) {
            int man = fullGiftService.selectstatus2();
            if (man >= 1) {
                return new CommonResult().success(3);
            } else {
                return new CommonResult().success(4);
            }
        } else {
            return new CommonResult().success(4);
        }
    }

}
