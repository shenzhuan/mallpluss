package com.mei.zhuang.controller.marking;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.ExcelExportUtil;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsShopCodeGift;
import com.mei.zhuang.entity.marking.EsShopCodeGiftGoodsMap;
import com.mei.zhuang.entity.marking.EsShopCodeGiftRule;
import com.mei.zhuang.service.marking.CodeGiftService;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 验证码赠礼管理
 */

@Slf4j
@Api(value = "验证码赠礼管理", description = "", tags = {"验证码赠礼管理"})
@RestController
@RequestMapping("/api/codeGift")
public class CodeGiftController {


    @Resource
    private CodeGiftService codeGiftService;

    @SysLog(MODULE = "验证码赠礼管理", REMARK = "根据条件查询所有验证码赠礼列表")
    @ApiOperation("根据条件查询所有验证码赠礼列表")
    @PostMapping(value = "/list")
    public Object getCodeGiftByPage(EsShopCodeGift entity) {
        try {
            PageHelper.startPage(entity.getCurrent(), entity.getSize());
            // List<EsShopFullGift> esShopDiscount = fullGiftService.slelectPurchase2();
            return new CommonResult().success(PageInfo.of(codeGiftService.list(new QueryWrapper<>(entity))));
        } catch (Exception e) {
            log.error("根据条件查询所有验证码赠礼列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "验证码下载csv", REMARK = "验证码下载csv")
    @ApiOperation("验证码下载csv")
    @PostMapping(value = "/codecsv")
    public Object codecsv(HttpServletRequest request, HttpServletResponse response) {
        try {
            ExcelExportUtil excelExportUtil = new ExcelExportUtil();
            return excelExportUtil.getHSSFWorkbook(request, response);
        } catch (Exception e) {
            log.error("根据条件查询所有验证码赠礼列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "验证码赠礼管理", REMARK = "保存验证码赠礼")
    @ApiOperation("保存验证码赠礼")
    @PostMapping(value = "/save")
    public Object saveCodeGift(EsShopCodeGift entity) {
        try {
            //商品
            List<GoodsSepcVo> list = JSONObject.parseArray(entity.getGoodSpecList(), GoodsSepcVo.class);
            List<EsShopCodeGiftRule> list2 = JSONObject.parseArray(entity.getCodeList(), EsShopCodeGiftRule.class);
            //赠品
            List<EsShopCodeGiftGoodsMap> list3 = JSONObject.parseArray(entity.getSongGoodsIds(), EsShopCodeGiftGoodsMap.class);
            entity.setGoodsSepcVoList(list);
            entity.setCoderuleList(list2);
            entity.setGoodsCouponList(list3);
            TestCSVUtil csvUtil = new TestCSVUtil();
            //唯一验证
            if (entity.getType() == 1) {
                EsShopCodeGiftRule codegif = codeGiftService.codegif2(list2.get(0).getCode());
                if (codegif != null && codegif.getCount() > 0) {
                    return new CommonResult().failed("验证码已存在");
                }
            }
            //批量验证码
            if (entity.getType() == 2) {
                List<String> source = csvUtil.readcsvFile(list2.get(0).getCodeOpenid());
                StringBuilder builder = new StringBuilder();
                Set set = new HashSet(source);
                for (int i = 0; i < source.size(); i++) {
                    if (set.size() < source.size()) {
                        return new CommonResult().failed("验证码上传文件有重复内容");
                    } else {
                        if (i == source.size() - 1) {
                            builder.append(source.get(i));
                            break;
                        }
                        builder.append(source.get(i)).append(",");
                    }
                }
                //验证码信息
                list2.get(0).setCode(builder.toString());
            }

            if (codeGiftService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存验证码赠礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "验证码赠礼管理", REMARK = "更新验证码赠礼")
    @ApiOperation("更新验证码赠礼")
    @PostMapping(value = "/update")
    public Object updateCodeGift(EsShopCodeGift entity) {
        try {
            //商品
            List<GoodsSepcVo> list = JSONObject.parseArray(entity.getGoodSpecList(), GoodsSepcVo.class);
            List<EsShopCodeGiftRule> list2 = JSONObject.parseArray(entity.getCodeList(), EsShopCodeGiftRule.class);
            //赠品
            List<EsShopCodeGiftGoodsMap> list3 = JSONObject.parseArray(entity.getSongGoodsIds(), EsShopCodeGiftGoodsMap.class);
            entity.setGoodsSepcVoList(list);
            entity.setCoderuleList(list2);
            entity.setGoodsCouponList(list3);
            TestCSVUtil csvUtil = new TestCSVUtil();
            //唯一验证
            if (entity.getType() == 1) {
                String code = list2.get(0).getCode();
                //本身code是否相等
                EsShopCodeGiftRule codegif = codeGiftService.codegif(entity.getId());
                EsShopCodeGiftRule code2 = codeGiftService.codegif2(code);
                if (code.equals(codegif.getCode())) {
                    return new CommonResult().success(codeGiftService.update(entity));
                } else if (code2 == null || code2.getCount() == 0) {
                    return new CommonResult().success(codeGiftService.update(entity));
                } else {
                    if (code2.getCount() > 0) {
                        return new CommonResult().failed("验证码已存在");
                    }
                }
            }
            //批量验证码
            if (entity.getType() == 2) {
                List<String> source = csvUtil.readcsvFile(list2.get(0).getCodeOpenid());
                StringBuilder builder = new StringBuilder();
                Set set = new HashSet(source);
                for (int i = 0; i < source.size(); i++) {
                    if (set.size() < source.size()) {
                        return new CommonResult().failed("上传的验证码有重复，请重新上传");
                    } else {
                        if (i == source.size() - 1) {
                            builder.append(source.get(i));
                            break;
                        }
                        builder.append(source.get(i)).append(",");
                    }
                }
                //验证码信息
                list2.get(0).setCode(builder.toString());
                return new CommonResult().success(codeGiftService.update(entity));
            }

        } catch (Exception e) {
            log.error("更新验证码赠礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "验证码赠礼管理", REMARK = "删除验证码赠礼")
    @ApiOperation("删除验证码赠礼")
    @PostMapping(value = "/delete")
    public Object deleteCodeGift(@ApiParam("验证码赠礼id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("验证码赠礼id");
            }
            EsShopCodeGift manjian = codeGiftService.getById(id);
            if (manjian.getStatus() == 1) {
                return new CommonResult().failed("已启用计划，不能直接删除，需禁用后方可删除");
            }
            if (codeGiftService.deleteCode(id) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除验证码赠礼：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "验证码赠礼管理", REMARK = "验证码明细查询")
    @ApiOperation("验证码明细查询")
    @PostMapping(value = "/codeselect")
    public Object codeselect(@ApiParam("验证码赠礼id") @RequestParam Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("验证码赠礼id");
            }
            return new CommonResult().success(codeGiftService.CodeList(id));

        } catch (Exception e) {
            log.error("验证码明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "验证码赠礼管理", REMARK = "修改状态")
    @ApiOperation("修改状态 status 为列表的状态")
    @PostMapping(value = "/updateStatus")
    public Object updateShowStatus(@RequestParam("id") Long id,
                                   @RequestParam("status") Integer status) {
        int count = codeGiftService.updateShowStatus(id, status);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

}
