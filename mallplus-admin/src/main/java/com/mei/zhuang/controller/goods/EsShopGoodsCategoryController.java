package com.mei.zhuang.controller.goods;

import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.service.goods.EsShopGoodsCategoryService;
import com.mei.zhuang.service.goods.EsShopGoodsService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.entity.goods.EsShopGoodsCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品分类管理
 */
@Slf4j
@Api(value = "商品分类管理", description = "", tags = {"商品分类管理"})
@RestController
@RequestMapping("/api/goodsCategory")
public class EsShopGoodsCategoryController {

    @Resource
    private EsShopGoodsCategoryService shopGoodsCategoryService;
    @Resource
    private EsShopGoodsService goodsService;


    @SysLog(MODULE = "商品分类管理", REMARK = "根据条件查询所有商品分类列表")
    @ApiOperation("根据条件查询所有商品分类列表")
    @PostMapping(value = "/list")
    public Object getGoodsCategoryByPage(EsShopGoodsCategory entity) {
        try {
            return new CommonResult().success(shopGoodsCategoryService.list(entity.getCurrent(), entity.getSize()));
        } catch (Exception e) {
            log.error("根据条件查询所有商品分类列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "商品分类管理", REMARK = "查询子分类")
    @ApiOperation("查询子分类")
    @PostMapping(value = "/selGoodsCategorySubClass")
    public Object selGoodsCategorySubClass(@RequestParam("parentId") Long parentId) {
        try {
            //非空验证
            if (ValidatorUtils.empty(parentId)) return new CommonResult().paramFailed("商品分类父级id为空");
            return shopGoodsCategoryService.selGoodsCategorySubClass(parentId);
        } catch (Exception e) {
            log.error("根据父id查询子分类：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }


    @SysLog(MODULE = "商品分类管理", REMARK = "保存商品分类")
    @ApiOperation("保存商品分类")
    @PostMapping(value = "/save")
    public Object saveGoodsCategory(EsShopGoodsCategory entity) {
        try {
            if (ValidatorUtils.empty(entity.getName())) {
                return new CommonResult().failed("请指定分类名称");
            }
            if (entity.getDisplayOrder() == null || entity.getDisplayOrder().equals("")) {
                entity.setDisplayOrder(0);
            }
            if (entity.getDisplayOrder() >= 1000000) {
                return new CommonResult().failed("排序不得大于1000000");
            }
            EsShopGoodsCategory category = shopGoodsCategoryService.selCategory(entity.getName());
            if (category != null) {
                return new CommonResult().failed("分类名称已存在");
            }
            if (entity.getDisplayOrder() == null) {
                entity.setDisplayOrder(0);
            }
            return shopGoodsCategoryService.saveGoodsCategory(entity);
        } catch (Exception e) {
            log.error("保存商品分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品分类管理", REMARK = "修改商品分类状态")
    @ApiOperation("商品分类禁用/启用")
    @PostMapping(value = "/updGoodsCategoryStatus")
    public Object updGoodsCategoryStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品分类id为空");
            }
            //判断该分类下是否还有分类,如果存在就禁用
            if (status == 0) {
                List<EsShopGoodsCategory> list = shopGoodsCategoryService.countSubClass(id);
                if (list != null && list.size() > 0) {
                    for (EsShopGoodsCategory es : list) {
                        shopGoodsCategoryService.updGoodsCategoryStatus(es.getId(), status);
                        if (es.getLevel() == 1) {
                            //查询三级分类
                            List<EsShopGoodsCategory> lis = (List<EsShopGoodsCategory>) goodsService.selShopGoodsCategoryTwo(es.getId(), 2);
                            for (EsShopGoodsCategory li : lis) {
                                shopGoodsCategoryService.updGoodsCategoryStatus(li.getId(), status);
                            }
                        }

                    }
                }
            }
            if (status == 1) {
                //判断父级是否启用
                EsShopGoodsCategory category = shopGoodsCategoryService.selDetail(id);
                if (category.getParentId() != null && !category.getParentId().equals("")) {
                    EsShopGoodsCategory category1 = shopGoodsCategoryService.selDetail(category.getParentId());
                    if (category1 != null) {
                        if (category1.getStatus() == 0) {
                            return new CommonResult().failed("父类未启用子类不得启用");
                        }
                    }

                }
            }
            Object obj = shopGoodsCategoryService.updGoodsCategoryStatus(id, status);
            Integer num = null;
            if (obj != null) {
                num = 1;
            } else {
                num = 0;
            }
            return new CommonResult().success("success", num);
        } catch (Exception e) {
            log.error("更新商品分组：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品分类管理", REMARK = "更新商品分类")
    @ApiOperation("更新商品分类")
    @PostMapping(value = "/update")
    public Object updateGoodsCategory(EsShopGoodsCategory entity) {

        try {
            return shopGoodsCategoryService.updateGoodsCategory(entity);
        } catch (Exception e) {
            log.error("更新商品分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品分类管理", REMARK = "更新商品分类排序")
    @ApiOperation("更新商品分类排序")
    @PostMapping(value = "/updateDisplayOrder")
    public Object updateGoodsCategoryDisplayOrder(@RequestParam("categoryList") String categoryList) {
        List<EsShopGoodsCategory> lists = JSONObject.parseArray(categoryList, EsShopGoodsCategory.class);
        try {
            boolean bool = shopGoodsCategoryService.updateBatchById(lists);
            return new CommonResult().success("success", bool);
        } catch (Exception e) {
            log.error("更新商品分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "商品分类管理", REMARK = "批量删除商品分类")
    @ApiOperation("批量删除商品分类")
    @PostMapping(value = "/delGoodsCategory")
    public Object batchDelete(@RequestParam("ids") String ids) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().paramFailed("商品分类ids");
            }
            Integer num = null;
            String[] id = ids.split(",");
            for (int i = 0; i < id.length; i++) {
                Long parentId = Long.parseLong(id[i]);
                if (parentId == 1) {
                    return new CommonResult().failed("此分类不得删除");
                }
                List<EsShopGoodsCategory> list = shopGoodsCategoryService.countSubClass(parentId);
                //判断分类下是否还有子分类
                if (list.size() > 0) {
                    for (EsShopGoodsCategory es : list) {
                        shopGoodsCategoryService.delGoodsCategory(es.getId());
                    }
                }
                //删除分类
                num = shopGoodsCategoryService.delGoodsCategory(parentId);
            }
            return new CommonResult().success("success", num);

        } catch (Exception e) {
            log.error("删除商品分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品分类管理", REMARK = "查询商品分类明细")
    @ApiOperation("查询商品分类明细")
    @PostMapping(value = "/detail")
    public Object getGoodsCategoryById(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品分类id为空");
            }
            EsShopGoodsCategory coupon = shopGoodsCategoryService.selDetail(id);
            //查询分类推荐商品
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询商品分类明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "商品分类管理", REMARK = "查询所有商品分类")
    @ApiOperation("查询所有分类")
    @PostMapping(value = "/selectAll")
    public Object selectAll() {
        try {

            Object coupon = shopGoodsCategoryService.selectAll();
            //查询分类推荐商品
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询所有分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }


}
