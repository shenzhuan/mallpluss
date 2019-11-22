package com.mei.zhuang.service.marking;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopCoupon;
import com.mei.zhuang.entity.marking.EsShopCouponGoodsMap;
import com.mei.zhuang.entity.marking.EsShopCouponRule;

import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
public interface CouponService extends IService<EsShopCoupon> {

    Integer save(EsShopCoupon entity, EsShopCouponRule rule) throws Exception;

    boolean update(EsShopCoupon entity, EsShopCouponRule rule) throws Exception;

    //删除指定添加的商品
    Integer deleteid(Long id);

    //删除所有优惠券
    Integer detetecouponid(Long id);

    //优惠券的状态修改
    Integer updatestatus(Long id, String statusOpen);

    //优惠券选择
    List<EsShopCoupon> selectcoupon(EsShopCoupon esShopCoupon);

    //优惠券管理面显示
    Map<String, Object> selectmapcoupon(EsShopCoupon esShopCoupon) throws Exception;

    //优惠券明细
    List<Map<String, Object>> couponlimit(Long id);

    //商品明细
    List<EsShopCouponGoodsMap> selectgoods(Long couponId);

    //1.实物商品
    List<EsShopCouponGoodsMap> selectgoods2(Long couponId);

    //赠品券商品查询
    List<EsShopCouponGoodsMap> selcetcoupongoods(Long couponId);

    //优惠券批量查询
    List<EsShopCoupon> couponbatch(String id);


}
