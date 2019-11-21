package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopCouponNew;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-13
 */
public interface EsShopCouponNewMapper extends BaseMapper<EsShopCouponNew> {

    List<EsShopCouponNew> couponnew(Integer vouchersNode);

    //新人明细
    EsShopCouponNew listcouponnew();
}
