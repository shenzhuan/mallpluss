package com.mei.zhuang.dao.marking;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopCodeGiftRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-02
 */
public interface EsShopCodeGiftRuleMapper extends BaseMapper<EsShopCodeGiftRule> {

    void updateCodeStatus(@Param("code") String code, @Param("status") Integer status);

    //查询验证码
    List<EsShopCodeGiftRule> codelist(long codeGiftId);

    //唯一验证
    EsShopCodeGiftRule codegif(long codeGiftId);

    EsShopCodeGiftRule codegif2(String code);
}
