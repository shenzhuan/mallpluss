package com.mei.zhuang.vo.marking;

import com.mei.zhuang.entity.order.EsMemberCoupon;
import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2019/7/8.
 */
@Data
public class AllMemberCoupon {
    //未使用 已使用 已过期
    private List<EsMemberCoupon> noCouponList;
    private List<EsMemberCoupon> alCouponList;
    private List<EsMemberCoupon> exCouponList;
}
