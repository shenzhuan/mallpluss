package com.mei.zhuang.controller.goods;

import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.service.goods.EsShopGoodsQRCodeService;
import com.mei.zhuang.service.goods.EsStartAdvertisingImgService;
import com.mei.zhuang.service.goods.EsStartAdvertisingService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.utils.ValidatorUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.entity.goods.EsShopGoodsQRCode;
import com.mei.zhuang.entity.goods.EsStartAdvertising;
import com.mei.zhuang.entity.goods.EsStartAdvertisingImg;
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
@Api(value = "启动广告管理", description = "", tags = {"启动管理"})
@RestController
@RequestMapping("/api/advertising")
public class EsStartAdvertisingController {

    @Resource
    private EsStartAdvertisingService esStartAdvertisingService;

    @Resource
    private EsStartAdvertisingImgService esStartAdvertisingImgService;
    @Resource
    private EsShopGoodsQRCodeService esShopGoodsQRCodeService;

    @SysLog(MODULE = "启动广告管理", REMARK = "查询启动广告列表")
    @ApiOperation("查询启动广告列表")
    @PostMapping("/select")
    public Object select(EsStartAdvertising entity) {
        try {

            return new CommonResult().success("success", esStartAdvertisingService.select(entity));
        } catch (Exception e) {
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "启动广告管理", REMARK = "删除启动用广告")
    @ApiOperation("删除启动用广告")
    @PostMapping("/delete")
    public Object delete(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("编号不得为空");
            }
            //已启用的广告不得删除
            EsStartAdvertising esStartAdvertising = esStartAdvertisingService.getById(id);
            if (esStartAdvertising.getIsStart() == 1 || esStartAdvertising.getIsStart() == 2) {
                return new CommonResult().failed("已启用广告不得删除");
            }
            return new CommonResult().success("success", esStartAdvertisingService.removeById(id));
        } catch (Exception e) {
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "启动广告管理", REMARK = "新增启动用广告")
    @ApiOperation("新增启动用广告")
    @PostMapping("/save")
    public Object save(EsStartAdvertising entity) {
        try {
            if (ValidatorUtils.empty(entity.getAdvertName())) {
                return new CommonResult().failed("广告名称不得为空");
            }
            if (entity.getAdvertImg() != null && !entity.getAdvertImg().equals("")) {
                entity.setListAdvertImg(JSONObject.parseArray(entity.getAdvertImg(), EsStartAdvertisingImg.class));
                if (entity.getListAdvertImg() != null && entity.getListAdvertImg().size() > 10) {
                    return new CommonResult().failed("图片数量不得超过10张");
                }
            }
            //判断是否有启动广告或定时广告
            if (entity.getIsStart() == 1 || entity.getIsStart() == 2) {
                //判断是否有启动广告
                Integer count = esStartAdvertisingService.countStatus(null);
                if (count > 0) {
                    return new CommonResult().failed("广告只能启用一个");
                }
            }
            return new CommonResult().success("success", esStartAdvertisingService.save(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "启动广告管理", REMARK = "查询启动广告详情")
    @ApiOperation("查询启动广告详情")
    @PostMapping("/detail")
    public Object detail(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请选择广告");
            }
            EsStartAdvertising esStartAdvertising = esStartAdvertisingService.getById(id);
            if (esStartAdvertising != null) {
                EsStartAdvertisingImg advertisingImg = new EsStartAdvertisingImg();
                advertisingImg.setAdvertisingId(id);
                List<EsStartAdvertisingImg> list = esStartAdvertisingImgService.list(new QueryWrapper<>(advertisingImg));
                if (list != null && list.size() > 0) {
                    esStartAdvertising.setListAdvertImg(list);
                }
            }

            return new CommonResult().success("success", esStartAdvertising);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "启动广告管理", REMARK = "修改启动广告")
    @ApiOperation("修改启动广告")
    @PostMapping("/update")
    public Object detail(EsStartAdvertising entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("广告编号不得为空");
            }
            //判断是否有启动广告或定时广告
            if (entity.getIsStart() == 1) {
                //判断是否有启动广告
                Integer count = esStartAdvertisingService.countStatus(entity.getId());
                if (count > 0) {
                    return new CommonResult().failed("广告只能启用一个");
                }
            }
            if (entity.getIsStart() == 2) {
                //判断是否有启动广告
                Integer count = esStartAdvertisingService.countStatus(entity.getId());
                if (count > 0) {
                    return new CommonResult().failed("广告只能启用一个");
                }
            }
            return new CommonResult().success("success", esStartAdvertisingService.updateAdvert(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "启动广告管理", REMARK = "查询商品二维码")
    @ApiOperation("查询商品二维码")
    @PostMapping("/selQRCode")
    public Object selQRCode(Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定商品编号");
            }
            EsShopGoodsQRCode qrCode = new EsShopGoodsQRCode();
            qrCode.setGoodsId(id);
            return new CommonResult().success("success", esShopGoodsQRCodeService.getOne(new QueryWrapper<>(qrCode)));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "启动广告管理", REMARK = "查询已启动广告")
    @ApiOperation("查询已启动广告")
    @PostMapping("/selAdvert")
    public Object selAdvert() {
        try {
            EsStartAdvertising startAdvertising = new EsStartAdvertising();
            startAdvertising.setIsStart(1);
            return new CommonResult().success("success", esStartAdvertisingService.getOne(new QueryWrapper<>(startAdvertising)));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }
}
