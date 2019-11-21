package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopCouponShopping;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-15
 */
public interface EsShopCouponShoppingMapper extends BaseMapper<EsShopCouponShopping> {

    //购物发券联查
    List<Map<String,Object>>selectshopping();



    //修改满额状态
    Integer updatestatusid(Integer param1, Long param2);

}
