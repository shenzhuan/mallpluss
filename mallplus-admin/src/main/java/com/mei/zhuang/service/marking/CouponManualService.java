package com.mei.zhuang.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopCouponManual;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-18
 */
public interface CouponManualService extends IService<EsShopCouponManual> {
    boolean save(EsShopCouponManual esShopCouponManual);

    Integer deleteManual(long id);

    boolean update(EsShopCouponManual esShopCouponManual) throws Exception;

    //状态 1 已完成，2 发送中 3，未开始
    //手工发券状态修改
    Integer updatestatus(long id, Integer statusissue);

    //手工明细查询
    EsShopCouponManual ManualList(long id);

    //手工发券查询
    List<Map<String, Object>> selectmanual(EsShopCouponManual esShopCouponManual) throws Exception;

    // void sendManualCoupon(CartMarkingVo vo) throws ParseException;

}
