package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopFullGiftGoodsMap;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-02
 */
public interface EsShopFullGiftGoodsMapMapper extends BaseMapper<EsShopFullGiftGoodsMap> {
    //满赠商品明细
    List<EsShopFullGiftGoodsMap> selectgift(Long fullGiftId);
    //满赠赠品明细
    List<EsShopFullGiftGoodsMap> selectgift2(Long fullGiftId);

    List<EsShopFullGiftGoodsMap> slecetgoods(long ruleId);
}
