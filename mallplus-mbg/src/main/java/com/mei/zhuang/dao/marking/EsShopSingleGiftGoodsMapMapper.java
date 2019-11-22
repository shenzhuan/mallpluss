package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopSingleGiftGoodsMap;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-02
 */
public interface EsShopSingleGiftGoodsMapMapper extends BaseMapper<EsShopSingleGiftGoodsMap> {

    //单品商品查询
    List<EsShopSingleGiftGoodsMap> selectsinggoods1(Long singleGiftId);

    //组合商品查询
    List<EsShopSingleGiftGoodsMap> selectsinggoods2(Long singleGiftId);

    //赠品商品查询
    List<EsShopSingleGiftGoodsMap> selectsinggoods3(Long singleGiftId);
}
