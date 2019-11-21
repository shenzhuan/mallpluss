package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoodsCategoryAdvertimg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EsShopGoodsCategoryAdvertimgMapper extends BaseMapper<EsShopGoodsCategoryAdvertimg> {

    List<EsShopGoodsCategoryAdvertimg> selEsShopGoodsCategoryAdvertimg(@Param("id") Long id);

    Integer delete(@Param("categoryId") Long categoryId);

}
