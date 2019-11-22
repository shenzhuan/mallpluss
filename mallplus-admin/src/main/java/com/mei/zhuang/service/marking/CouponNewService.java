package com.mei.zhuang.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopCouponNew;

public interface CouponNewService extends IService<EsShopCouponNew> {
    boolean save(EsShopCouponNew entity) ;
    boolean update(EsShopCouponNew esShopCouponNew) throws Exception;

    //新人发券的查询
    EsShopCouponNew listcouponnew();

    //通用优惠券删除
    Integer deletetypeid(Long couponid, String typeid);


}
