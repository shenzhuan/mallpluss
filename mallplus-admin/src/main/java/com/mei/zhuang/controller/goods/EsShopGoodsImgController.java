package com.mei.zhuang.controller.goods;


import com.mei.zhuang.service.goods.EsShopGoodsImgGroupService;
import com.mei.zhuang.service.goods.EsShopGoodsImgService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.entity.goods.EsShopGoodsImg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(value = "图片管理", description = "", tags = {"图片管理"})
@RestController
@RequestMapping("/api/goodsImg")
public class EsShopGoodsImgController {

    @Resource
    private EsShopGoodsImgService esShopGoodsImgService;
    @Resource
    private EsShopGoodsImgGroupService esShopGoodsImgGroupService;


    @SysLog(MODULE = "图片管理", REMARK = "根据分类查询图片")
    @ApiOperation("根据分类查询图片")
    @PostMapping("/selImg")
    public Object selImg(EsShopGoodsImg entity) {

        return new CommonResult().success("success", esShopGoodsImgService.selImgs(entity));
    }

    @SysLog(MODULE = "图片管理", REMARK = "删除图片")
    @ApiOperation("删除图片")
    @PostMapping("/delImg")
    public Object delImg(@RequestParam("id") Long id) {
        if (ValidatorUtils.empty(id)) {
            return new CommonResult().failed("请指定图片ID");
        }
        return new CommonResult().success("success", esShopGoodsImgService.delImg(id));
    }
}
