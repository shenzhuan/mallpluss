package com.mei.zhuang.service.sys;

import com.arvato.admin.vo.ShopParam;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.plugins.Page;
import com.mei.zhuang.entity.sys.EsShopBrand;
import com.mei.zhuang.entity.sys.EsShopNew;

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
