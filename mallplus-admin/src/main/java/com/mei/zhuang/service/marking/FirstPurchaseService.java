package com.mei.zhuang.service.marking;


import com.mei.zhuang.vo.order.CartMarkingVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopFirstPurchase;
import com.mei.zhuang.entity.marking.EsShopFirstPurchaseGoodsMap;
import com.mei.zhuang.entity.marking.EsShopFirstPurchaseRule;

import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
public interface FirstPurchaseService extends IService<EsShopFirstPurchase> {
    boolean save(EsShopFirstPurchase entity) ;

    int updateShowStatus(Long ids, Integer status);

    boolean update(EsShopFirstPurchase entity) throws Exception;

    Integer deleteid(Long id);
    //商品明细查询
    List<EsShopFirstPurchaseGoodsMap> selectgoodsid(Long firstPurchaseId);
    //赠品明细查询
    List<EsShopFirstPurchaseRule> selectcouponid(Long firstPurchaseId);
    //删除关联id

    //删除编辑满额立减商品
    Integer deletegoodsid(Long id);
    //赠品删除
    Integer deletegiftid(Long firstPurchaseId);

    //首购查询
    List<EsShopFirstPurchase> slelectPurchase();

    List<EsShopFirstPurchaseRule> matchFirstPurchase(CartMarkingVo vo) throws Exception ;

    List<EsShopFirstPurchaseRule> isFirstPurchaseUseAble(CartMarkingVo vo);
}
