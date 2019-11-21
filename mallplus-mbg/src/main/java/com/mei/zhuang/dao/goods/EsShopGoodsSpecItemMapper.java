package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoodsSpecItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-04-16
 */
public interface EsShopGoodsSpecItemMapper extends BaseMapper<EsShopGoodsSpecItem> {

    EsShopGoodsSpecItem selectSpecItem(@Param("goodsId") Long goodsId, @Param("id") Long id);

    List<EsShopGoodsSpecItem> selectSpecItems(@Param("goodsId") Long goodsId, @Param("specId") Long specId);

    Integer updates(@Param("id") Long id, @Param("title") String title);

    List<EsShopGoodsSpecItem> selectSpecItemsGoodsId(@Param("goodsId") Long goodsId);
}
