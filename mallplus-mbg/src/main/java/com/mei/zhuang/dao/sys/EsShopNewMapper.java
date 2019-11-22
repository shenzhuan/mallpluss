package com.mei.zhuang.dao.sys;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.EsShopNew;
import com.mei.zhuang.vo.ShopParam;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-29
 */
public interface EsShopNewMapper extends BaseMapper<EsShopNew> {

    List<EsShopNew> selectShopListByParam(ShopParam shopParam);

/*
    EsShopNew selectById(Long id);
*/

    Integer selectShopCountByParam(ShopParam shopParam);

}
