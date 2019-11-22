package com.mei.zhuang.dao.marking;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopCoupon;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-04-13
 */
public interface EsShopCouponMapper extends BaseMapper<EsShopCoupon> {
    //优惠券选择
    List<EsShopCoupon> selectcoupon(EsShopCoupon esShopCoupon);

    //优惠券管理面显示
    List<Map<String, Object>> selectmapcoupon(EsShopCoupon esShopCoupon);

    //优惠券明细
    List<Map<String, Object>> couponlimit(Long id);

    //查询开启的数量
    Integer selectstatus();

    //优惠券的状态修改
    Integer updatestatus(Long id, String statusOpen);

    //定時器查詢时间
    List<EsShopCoupon> coupontime();
}
