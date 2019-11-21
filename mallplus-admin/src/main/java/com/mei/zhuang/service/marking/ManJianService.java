package com.mei.zhuang.service.marking;

import com.arvato.ec.common.vo.marking.MjDcVo;
import com.arvato.ec.common.vo.order.CartMarkingVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopManjian;
import com.mei.zhuang.entity.marking.EsShopManjianGoodsMap;
import com.mei.zhuang.entity.marking.EsShopManjianRule;
import com.mei.zhuang.entity.order.EsShopCart;

import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
public interface ManJianService extends IService<EsShopManjian> {

    MjDcVo matchManjian(List<EsShopCart> cartList) throws Exception;
    boolean save(EsShopManjian entity) throws Exception;

    int updateShowStatus(Long ids, Integer status);

    Integer update(EsShopManjian entity) throws Exception;
    //商品明细查询
    List<EsShopManjianGoodsMap> selectgoodsid(Long manjianId);
    //优惠设置明细
    List<EsShopManjianRule> selectcouponid(Long couponid);

    //满减查询
    List<EsShopManjian> slelectMan();

    //删除关联id
    Integer deleteid(Long id);

    //删除编辑满额立减商品
    Integer deletegoodsid(Long id);


 /*   //周期活动时间
    Object dateworker(Long id) throws Exception;*/

    //状态数量
    Integer selectstatus();


    EsShopManjianRule isManJianUseAble(CartMarkingVo vo);
}
