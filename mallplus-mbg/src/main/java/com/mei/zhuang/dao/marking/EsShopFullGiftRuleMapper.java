package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopFullGiftRule;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-02
 */
public interface EsShopFullGiftRuleMapper extends BaseMapper<EsShopFullGiftRule> {

    List<EsShopFullGiftRule> selectfullgif(long fullGiftId);

    List<EsShopFullGiftRule> selectgif(long fullGiftId);


}
