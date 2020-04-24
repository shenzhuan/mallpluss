package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.bill.entity.BakBrand;
import com.zscat.mallplus.bill.entity.BakCategory;
import com.zscat.mallplus.bill.entity.BakGoods;
import com.zscat.mallplus.bill.mapper.BakBrandMapper;
import com.zscat.mallplus.bill.mapper.BakCategoryMapper;
import com.zscat.mallplus.bill.mapper.BakGoodsMapper;
import com.zscat.mallplus.build.service.IBuildWuyeCompanyService;
import com.zscat.mallplus.build.service.IBuildingCommunityService;
import com.zscat.mallplus.pms.entity.PmsBrand;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.entity.PmsProductAttributeCategory;
import com.zscat.mallplus.pms.entity.PmsProductCategory;
import com.zscat.mallplus.pms.mapper.PmsBrandMapper;
import com.zscat.mallplus.pms.mapper.PmsProductAttributeCategoryMapper;
import com.zscat.mallplus.pms.mapper.PmsProductCategoryMapper;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.sys.entity.SysTest;
import com.zscat.mallplus.sys.mapper.SysStoreMapper;
import com.zscat.mallplus.sys.mapper.SysUserMapper;
import com.zscat.mallplus.sys.service.ISysTestService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mallplus
 * @date 2019-12-02
 * 测试
 */
@Slf4j
@RestController
@RequestMapping("/sys/sysTest")
public class SysTestController {

    @Resource
    private ISysTestService ISysTestService;

