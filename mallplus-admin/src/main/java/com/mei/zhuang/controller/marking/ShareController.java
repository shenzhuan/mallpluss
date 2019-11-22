package com.mei.zhuang.controller.marking;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsShopShare;
import com.mei.zhuang.entity.marking.EsShopShareMap;
import com.mei.zhuang.service.marking.EsShopShareService;
import com.mei.zhuang.utils.ImgBase64Util;
import com.mei.zhuang.utils.ValidatorUtils;
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
@Api(value = "分享助力", description = "", tags = {"分享助力"})
@RestController
@RequestMapping("/api/Share")
public class ShareController {
    @Resource
    private EsShopShareService service;


    @SysLog(MODULE = "分享助力", REMARK = "根据条件查询分享助力列表")
    @ApiOperation("根据条件查询分享助力列表")
    @PostMapping(value = "/list")
    public Object getSingleGiftByPage(EsShopShare entity,
                                      @RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            PageHelper.startPage(current, size);
            return new CommonResult().success(PageInfo.of(service.ShareList()));
        } catch (Exception e) {
            log.error("根据条件查询分享助力列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "分享助力保存", REMARK = "分享助力保存")
    @ApiOperation("分享助力保存")
    @PostMapping(value = "/save")
    public Object saveSingleGift(EsShopShare entity) {
        try {
            if (!entity.getShareMapinitiate().equals("")) {
                List<EsShopShareMap> list3 = JSONObject.parseArray(entity.getShareMapinitiate(), EsShopShareMap.class);
                entity.setSharemapList(list3);
            } else {
                return new CommonResult().failed("发起者获赠奖品不能为空");
            }
            if (entity.getActivityType() == 1) {
                if (!entity.getSharehelp().equals("")) {
                    List<EsShopShareMap> list4 = JSONObject.parseArray(entity.getSharehelp(), EsShopShareMap.class);
                    entity.setSharemapList2(list4);
                } else {
                    return new CommonResult().failed("助力者获赠奖品不能为空");
                }
            }
            if (service.saveShare(entity) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("分享助力保存：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "分享助力修改", REMARK = "分享助力修改")
    @ApiOperation("分享助力修改")
    @PostMapping(value = "/update")
    public Object update(EsShopShare entity) {
        try {
            if (!entity.getShareMapinitiate().equals("")) {
                List<EsShopShareMap> list3 = JSONObject.parseArray(entity.getShareMapinitiate(), EsShopShareMap.class);
                entity.setSharemapList(list3);
            } else {
                return new CommonResult().failed("发起者获赠奖品不能为空");
            }
            if (entity.getActivityType() == 1) {
                if (!entity.getSharehelp().equals("")) {
                    List<EsShopShareMap> list4 = JSONObject.parseArray(entity.getSharehelp(), EsShopShareMap.class);
                    entity.setSharemapList2(list4);
                } else {
                    return new CommonResult().failed("助力者获赠奖品不能为空");
                }
            }
            if (service.update(entity) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("分享助力修改：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }


    @SysLog(MODULE = "分享助力状态修改", REMARK = "分享助力状态修改")
    @ApiOperation("分享助力状态修改")
    @PostMapping(value = "/updatestatus")
    public Object updatestatus(@RequestParam long id, @RequestParam Integer status) {
        try {
            if (service.updatestatus(id, status) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("分享助力状态修改：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "分享助力删除", REMARK = "分享助力删除")
    @ApiOperation("分享助力删除")
    @PostMapping(value = "/delete")
    public Object delete(@RequestParam long id) {
        try {
            if (service.delete(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("分享助力删除：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "分享助力明细", REMARK = "分享助力明细")
    @ApiOperation("分享助力明细")
    @PostMapping(value = "/detail")
    public Object detail(@RequestParam long id) {
        try {
            return new CommonResult().success(service.sharedetail(id));
        } catch (Exception e) {
            log.error("分享助力明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "状态查询", REMARK = "状态查询")
    @ApiOperation("状态查询")
    @PostMapping(value = "/selectStatus")
    public Object deselectStatustail(@RequestParam Integer status) {
        try {
            if (status == 1) {
                int man = service.status(status);
                System.out.println(man + "数量");
                if (man >= 1) {
                    return new CommonResult().success(3);
                } else {
                    return new CommonResult().success(4);
                }
            } else {
                return new CommonResult().success(4);
            }
        } catch (Exception e) {
            log.error("状态查询：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "分享助力二维码", REMARK = "分享助力二维码")
    @ApiOperation("分享助力二维码")
    @PostMapping(value = "/shareCode")
    public Object shareCode(@RequestParam long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定分享助力编号");
            }
            String img = ImgBase64Util.StringUtil(id, "add/shareHelp/index/index");
            return new CommonResult().success(img);
        } catch (Exception e) {
            log.error("分享助力明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

}
