package com.mei.zhuang.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopCouponGoodsMap;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.vo.marking.AllMemberCoupon;
import com.mei.zhuang.vo.order.CartMarkingVo;
import com.mei.zhuang.vo.order.CouponFilterParam;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/27 15:21
 * @Description:
 */
public interface MemberCouponService extends IService<EsMemberCoupon> {

     public List<EsShopCouponGoodsMap>  selectSendCouponGift(Long couponId) ;
     public AllMemberCoupon selectUserMemberCouponList(Long memberId) throws ParseException ;
     boolean lockCoupon(EsMemberCoupon coupon) ;

     void useCoupon(long orderId) ;

     void releaseCoupon(long orderId) ;

     public boolean isCouponUsable(EsMemberCoupon coupon, CouponFilterParam condition) ;

     public void shareCouponDiscount(EsMemberCoupon coupon, List<EsShopOrderGoods> orderItemList) ;

  //   ExcelExportUtil BySchemeId(String sheetTitle) throws IOException;

     //发放记录查询
     List<Map<String,Object>> selectMemberCoupon(EsMemberCoupon coupon) throws ParseException;

     List<Map<String,Object>> selectMemberCoupon2();
     //发放记录添加
     Object saveadd(Long memberId, Integer node);

     //发放数量显示
     Map<String,Object> record();


     //修改用户券库
     boolean updatecoupon(EsMemberCoupon updaecoupon);

     List<EsMemberCoupon> selectUserMemberCoupon(CartMarkingVo vo) throws ParseException;

     BigDecimal selectUserCouponById(CartMarkingVo vo);

    void sendFillFillCoupon(CartMarkingVo vo);

     void sendShopCoupon(CartMarkingVo vo);
     public void sendManualCoupon(CartMarkingVo vo) throws ParseException ;
     //查询奖品单日发放数量
     List<EsMemberCoupon> selectCountMax(EsMemberCoupon entity);

}
