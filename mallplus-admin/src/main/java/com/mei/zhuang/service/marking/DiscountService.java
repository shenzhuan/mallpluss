package com.mei.zhuang.service.marking;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopDiscount;
import com.mei.zhuang.entity.marking.EsShopDiscountGoodsMap;
import com.mei.zhuang.entity.marking.EsShopDiscountRule;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.vo.marking.MjDcVo;
import com.mei.zhuang.vo.order.CartMarkingVo;

import java.text.ParseException;
import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
public interface DiscountService extends IService<EsShopDiscount> {
    boolean save(EsShopDiscount entity);

    int updateShowStatus(Long ids, Integer status);

    //商品明细查询
    List<EsShopDiscountGoodsMap> selectgoodsid(Long discountId);

    //优惠设置明细
    List<EsShopDiscountRule> selectcouponid(Long discountId);

    //折扣查询
    List<EsShopDiscount> slelectDiscount();

    Integer update(EsShopDiscount entity) throws Exception;

    //删除关联id
    Integer deleteid(Long id);

    //状态数量
    Integer selectstatus();

    MjDcVo matchDiscount(List<EsShopCart> cartList) throws ParseException;

    EsShopDiscountRule isDiscountRuleUseAble(CartMarkingVo vo);
}
