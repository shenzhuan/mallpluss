package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoodsCategoryRecom;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EsShopGoodsCategoryRecomMapper extends BaseMapper<EsShopGoodsCategoryRecom> {

    List<EsShopGoodsCategoryRecom> selCategoryRecom(@Param("categoryId") Long categoryId);

    Integer delete(@Param("categoryId") Long categoryId);
}


