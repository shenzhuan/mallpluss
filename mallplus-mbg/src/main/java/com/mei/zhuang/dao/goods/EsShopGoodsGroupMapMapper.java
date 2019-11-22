package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoodsGroupMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-16
 */
public interface EsShopGoodsGroupMapMapper extends BaseMapper<EsShopGoodsGroupMap> {

    /**
     * 查询分组推荐商品
     *
     * @param id
     * @return
     */
    List<EsShopGoodsGroupMap> selEsShopGoodsGroupMap(@Param("id") Long id);
}
