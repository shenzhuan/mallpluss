package com.mei.zhuang.vo.order;

import lombok.Data;

/**
 * 生成订单时传入的参数
 * https://github.com/shenzhuan/mallplus on 2018/8/30.
 */
@Data
public class BookOrderParam {
    //小程序推送跳转页面
    private String page;
    //小程序推送需要的formid
    private String formid;
    private String platform = "2";
    //收货地址id
    private Long addressId;
    //會員优惠券id
    private Long couponId;
    // 2折扣，1现金，3实物券 4赠品券
    private Integer couponType;
    private Long memberId;
    //使用的积分数
    private Integer useIntegration;
    //支付方式
    private Integer payType;
    // 0 送货 1 自取
    private Integer offline;
    private String content;
    //单个购物车id
    private String cartId;
    //多个购物车ids
    private String cartIds;
    // 1 商品详情 2 勾选购物车 3全部购物车的商品 4.好友赠礼
    private String type;

    private String addressDetail;
    private String addressInfo;
    private String buyerName;//收货人姓名
    private String addressProvince;
    private String addressArea;
    private String remarkBuyer;
    private String addressCity;
    private String buyerMobile; //收货人电话

    private String remark;//买家留言

    // 商品规格id
    private Long optionId;
    private Integer total;
    private Long goodsId;
    private Long shopId=1l;
    private long giftId;
    // 满减
    private Long manjianId;
    // 折扣
    private Long discountId;
    //  //首赠礼 规则ids
    private String firstPurchaseId;
    // //满赠礼 规则ids
    private String fullGiftId;
    //选赠礼规格ids
    private String ChoosGiftId;

    // 单品礼赠 规则ids
    private String singleGiftIds;
    // 单品礼赠 数量
    private Integer singleCount;
    // 验证码兑换的商品表id
    private String codeGiftIds;
    private String code;
    /**
     * 规格选项的类型（1-文字、2-颜色、3-图片）
     */
    private Integer typeOption;

    /**
     * 规格选项的内容（和type一一对应）
     */
    private String typeword;


    private Long orderId;//促销代码赠礼用


}
