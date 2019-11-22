package com.mei.zhuang.service.marking;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopGoodsRules;
import com.mei.zhuang.entity.marking.EsShopGoodsRulesSpec;
import com.mei.zhuang.entity.order.EsShopCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
public interface RulesSpecService extends IService<EsShopGoodsRules> {
    boolean save(EsShopGoodsRules entity);

    Integer deleterule(String id);

    EsShopGoodsRules detailrule(long id);


    //商品规则明细
    List<EsShopGoodsRulesSpec> listrulesspec(long rulesId);

    boolean update(EsShopGoodsRules entity) throws Exception;

    //规则商品查询
    List<EsShopGoodsRules> lsitrules(String goodsname);


    EsShopGoodsRules matchGoodsRules(List<EsShopCart> cartList);

    //商品同步
    Integer delete(@Param("goodsId") long goodsId, @Param("according") int according);

    Integer updateRule(@Param("goodsname") String goodsname, @Param("goodsId") long goodsId);
}
