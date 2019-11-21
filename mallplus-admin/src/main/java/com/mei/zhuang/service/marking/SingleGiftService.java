package com.mei.zhuang.service.marking;



import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopSingleGift;
import com.mei.zhuang.entity.marking.EsShopSingleGiftGoodsMap;
import com.mei.zhuang.entity.marking.EsShopSingleGiftRule;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.vo.order.CartMarkingVo;

import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
public interface SingleGiftService extends IService<EsShopSingleGift> {
    boolean save(EsShopSingleGift entity);

    int updateShowStatus(Long ids, Integer status);

    boolean update(EsShopSingleGift entity) throws Exception;

    //查询规则
    List<EsShopSingleGiftRule>selectsing(Long singleGiftId);

    //单品和多组商品查询
    Map<String,Object> selectsinggoods(Long singleGiftId);


    //赠品商品查询
    List<EsShopSingleGiftGoodsMap> selectsinggoods3(Long singleGiftId);
    Integer deleteid(Long id);

    //单品查询
    List<EsShopSingleGift> slelectPurchase();

    List<EsShopSingleGift> matchSingleGift(List<EsShopCart> cartList) throws Exception ;

    List<EsShopSingleGiftGoodsMap> isSingleGiftUseAble(CartMarkingVo vo);
}