    @Resource
    private SysStoreMapper storeMapper;
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private BakCategoryMapper bakCategoryMapper;
    @Resource
    private BakGoodsMapper bakGoodsMapper;
    @Resource
    private IPmsProductService productService;
    @Resource
    private BakBrandMapper bakBrandMapper;
    @Resource
    private PmsProductCategoryMapper pmsProductCategoryMapper;
    @Resource
    private PmsProductAttributeCategoryMapper pmsProductAttributeCategoryMapper;
    @Resource
    private PmsBrandMapper pmsBrandMapper;
    @Resource
    private IBuildingCommunityService communityService;
    @Resource
    private IBuildWuyeCompanyService wuyeCompanyService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有测试列表")
    @ApiOperation("根据条件查询所有测试列表")
    @GetMapping(value = "/list")
    public Object getSysTestByPage() {
        try {
            BakCategory category = bakCategoryMapper.selectById(1010000);
            PmsProductCategory pmsProductCategory = new PmsProductCategory();
            pmsProductCategory.setId(category.getId());
            pmsProductCategory.setIcon(category.getIconUrl());
            pmsProductCategory.setDescription(category.getDescs());
            pmsProductCategory.setName(category.getName());
            pmsProductCategory.setKeywords(category.getKeywords());
            pmsProductCategory.setParentId(category.getPid().longValue());
            pmsProductCategory.setLevel(0);
            pmsProductCategory.setSort(category.getSortOrder());
            pmsProductCategory.setNavStatus(1);
            pmsProductCategory.setIndexStatus(1);
            pmsProductCategory.setShowStatus(1);
            pmsProductCategoryMapper.insert(pmsProductCategory);


            PmsProductAttributeCategory pmsProductAttributeCategory = new PmsProductAttributeCategory();
            pmsProductAttributeCategory.setPic(category.getIconUrl());
            pmsProductAttributeCategory.setId(category.getId());
            pmsProductAttributeCategory.setName(category.getName());
            pmsProductAttributeCategory.setStyle(1);
            pmsProductAttributeCategory.setShowIndex(1);

            pmsProductAttributeCategoryMapper.insert(pmsProductAttributeCategory);

            List<BakCategory> categoryList = bakCategoryMapper.selectList(new QueryWrapper<BakCategory>().eq("pid", 1010000));
            for (BakCategory bakCategory : categoryList) {

                PmsProductCategory pmsProductCategory1 = new PmsProductCategory();
                pmsProductCategory1.setSort(bakCategory.getSortOrder());
                pmsProductCategory1.setId(bakCategory.getId());
                pmsProductCategory1.setIcon(bakCategory.getIconUrl());
                pmsProductCategory1.setName(bakCategory.getName());
                pmsProductCategory1.setKeywords(bakCategory.getKeywords());
                pmsProductCategory1.setParentId(bakCategory.getPid().longValue());
                pmsProductCategory1.setLevel(1);
                pmsProductCategory1.setNavStatus(1);
                pmsProductCategory1.setIndexStatus(1);
                pmsProductCategory1.setShowStatus(1);
                pmsProductCategory.setDescription(category.getDescs());
                pmsProductCategoryMapper.insert(pmsProductCategory1);
            }
            List<Long> ids = categoryList.stream()
                    .map(BakCategory::getId)
                    .collect(Collectors.toList());
            if (ids != null) {
                List<BakGoods> goodsList = bakGoodsMapper.selectList(new QueryWrapper<BakGoods>().in("category_id", ids));
                for (BakGoods gg : goodsList) {
                    createG(gg, 1, pmsProductAttributeCategory);
                }
                List<Integer> brands = goodsList.stream()
                        .map(BakGoods::getBrandId)
                        .collect(Collectors.toList());
                if (brands != null) {
                    List<BakBrand> bakBrands = bakBrandMapper.selectBatchIds(brands);
                    if (bakBrands != null && bakBrands.size() > 0) {
                        for (BakBrand bakBrand : bakBrands) {
                            PmsBrand brand = new PmsBrand();
                            brand.setId(bakBrand.getId());
                            brand.setBrandStory(bakBrand.getDescs());
                            brand.setBigPic(bakBrand.getPicUrl());
                            brand.setName(bakBrand.getName());
                            brand.setShowStatus(1);
                            brand.setFactoryStatus(1);
                            brand.setFirstLetter(bakBrand.getName().substring(0, 1));
                            brand.setLogo(bakBrand.getPicUrl());
                            brand.setSort(bakBrand.getSortOrder());
                            pmsBrandMapper.insert(brand);
                        }
                    }
                }
            }


        } catch (Exception e) {
            log.error("根据条件查询所有测试列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    void createG(BakGoods gg, Integer storeId, PmsProductAttributeCategory pmsProductAttributeCategory) {
        PmsProduct g = new PmsProduct();

        g.setName(gg.getName());
        g.setSubTitle(gg.getBrief());
        g.setDescription(gg.getBrief());
        g.setDetailHtml(gg.getDetail());
        g.setDetailMobileHtml(gg.getDetail());
        g.setDetailTitle(gg.getBrief());
        g.setDetailDesc(gg.getBrief());
        g.setAreaId(1301L);
        g.setAreaName("石家庄");
        g.setPic(gg.getPicUrl());

        g.setAlbumPics(gg.getPicUrl());
        if (ValidatorUtils.notEmpty(gg.getCounterPrice())) {
            g.setPrice(gg.getCounterPrice());
        }
        if (ValidatorUtils.notEmpty(gg.getRetailPrice())) {
            g.setOriginalPrice(gg.getRetailPrice());
        }

        g.setSort(gg.getSortOrder());
        g.setSale(gg.getCategoryId());
        g.setStock(gg.getId());
        g.setLowStock(0);
        g.setIsVip(0);
        g.setGiftGrowth(0);
        g.setGiftPoint(0);
        g.setIsFenxiao(0);
        g.setPublishStatus(1);
        g.setGiftPoint(gg.getCategoryId());
        g.setGiftGrowth(gg.getCategoryId());
        g.setPromotionType(0);
        g.setVerifyStatus(1);
        g.setProductSn(gg.getGoodsSn());
        g.setQsType(1);
        g.setNewStatus(1);
        g.setCreateTime(new Date());

        g.setBrandId(gg.getBrandId().longValue());

        g.setProductCategoryId(gg.getCategoryId().longValue());
        g.setUnit(gg.getUnit());
        g.setProductAttributeCategoryId(pmsProductAttributeCategory.getId());
        productService.save(g);
    }

    @SysLog(MODULE = "sys", REMARK = "保存测试")
    @ApiOperation("保存测试")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sys:sysTest:create')")
    public Object saveSysTest(@RequestBody SysTest entity) {
        try {

            if (ISysTestService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存测试：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "更新测试")
    @ApiOperation("更新测试")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sys:sysTest:update')")
    public Object updateSysTest(@RequestBody SysTest entity) {
        try {
            if (ISysTestService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新测试：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "删除测试")
    @ApiOperation("删除测试")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sys:sysTest:delete')")
    public Object deleteSysTest(@ApiParam("测试id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("测试id");
            }
            if (ISysTestService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除测试：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "给测试分配测试")
    @ApiOperation("查询测试明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sys:sysTest:read')")
    public Object getSysTestById(@ApiParam("测试id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("测试id");
            }
            SysTest coupon = ISysTestService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询测试明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除测试")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "sys", REMARK = "批量删除测试")
    @PreAuthorize("hasAuthority('sys:sysTest:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = ISysTestService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "sys", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SysTest entity) {
        // 模拟从数据库获取需要导出的数据
        List<SysTest> personList = ISysTestService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SysTest.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "sys", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SysTest> personList = EasyPoiUtils.importExcel(file, SysTest.class);
        ISysTestService.saveBatch(personList);
    }
}


