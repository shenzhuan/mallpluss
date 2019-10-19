package com.zscat.mallplus.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.bill.entity.BakBrand;
import com.zscat.mallplus.bill.entity.BakCategory;
import com.zscat.mallplus.bill.entity.BakGoods;
import com.zscat.mallplus.bill.mapper.BakBrandMapper;
import com.zscat.mallplus.bill.mapper.BakCategoryMapper;
import com.zscat.mallplus.bill.mapper.BakGoodsMapper;
import com.zscat.mallplus.pms.entity.PmsBrand;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.entity.PmsProductAttributeCategory;
import com.zscat.mallplus.pms.entity.PmsProductCategory;
import com.zscat.mallplus.pms.mapper.PmsBrandMapper;
import com.zscat.mallplus.pms.mapper.PmsProductAttributeCategoryMapper;
import com.zscat.mallplus.pms.mapper.PmsProductCategoryMapper;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.sys.entity.SysStore;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.mapper.SysStoreMapper;
import com.zscat.mallplus.sys.mapper.SysUserMapper;
import com.zscat.mallplus.sys.service.ISysStoreService;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.ApiContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-05-18
 */
@Service
public class SysStoreServiceImpl extends ServiceImpl<SysStoreMapper, SysStore> implements ISysStoreService {

    @Resource
    private SysStoreMapper storeMapper;
    @Resource
    private SysUserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApiContext apiContext;
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
    @Transactional
    @Override
    public boolean saveStore(SysStore entity) {
        entity.setTryTime(new Date());
        storeMapper.insert(entity);
        SysUser user = new SysUser();
        user.setUsername(entity.getName());
        SysUser umsAdminList = userMapper.selectByUserName(entity.getName());
        if (umsAdminList!=null) {
            return false;
        }
        user.setStatus(1);
        user.setSupplyId(1L);
        user.setPassword(passwordEncoder.encode(entity.getSupportName()));
        user.setCreateTime(new Date());
        user.setIcon(entity.getLogo());
        user.setNickName(entity.getName());
        //user.setStoreId(entity.getId());
        user.setEmail(entity.getSupportPhone());
        apiContext.setCurrentProviderId(entity.getId());
        //
        if (entity.getType()!=null){
            CompletableFuture.runAsync(() -> {

            BakCategory category =  bakCategoryMapper.selectById(entity.getType());
            PmsProductCategory pmsProductCategory = new PmsProductCategory();
            pmsProductCategory.setIcon(category.getIconUrl());
            pmsProductCategory.setName(category.getName());
            pmsProductCategory.setKeywords(category.getKeywords());
            pmsProductCategory.setParentId(category.getPid().longValue());
            pmsProductCategory.setLevel(Integer.parseInt(category.getLevel().substring(1)));
            pmsProductCategoryMapper.insert(pmsProductCategory);


            PmsProductAttributeCategory pmsProductAttributeCategory = new PmsProductAttributeCategory();
            pmsProductAttributeCategory.setPic(category.getIconUrl());
            pmsProductAttributeCategory.setName(category.getName());

            pmsProductAttributeCategoryMapper.insert(pmsProductAttributeCategory);

            List<BakCategory> categoryList = bakCategoryMapper.selectList(new QueryWrapper<BakCategory>().eq("pid",entity.getType()));
            for (BakCategory bakCategory :categoryList){

                PmsProductCategory pmsProductCategory1 = new PmsProductCategory();
                pmsProductCategory1.setIcon(bakCategory.getIconUrl());
                pmsProductCategory1.setName(bakCategory.getName());
                pmsProductCategory1.setKeywords(bakCategory.getKeywords());
                pmsProductCategory1.setParentId(bakCategory.getPid().longValue());
                pmsProductCategory1.setLevel(Integer.parseInt(bakCategory.getLevel().substring(1)));
                pmsProductCategoryMapper.insert(pmsProductCategory1);
            }
            List<Integer> ids = categoryList.stream()
                    .map(BakCategory::getId)
                    .collect(Collectors.toList());
            if (ids!=null){
              List<BakGoods> goodsList =  bakGoodsMapper.selectList(new QueryWrapper<BakGoods>().in("category_id",ids));
                for (BakGoods gg : goodsList){
                    createG(gg,entity.getId());
                }
                List<Integer> brands = goodsList.stream()
                        .map(BakGoods::getBrandId)
                        .collect(Collectors.toList());
                if (brands!=null){
                    List<BakBrand> bakBrands =bakBrandMapper.selectBatchIds(brands);
                    if (bakBrands!=null && bakBrands.size()>0){
                        for (BakBrand bakBrand : bakBrands){
                            PmsBrand brand = new PmsBrand();
                            brand.setBigPic(bakBrand.getPicUrl());
                            brand.setName(bakBrand.getName());
                            brand.setShowStatus(1);
                            brand.setFactoryStatus(1);
                            brand.setLogo(bakBrand.getPicUrl());
                            brand.setSort(bakBrand.getSortOrder());
                            pmsBrandMapper.insert(brand);
                        }
                    }
                }
            }
            });
        }
        return userMapper.insert(user) > 0;
    }
    void createG(BakGoods gg,Long storeId){
        PmsProduct g = new PmsProduct();

        g.setName(gg.getName());
        g.setSubTitle(gg.getBrief());
        g.setDescription(gg.getBrief());
        g.setDetailHtml(gg.getDetail());
        g.setDetailMobileHtml(gg.getDetail());
        g.setDetailTitle(gg.getBrief());
        g.setDetailDesc(gg.getBrief());

        g.setPic(gg.getPicUrl());

        g.setAlbumPics(gg.getPicUrl());
        if (ValidatorUtils.notEmpty(gg.getCounterPrice())){
            g.setPrice(gg.getCounterPrice());
        }
        if (ValidatorUtils.notEmpty(gg.getRetailPrice())){
            g.setOriginalPrice(gg.getRetailPrice());
        }

        g.setSort(gg.getSortOrder());
        g.setSale(gg.getCategoryId());
        g.setStock(gg.getId());
        g.setLowStock(0);
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

        g.setProductAttributeCategoryId(gg.getCategoryId().longValue());
        productService.save(g);
    }
}
