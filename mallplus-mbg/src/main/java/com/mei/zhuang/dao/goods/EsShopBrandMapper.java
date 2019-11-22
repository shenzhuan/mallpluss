package com.mei.zhuang.dao.goods;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopBrand;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-16
 */
public interface EsShopBrandMapper extends BaseMapper<EsShopBrand> {

    Integer addbrand(EsShopBrand esShopBrand);


    //修改品牌
    Integer updatebrand(EsShopBrand esShopBrand);

    //删除品牌
    Integer deletebrand(Long brandId);

    //查询明细
    EsShopBrand selectbrandid(Long branfId);
}
