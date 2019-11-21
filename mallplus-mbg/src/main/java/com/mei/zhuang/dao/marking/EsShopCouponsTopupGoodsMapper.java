package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopCouponsTopupGoods;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-13
 */
public interface EsShopCouponsTopupGoodsMapper extends BaseMapper<EsShopCouponsTopupGoods> {

    //商品通用删除
    Integer deletegoodsid(Long param1, String param2);

}
