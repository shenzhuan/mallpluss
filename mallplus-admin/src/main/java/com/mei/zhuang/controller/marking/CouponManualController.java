package com.mei.zhuang.controller.marking;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsShopCouponManual;
import com.mei.zhuang.entity.marking.EsShopCouponNewRule;
import com.mei.zhuang.service.marking.CouponManualService;
import com.mei.zhuang.service.marking.impl.CouponManualServiceImpl;
import com.mei.zhuang.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Api(value = "手工发券管理", description = "", tags = {"手工发券管理"})
@RestController
@RequestMapping("/api/manual")
public class CouponManualController {

    @Resource
    private CouponManualService manualService;
    @Resource
    private CouponTime couponTime;
    @Resource
    private CouponManualServiceImpl service;

    @SysLog(MODULE = "手工发券管理", REMARK = "根据条件查询所有手工券列表")
    @ApiOperation("根据条件查询所有手工券列表")
    @PostMapping(value = "/list")
    public Object getCouponByPage( EsShopCouponManual entity,
                                  @RequestParam(value = "current", defaultValue = "1") Integer current,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        try {
            PageHelper.startPage(current, size);
            return new CommonResult().success(PageInfo.of(manualService.list(new QueryWrapper<>(entity))));
        } catch (Exception e) {
            log.error("根据条件查询所有手工券列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "手工发券管理", REMARK = "保存手工发券")
    @ApiOperation("保存手工发券")
    @PostMapping(value = "/save")
    public Object saveCoupon( EsShopCouponManual entity) {
        try {
            TestCSVUtil csvUtil = new TestCSVUtil();
            List<EsShopCouponNewRule> list = JSONObject.parseArray(entity.getRuleList(), EsShopCouponNewRule.class);
            entity.setNewRuleList(list);
            if(entity.getStampsNumber()==0){
                return new CommonResult().failed("请输入大于0的整数");
            }
            List<String> source = csvUtil.readcsvFile(entity.getPath());
            Set set = new HashSet(source);
            for (int i = 0; i < source.size(); i++) {
                if (set.size() < source.size()) {
                    return new CommonResult().failed("上传的openid有重复，请重新上传");
                }
            }
            return new CommonResult().success(manualService.save(entity));
        } catch (Exception e) {
            log.error("保存手工发券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
    @SysLog(MODULE = "手工发券管理", REMARK = "修改手工发券")
    @ApiOperation("修改手工发券")
    @PostMapping(value = "/update")
    public Object updateCoupon( EsShopCouponManual entity) {
        try {
            List<EsShopCouponNewRule> list = JSONObject.parseArray(entity.getRuleList(), EsShopCouponNewRule.class);
            entity.setNewRuleList(list);
            if (manualService.update(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("修改手工发券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "手工发券管理", REMARK = "删除手工发券")
    @ApiOperation("删除手工发券")
    @PostMapping(value = "/deleteManual")
    public Object deleteManual(@ApiParam("优惠券类型id") @RequestParam long id) {
        try {

            if (manualService.deleteManual(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除手工发券：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "手工发券管理", REMARK = "手工发券状态修改")
    @ApiOperation("手工发券状态修改")
    @PostMapping(value = "/statusManual")
    public Object deleteManual(@ApiParam("手工发券id") @RequestParam long id, @RequestParam Integer statusissue) {
        try {
            EsShopCouponManual manual = manualService.getById(id);
            //活动开启 1，立即开启，2指定时间，3关闭
            if(manual.getActivityOpen()==2) {
                if (statusissue == 2) {
                    couponTime.setCoupon(1);
                    couponTime.schedulcouponTime();
                }else{
                    couponTime.setCoupon(0);
                    couponTime.schedulcouponTime();
                }
                //发券对象 1,全部用户2，指定用户
            }else if(manual.getActivityOpen()==1&&manual.getSendObject()==1){
                if (statusissue == 2) {
                    System.out.println("停止活动");
                    service.SetCou(1);
                    service.makeRealData(manual);
                }else{
                    service.SetCou(0);
                    service.makeRealData(manual);
                }
            }else if(manual.getActivityOpen()==1&&manual.getSendObject()==2){
                if (statusissue == 2) {
                    System.out.println("停止活动");
                    service.SetCou(1);
                    service.specifiedCoupon(manual);
                }else{
                    service.SetCou(0);
                    service.specifiedCoupon(manual);
                }
            }
            return new CommonResult().success(manualService.updatestatus(id,statusissue));
        } catch (Exception e) {
            log.error("手工发券状态修改：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
    @SysLog(MODULE = "手工发券管理", REMARK = "手工发券明细")
    @ApiOperation("手工发券明细")
    @PostMapping(value = "/selectCoupon")
    public Object selectCoupon(@ApiParam("明细d") @RequestParam Long id) {
        try {

            return new CommonResult().success(manualService.ManualList(id));
        } catch (Exception e) {
            log.error("手工发券明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
}
