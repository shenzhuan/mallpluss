package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopCouponNewRule;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-13
 */
public interface EsShopCouponNewRuleMapper extends BaseMapper<EsShopCouponNewRule> {

    //根据发券的类型id删除对应的优惠券id
    Integer deletetypeid(Long arg0, String arg1);

    //首次下单
    List<EsShopCouponNewRule> listrule(Integer vouchers);


}
