package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoodsCateMap;
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
public interface EsShopGoodsCateMapMapper extends BaseMapper<EsShopGoodsCateMap> {

    List<EsShopGoodsCateMap> selEsShopGoodsCateMap(@Param("id") Long id);
}
