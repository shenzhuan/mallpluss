package com.mei.zhuang.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopCouponShopping;
import com.mei.zhuang.entity.marking.EsShopCouponsTopupGoods;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  购物发券
 * </p>
 *
 * @author arvato team
 * @since 2019-05-15
 */
public interface  CouponShoppingService extends IService<EsShopCouponShopping> {
    boolean save(EsShopCouponShopping esShopCouponShopping) ;

    boolean update(EsShopCouponShopping entity) throws Exception;

    //删除购物发券
    Integer deletecouponid(long id);
    //购物发券联查
    List<Map<String,Object>> selectshopping();

    EsShopCouponShopping  selectshopid(long id);
    //商品查询
    List<EsShopCouponsTopupGoods> goodsList(long id);

    //修改购物发券状态
    Integer updatestatusid(Integer activitiesOpen, Long id);

}
