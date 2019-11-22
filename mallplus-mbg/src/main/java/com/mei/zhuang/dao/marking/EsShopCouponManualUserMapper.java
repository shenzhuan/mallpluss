package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopCouponManualUser;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-18
 */
public interface EsShopCouponManualUserMapper extends BaseMapper<EsShopCouponManualUser> {


    Integer updateuser(String param1, long param2);


}
