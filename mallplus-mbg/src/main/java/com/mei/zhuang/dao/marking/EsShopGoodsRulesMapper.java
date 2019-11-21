package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopGoodsRules;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface EsShopGoodsRulesMapper extends BaseMapper<EsShopGoodsRules> {

    List<EsShopGoodsRules> lsitrules(@Param("goodsname") String goodsname);

    Integer updateRule(@Param("goodsname") String goodsname, @Param("goodsId") long goodsId);
}
