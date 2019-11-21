package com.mei.zhuang.vo.order;

import lombok.Data;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/21 16:21
 * @Description:
 */
@Data
public class CartParam {
    // 单个购物车id
    private Long cartId;
    //规格id
    private Long optionId;
    private Long goodsId;
    private Integer total;
    private Long memberId;
    // 多个购物车id
    private String ids;
    private Integer isSelected; // 1 选中 2 未选中
    /**
     * 规格选项的类型（1-文字、2-颜色、3-图片）
     */
    private Integer typeOption;

    /**
     * 规格选项的内容（和type一一对应）
     */
    private String typeword;
}
