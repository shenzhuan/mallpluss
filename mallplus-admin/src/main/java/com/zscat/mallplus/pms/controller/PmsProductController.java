package com.zscat.mallplus.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.enums.AllEnum;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.entity.OmsOrderItem;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.entity.PmsProductVertifyRecord;
import com.zscat.mallplus.pms.entity.PmsSkuStock;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.pms.service.IPmsSkuStockService;
import com.zscat.mallplus.pms.vo.PmsProductParam;
import com.zscat.mallplus.pms.vo.PmsProductResult;
import com.zscat.mallplus.ums.entity.UmsMemberTag;
import com.zscat.mallplus.ums.service.IUmsMemberTagService;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.IdStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品信息
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "PmsProductController", description = "商品信息管理")
@RequestMapping("/pms/PmsProduct")
public class PmsProductController {
    @Resource
    private IPmsProductService IPmsProductService;
    @Resource
    private com.zscat.mallplus.ums.service.IUmsMemberTagService IUmsMemberTagService;
    @Resource
    private IPmsSkuStockService skuStockService;
    @SysLog(MODULE = "pms", REMARK = "根据条件查询所有商品信息列表")
    @ApiOperation("根据条件查询所有商品信息列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public Object getPmsProductByPage(PmsProduct entity,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            IPage<PmsProduct> page = null;
            if (ValidatorUtils.empty(entity.getStatus())) {
                entity.setStatus(0);
            }
            if (ValidatorUtils.notEmpty(entity.getKeyword())) {
                if (entity.getStatus() == 1) {
                    entity.setDeleteStatus(1);
                    entity.setPublishStatus(1);
                    entity.setVerifyStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 2) {
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).eq("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 3) {
                    entity.setPublishStatus(0);
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 4) {
                    entity.setDeleteStatus(0);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else {
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                }
            } else {
                if (entity.getStatus() == 1) {
                    entity.setDeleteStatus(1);
                    entity.setPublishStatus(1);
                    entity.setVerifyStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 2) {
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            eq("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 3) {
                    entity.setPublishStatus(0);
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 4) {
                    entity.setDeleteStatus(0);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else {
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                }

            }
            return new CommonResult().success(page);
        } catch (Exception e) {
            log.error("根据条件查询所有商品信息列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "根据条件查询所有商品信息列表")
    @ApiOperation("根据条件查询所有商品信息列表")
    @GetMapping(value = "/listBySku")
    @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public Object listBySku(PmsProduct entity,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            IPage<PmsProduct> page = null;
            if (ValidatorUtils.empty(entity.getStatus())) {
                entity.setStatus(0);
            }
            if (ValidatorUtils.notEmpty(entity.getKeyword())) {
                if (entity.getStatus() == 1) {
                    entity.setDeleteStatus(1);
                    entity.setPublishStatus(1);
                    entity.setVerifyStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 2) {
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).eq("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 3) {
                    entity.setPublishStatus(0);
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 4) {
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else {
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                }
            } else {
                if (entity.getStatus() == 1) {
                    entity.setDeleteStatus(1);
                    entity.setPublishStatus(1);
                    entity.setVerifyStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 2) {
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            eq("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 3) {
                    entity.setDeleteStatus(1);
                    entity.setPublishStatus(0);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 4) {
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else {
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                }

            }
            if (page.getRecords()!=null && page.getRecords().size()>0){
                for (PmsProduct product : page.getRecords()) {
                    List<PmsSkuStock> skuStockList = skuStockService.list(new QueryWrapper<PmsSkuStock>().eq("product_id", product.getId()));
                    product.setSkuStockList(skuStockList);
                }
            }

            return new CommonResult().success(page);
        } catch (Exception e) {
            log.error("根据条件查询所有商品信息列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @ApiOperation("根据商品名称或货号模糊查询")
    @RequestMapping(value = "/simpleList", method = RequestMethod.GET)
    @ResponseBody
    public Object getList(String keyword) {
        List<PmsProduct> productList = IPmsProductService.list(keyword);
        return new CommonResult().success(productList);
    }

    @SysLog(MODULE = "pms", REMARK = "保存商品信息")
    @ApiOperation("保存商品信息")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('pms:PmsProduct:create')")
    public Object savePmsProduct(@RequestBody PmsProductParam productParam) {
        try {
            productParam.setDeleteStatus(1);
            int count = IPmsProductService.create(productParam);
            if (count > 0) {
                return new CommonResult().success(count);
            } else {
                return new CommonResult().failed();
            }
        } catch (Exception e) {
            log.error("保存商品信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "pms", REMARK = "更新商品信息")
    @ApiOperation("更新商品信息")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public Object updatePmsProduct(@PathVariable Long id, @RequestBody PmsProductParam productParam) {
        try {
            int count = IPmsProductService.update(id, productParam);
            if (count > 0) {
                return new CommonResult().success(count);
            } else {
                return new CommonResult().failed();
            }
        } catch (Exception e) {
            log.error("更新商品信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "pms", REMARK = "删除商品信息")
    @ApiOperation("删除商品信息")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProduct:delete')")
    public Object deletePmsProduct(@ApiParam("商品信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品信息id");
            }
            if (IPmsProductService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除商品信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "给商品信息分配商品信息")
    @ApiOperation("查询商品信息明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public Object getPmsProductById(@ApiParam("商品信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品信息id");
            }
            PmsProduct coupon = IPmsProductService.getById(id);
            if (ValidatorUtils.notEmpty(coupon.getTags())){
                String[] ids = coupon.getTags().split(",");
                 List<UmsMemberTag> tagList =  IUmsMemberTagService.list(new QueryWrapper<UmsMemberTag>().eq("type",2).eq("status",1).in("id",ids));
                 coupon.setTagList(tagList);
            }
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询商品信息明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除商品信息")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除商品信息")
    @PreAuthorize("hasAuthority('pms:PmsProduct:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = IPmsProductService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("根据商品id获取商品编辑信息")
    @RequestMapping(value = "/updateInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "根据商品id获取商品编辑信息")
    @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public Object getUpdateInfo(@PathVariable Long id) {
        PmsProductResult productResult = IPmsProductService.getUpdateInfo(id);
        if (ValidatorUtils.notEmpty(productResult.getTags())){
            String[] ids = productResult.getTags().split(",");
            List<UmsMemberTag> tagList =  IUmsMemberTagService.list(new QueryWrapper<UmsMemberTag>().eq("type",2).in("id",ids));
            productResult.setTagList(tagList);
        }
        return new CommonResult().success(productResult);
    }

    @ApiOperation("根据商品id获取审核信息")
    @RequestMapping(value = "/fetchVList/{id}", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "据商品id获取审核信息")
    public Object fetchVList(@PathVariable Long id) {
        List<PmsProductVertifyRecord> list = IPmsProductService.getProductVertifyRecord(id);
        return new CommonResult().success(list);
    }

    @ApiOperation("批量修改审核状态")
    @RequestMapping(value = "/update/verifyStatus")
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量修改审核状态")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public Object updateVerifyStatus(@RequestParam("ids") Long ids,
                                     @RequestParam("verifyStatus") Integer verifyStatus,
                                     @RequestParam("detail") String detail) {
        int count = IPmsProductService.updateVerifyStatus(ids, verifyStatus, detail);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量上下架")
    @RequestMapping(value = "/update/publishStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量上下架")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public Object updatePublishStatus(@RequestParam("ids") List<Long> ids,
                                      @RequestParam("publishStatus") Integer publishStatus) {
        int count = IPmsProductService.updatePublishStatus(ids, publishStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量上下架")
    @RequestMapping(value = "/publishStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量上下架")
    public Object updatePublishStatu(@RequestBody IdStatus ids, BindingResult result) {
        PmsProduct product = new PmsProduct();
        product.setId(ids.getId());
        product.setPublishStatus(ids.getStatus());
        Boolean count = IPmsProductService.updateById(product);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量推荐商品")
    @RequestMapping(value = "/update/recommendStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量推荐商品")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public Object updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                        @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = IPmsProductService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量设为新品")
    @RequestMapping(value = "/update/newStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量设为新品")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public Object updateNewStatus(@RequestParam("ids") List<Long> ids,
                                  @RequestParam("newStatus") Integer newStatus) {
        int count = IPmsProductService.updateNewStatus(ids, newStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量设为分销")
    @RequestMapping(value = "/update/isFenxiao", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量设为分销")
    public Object updateisFenxiao(@RequestParam("ids") List<Long> ids,
                                  @RequestParam("newStatus") Integer newStatus) {
        int count = IPmsProductService.updateisFenxiao(ids, newStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量设为会员商品")
    @RequestMapping(value = "/update/isVip", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量设为会员商品")
    public Object updateisVip(@RequestParam("ids") List<Long> ids,
                              @RequestParam("newStatus") Integer newStatus) {
        int count = IPmsProductService.updateisVip(ids, newStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @ApiOperation("批量修改删除状态")
    @RequestMapping(value = "/update/deleteStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量修改删除状态")
    @PreAuthorize("hasAuthority('pms:PmsProduct:delete')")
    public Object updateDeleteStatus(@RequestParam("ids") List<Long> ids,
                                     @RequestParam("deleteStatus") Integer deleteStatus) {
        int count = IPmsProductService.updateDeleteStatus(ids, deleteStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @GetMapping(value = "/goods/list")
    public Object getPmsProductListByPage(PmsProduct entity,
                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            IPage<PmsProduct> page = null;
            if (ValidatorUtils.empty(entity.getStatus())) {
                entity.setStatus(0);
            }
            if (ValidatorUtils.notEmpty(entity.getKeyword())) {
                if (entity.getStatus() == 1) {
                    entity.setPublishStatus(1);
                    entity.setVerifyStatus(1);
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 2) {
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).eq("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 3) {
                    entity.setDeleteStatus(1);
                    entity.setPublishStatus(0);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 4) {
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else {
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            like("name", entity.getKeyword()).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                }
            } else {
                if (entity.getStatus() == 1) {
                    entity.setPublishStatus(1);
                    entity.setVerifyStatus(1);
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 2) {
                    entity.setDeleteStatus(1);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            eq("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 3) {
                    entity.setDeleteStatus(1);
                    entity.setPublishStatus(0);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            gt("stock", 0).orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else if (entity.getStatus() == 4) {
                    entity.setDeleteStatus(0);
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                } else {
                    page = IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<PmsProduct>(entity).
                            orderByDesc("create_time").select(ConstansValue.sampleGoodsList));
                }

            }
            return new CommonResult().success(page);
        } catch (Exception e) {
            log.error("根据条件查询所有商品信息列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("批量上下架")
    @RequestMapping(value = "/updateReComStatus", method = RequestMethod.POST)
    @ResponseBody
    public Object updateReComStatus(@RequestBody IdStatus ids, BindingResult result) {
        PmsProduct product = new PmsProduct();
        product.setId(ids.getId());
        if (ids.getType() == 1) { // 1推荐2 上下架 3 审核 4 删除 5 分销 6 会员
            product.setRecommandStatus(ids.getStatus());
        } else if (ids.getType() == 2) {
            product.setPublishStatus(ids.getStatus());
        } else if (ids.getType() == 3) {
            product.setVerifyStatus(ids.getStatus());
        } else if (ids.getType() == 4) {
            product.setDeleteStatus(ids.getStatus());
        } else if (ids.getType() == 5) {
            product.setIsFenxiao(ids.getStatus());
        } else if (ids.getType() == 6) {
            product.setIsVip(ids.getStatus());
        }


        Boolean count = IPmsProductService.updateById(product);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }
}
