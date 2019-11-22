package com.mei.zhuang.controller.goods;

import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.goods.EsShopGoodsImg;
import com.mei.zhuang.entity.goods.EsShopGoodsImgGroup;
import com.mei.zhuang.service.goods.EsShopGoodsImgGroupService;
import com.mei.zhuang.service.goods.EsShopGoodsImgService;
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
@Api(value = "商品图片分组管理", description = "", tags = {"商品图片分组管理"})
@RestController
@RequestMapping("/api/goondsimggroup")
public class EsShopGoodsImgGroupController {

    @Resource
    private EsShopGoodsImgGroupService esShopGoodsImgGroupService;
    @Resource
    private EsShopGoodsImgService esShopGoodsImgService;

    @SysLog(MODULE = "商品图片分组管理", REMARK = "新增商品图片分组")
    @ApiOperation("新增商品图片分组")
    @PostMapping("/insEsShopGoodsImgGroup")
    public Object insEsShopGoodsImgGroup(@RequestParam("grouName") String grouName) {
        try {
            if (ValidatorUtils.empty(grouName)) {
                return new CommonResult().failed("名称不得为空！");
            }
            //判断分组是否存在
            Integer count = esShopGoodsImgGroupService.selCountGroupName(grouName);
            if (count > 0) {
                return new CommonResult().failed("图片分组已存在");
            }
            EsShopGoodsImgGroup entity = new EsShopGoodsImgGroup();
            entity.setGroupName(grouName);
            Integer obj = esShopGoodsImgGroupService.insEsShopGoodsImgGroup(entity);
            return new CommonResult().success("success", obj);
        } catch (Exception e) {
            log.error("新增商品图片分组异常", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品图片分组管理", REMARK = "删除商品图片分组")
    @ApiOperation("删除商品图片分组")
    @PostMapping("/delEsShopGoodsImgGroup")
    public Object delEsShopGoodsImgGroup(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("编号不得为空");
            }
            EsShopGoodsImg img = new EsShopGoodsImg();
            img.setId(id);
            List<EsShopGoodsImg> list = esShopGoodsImgService.selImgs(img);
            if (list.size() > 0) {
                for (EsShopGoodsImg imgList : list) {
                    //删除分类下的所有图片
                    esShopGoodsImgService.delImg(imgList.getId());
                }
            }
            Integer obj = esShopGoodsImgGroupService.delEsShopGoodsImgGroup(id);
            if (obj > 0) {
                return new CommonResult().success("success", obj);
            } else {
                return new CommonResult().success("fail", obj);
            }

        } catch (Exception e) {
            log.error("删除商品图片分组异常", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品图片分组管理", REMARK = "查询商品图片分组")
    @ApiOperation("查询商品图片分组")
    @PostMapping("/selEsShopGoodsImgGroup")
    public Object selEsShopGoodsImgGroup() {
        try {
            Object obj = esShopGoodsImgGroupService.selEsShopGoodsImgGroup();
            return new CommonResult().success("success", obj);
        } catch (Exception e) {
            log.error("新增商品图片分组异常", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

}

