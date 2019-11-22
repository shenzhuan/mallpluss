package com.mei.zhuang.dao.marking;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopManjianRule;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-02
 */
public interface EsShopManjianRuleMapper extends BaseMapper<EsShopManjianRule> {
    Integer selectcouponid(Long couponid, Integer typeid);
}
