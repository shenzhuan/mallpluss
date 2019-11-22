package com.mei.zhuang.controller.goods;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.service.goods.EsShopGoodsGroupService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.utils.ValidatorUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsGroup;
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
 * 商品分组管理
 */
@Slf4j
@Api(value = "商品分组管理", description = "", tags = {"商品分组管理"})
@RestController
@RequestMapping("/api/goodsGroup")
public class EsShopGoodsGroupController {


    @Resource
    private EsShopGoodsGroupService shopGoodsGroupService;


    @SysLog(MODULE = "商品分组管理", REMARK = "选择商品分类查询商品列表")
    @ApiOperation("选择商品分类查询商品列表")
    @PostMapping(value = "/selGoodsCateList")
    public Map<String, Object> goodscatelist(EsShopGoods entity) {
        try {
            List<Map<String, Object>> goodsgrouplist = shopGoodsGroupService.goodscatelist(entity);
            Map<String, Object> map = new HashMap<>();
            map.put("rows", goodsgrouplist);
            map.put("success", true);
            return map;
        } catch (Exception e) {
            log.error("选择商品分类查询商品列表：%s", e.getMessage(), e);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("message", "查询商品分组列表失败");
        map.put("success", true);
        return map;
    }

    @SysLog(MODULE = "商品分组管理", REMARK = "根据名称查询所有商品分组列表")
    @ApiOperation("根据名称查询所有商品分组列表")
    @PostMapping(value = "/goodsLists")
    public Object goodsLists(EsShopGoodsGroup entity) {
        try {
            PageHelper.startPage(entity.getCurrent(), entity.getSize());

            return new CommonResult().success(PageInfo.of(shopGoodsGroupService.list(new QueryWrapper<>(entity))));
        } catch (Exception e) {
            log.error("根据名称查询所有商品分组列表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }


    }

    @SysLog(MODULE = "商品分组管理", REMARK = "保存商品分组")
    @ApiOperation("保存商品分组")
    @PostMapping(value = "/save")
    public Object saveGoodsGroup(EsShopGoodsGroup entity) {
        try {
            //判断分组是否存在
            if (ValidatorUtils.empty(entity.getName())) {
                return new CommonResult().failed("请指定分组名称");
            }
            Integer count = shopGoodsGroupService.selCateCount(entity.getName());
            if (count > 0) {
                return new CommonResult().failed("分组已存在");
            } else {
                return shopGoodsGroupService.saveGoodsGroup(entity);
            }

        } catch (Exception e) {
            log.error("保存商品分组：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品分组管理", REMARK = "更新商品分组")
    @ApiOperation("更新商品分组")
    @PostMapping(value = "/update")
    public Object updateGoodsGroup(EsShopGoodsGroup entity) {
        try {
            if (ValidatorUtils.empty(entity.getName())) {
                return new CommonResult().failed("分组名称为空");
            }
            return shopGoodsGroupService.updateGoodsGroup(entity);
        } catch (Exception e) {
            log.error("更新商品分组：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品分组管理", REMARK = "批量修改商品分组状态")
    @ApiOperation("批量修改商品分组状态")
    @PostMapping(value = "/updStatus")
    public Object updatestateid(@RequestParam("ids") String ids, @RequestParam("status") Integer status) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().failed("Id不得为空");
            }
            String[] id = ids.split(",");
            for (int i = 0; i < id.length; i++) {
                shopGoodsGroupService.updatestateid(id[i], status);
            }
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("修改商品分组状态：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "商品分组管理", REMARK = "批量删除商品分组")
    @ApiOperation("批量删除商品分组")
    @PostMapping(value = "/delete")
    public Object deleteGoodsGroups(@ApiParam("批量商品分组ids") @RequestParam("ids") String ids) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().paramFailed("商品分组id");
            }
            shopGoodsGroupService.deleteGoodsGroups(ids);
            return new CommonResult().success();

        } catch (Exception e) {
            log.error("删除商品分组：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品分组管理", REMARK = "查询商品分组明细")
    @ApiOperation("查询商品分组明细")
    @PostMapping(value = "/detail")
    public Object getGoodsGroupById(@ApiParam("商品分组id") @RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品分组id");
            }
            return shopGoodsGroupService.getGoodsGroupById(id);
        } catch (Exception e) {
            log.error("查询商品分组明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "商品分组管理", REMARK = "查询所有已启用分组")
    @ApiOperation("查询所有已启用分组")
    @PostMapping(value = "/selectAll")
    public Object selectAll() {
        try {
            EsShopGoodsGroup group = new EsShopGoodsGroup();
            group.setStatus(1);
            return new CommonResult().success("success", shopGoodsGroupService.list(new QueryWrapper<>(group)));
        } catch (Exception e) {
            log.error("查询所有已启用分组：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
}
