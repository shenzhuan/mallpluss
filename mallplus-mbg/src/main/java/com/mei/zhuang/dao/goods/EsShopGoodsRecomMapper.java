package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoodsRecom;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品详情的推荐商品 Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-03
 */


public interface EsShopGoodsRecomMapper extends BaseMapper<EsShopGoodsRecom> {

    //Integer insRecom(EsShopGoodsRecom entity);
    List<EsShopGoodsRecom> selRecomGoods(@Param("goodsId") Long goodsId);
}
