package com.mei.zhuang.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopBrand;
import com.mei.zhuang.entity.sys.EsShopNew;
import com.mei.zhuang.vo.ShopParam;

import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-05-18 10:42
 * @Description:
 */
public interface IShopService extends IService<EsShopNew> {


    Page<EsShopNew> selectPageExt(ShopParam entity);

    List<EsShopBrand> selectBrandList();

    EsShopNew selectById(Long id);

    boolean deleteById(Long id);

}
