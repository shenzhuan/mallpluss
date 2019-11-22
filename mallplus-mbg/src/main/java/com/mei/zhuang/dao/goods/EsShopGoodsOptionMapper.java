package com.mei.zhuang.dao.goods;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoodsOption;
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
public interface EsShopGoodsOptionMapper extends BaseMapper<EsShopGoodsOption> {

    int decrSkuStock(@Param("id") long id, @Param("count") int count);

    int addSkuStock(@Param("id") long id, @Param("count") int count);

    List<EsShopGoodsOption> selectOption(@Param("goodsId") Long goodsId);

    //  List<EsShopGoodsOption> selPageList(Pagination page, EsShopGoodsOption entity);

    EsShopGoodsOption selOption(EsShopGoodsOption option);

    List<EsShopGoodsOption> selectLikeSpecIds(@Param("id") Long specItemId);

    Integer deletes(@Param("id") Long goodsId);
}

