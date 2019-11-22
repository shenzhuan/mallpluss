package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopDiscount;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-02
 */
public interface EsShopDiscountMapper extends BaseMapper<EsShopDiscount> {

    void stopAllDiscount();

    Integer selectstatus();

}
