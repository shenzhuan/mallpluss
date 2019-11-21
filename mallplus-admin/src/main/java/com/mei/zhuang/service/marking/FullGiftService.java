package com.mei.zhuang.service.marking;


import com.arvato.ec.common.vo.order.CartMarkingVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopFullGift;
import com.mei.zhuang.entity.marking.EsShopFullGiftGoodsMap;
import com.mei.zhuang.entity.order.EsShopCart;

import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
public interface FullGiftService extends IService<EsShopFullGift> {
    Integer save(EsShopFullGift entity) throws Exception;

    int updateShowStatus(Long ids, Integer status);

    List<EsShopFullGift> selectrule();

    boolean update(EsShopFullGift entity) throws Exception;

    //满赠商品明细
    List<EsShopFullGiftGoodsMap> selectgift(Long fullGiftId);
    //满赠赠品明细和优惠设置
    List<Map<String, Object>> selectgift2(Long fullGiftId);

   /* //优惠设置
    List<EsShopFullGiftRule> selectrule(Long fullGiftId);*/

    //满赠查询
    List<EsShopFullGift> slelectPurchase();
    //选赠礼
    List<EsShopFullGift> slelectPurchase2();

    Integer deleteid(Long id);

    //状态数量
    Integer selectstatus();

    Integer selectstatus2();

    List<EsShopFullGift> matchFullGift(List<EsShopCart> cartList) throws Exception;

    List<EsShopFullGift> ChooseFullGift(List<EsShopCart> cartList) throws Exception;

    List<EsShopFullGiftGoodsMap> isFullGiftGoodsUseAble(CartMarkingVo vo);
    List<EsShopFullGiftGoodsMap> isChooseGiftGoodsUseAble(CartMarkingVo vo);
}
