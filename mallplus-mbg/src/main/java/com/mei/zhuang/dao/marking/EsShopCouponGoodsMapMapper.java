package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopCouponGoodsMap;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-04-28
 */
public interface EsShopCouponGoodsMapMapper extends BaseMapper<EsShopCouponGoodsMap> {
        //2.限制使用的商品
    List<EsShopCouponGoodsMap> selectgoods(Long couponId);
    //1.实物明细商品
    List<EsShopCouponGoodsMap> selectgoods2(Long couponId);
    //赠品券商品明细查询
    List<EsShopCouponGoodsMap> selcetcoupongoods(Long couponId);

    //用于优惠券的赠品与商品的添加
    List<EsShopCouponGoodsMap> couponList(Long param1, Integer param2);

    EsShopCouponGoodsMap selectgoods3(Long couponId);
}
