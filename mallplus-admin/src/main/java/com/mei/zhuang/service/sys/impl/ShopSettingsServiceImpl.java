package com.mei.zhuang.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.sys.EsShopBrandMapper;
import com.mei.zhuang.dao.sys.EsShopNewMapper;
import com.mei.zhuang.entity.sys.EsShopBrand;
import com.mei.zhuang.entity.sys.EsShopNew;
import com.mei.zhuang.service.sys.IShopService;
import com.mei.zhuang.util.ShopTypeCharacterUtils;
import com.mei.zhuang.vo.ShopParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-05-18 10:43
 * @Description:
 */
@Service
public class ShopSettingsServiceImpl extends ServiceImpl<EsShopNewMapper, EsShopNew> implements IShopService {

    @Resource
    private EsShopNewMapper shopMapper;
    @Resource
    private EsShopBrandMapper shopBrandMapper;


    @Override
    public Page<EsShopNew> selectPageExt(ShopParam entity) {
        Page<EsShopNew> page = new Page<>(entity.getCurrent(), entity.getSize());
        List<EsShopNew> list = shopMapper.selectShopListByParam(entity);
        for(EsShopNew item : list){
            item.setTypeEn(ShopTypeCharacterUtils.getChByShopType(item.getTypeId()));
        }

       // page.setAsc(entity.getIsAsc() == 0 ? false : true);
        page.setRecords(list);
        page.setTotal(shopMapper.selectShopCountByParam(entity));
        return page;
    }

    @Override
    public List<EsShopBrand> selectBrandList() {
        return shopBrandMapper.selectList(new QueryWrapper<>(new EsShopBrand()));
    }

    @Override
    public EsShopNew selectById(Long id) {
        EsShopNew shopNew = shopMapper.selectById(id);
        shopNew.setShopBrand(shopBrandMapper.selectById(shopNew.getBrandId()));
        return shopNew;
    }

    @Override
    public boolean deleteById(Long id) {
        EsShopNew shop = new EsShopNew();
        shop.setIsDelete(1);
        shop.setId(id);
        return this.updateById(shop);
    }


}
