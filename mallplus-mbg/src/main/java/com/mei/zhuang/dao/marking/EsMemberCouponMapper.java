package com.mei.zhuang.dao.marking;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-04-15
 */
public interface EsMemberCouponMapper extends BaseMapper<EsMemberCoupon> {

    //修改用户的订单号
    public int lockCoupon(EsMemberCoupon coupon);

    public int useCoupon(long couponId);

    public int releaseCoupon(long couponId);

    public EsMemberCoupon selectCouponByOrderId(long orderId);

    public List<EsMemberCoupon> myCoupon(@Param("accountId") String accountId, @Param("type") Integer type);

    public List<EsMemberCoupon> unusedCouponList(String accountId);

    //查询所有为使用券的人
    List<EsMemberCoupon> selectstatus(String title);

    Integer namesum(String title, long typeId);

    //根据优惠券名称修改时间
    Integer updatecoupon(@Param("cou") EsMemberCoupon cou);

    //发放记录查询
    List<Map<String,Object>> selectMemberCoupon(@Param("coupon") EsMemberCoupon coupon);

    List<Map<String,Object>> selectMemberCoupon2();

    //有效数量
    EsMemberCoupon couponeff();
    //使用数量
    EsMemberCoupon couponsta();
    //用户数量与发券数量
    EsMemberCoupon couponmember();

    List<EsMemberCoupon> selectCountMax(EsMemberCoupon entity);

    Integer selectCounts(EsMemberCoupon entity);

}
