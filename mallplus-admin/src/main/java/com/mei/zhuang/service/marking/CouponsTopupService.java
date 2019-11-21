package com.mei.zhuang.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopCouponsTopup;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author arvato team
 * @since 2019-05-13
 */
public interface CouponsTopupService extends IService<EsShopCouponsTopup> {
    boolean save(EsShopCouponsTopup entity) throws  Exception;
    boolean update(EsShopCouponsTopup entity) throws Exception;
    //商品通用删除
    Integer deletegoodsid(Long arg0, String arg1);

    //删除满额发券
    Integer deletecouponid(long id);

    //查询满额发券明细
    EsShopCouponsTopup selectTopupid(long id);

    //满额查询
    List<Map<String,Object>> selectTopup(EsShopCouponsTopup Topup);

    //修改满额状态
    Integer updatestatusid(Integer activitiesOpen, Long id);
}
