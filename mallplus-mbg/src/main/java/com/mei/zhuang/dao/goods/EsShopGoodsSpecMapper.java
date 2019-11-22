package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoodsSpec;
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
public interface EsShopGoodsSpecMapper extends BaseMapper<EsShopGoodsSpec> {

    List<EsShopGoodsSpec> detail(@Param("id") Long id);

}
