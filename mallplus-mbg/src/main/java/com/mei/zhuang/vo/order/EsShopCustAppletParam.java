package com.mei.zhuang.vo.order;

import com.mei.zhuang.entity.order.EsShopCustomizedApplet;
import lombok.Data;

@Data
public class EsShopCustAppletParam extends EsShopCustomizedApplet {

    //规格id
    private Long optionId;
    private Integer total;
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
