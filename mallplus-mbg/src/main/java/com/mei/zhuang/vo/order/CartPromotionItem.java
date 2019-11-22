package com.mei.zhuang.vo.order;

import com.mei.zhuang.entity.goods.EsShopGoodsOption;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.entity.order.EsShopCustomizedApplet;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车中促销信息的封装
 */
@Data
public class CartPromotionItem {
    EsShopCart esShopCart;
    EsShopGoodsOption goodsOption;
    //商品定制服务
    EsShopCustomizedApplet esShopCustApplet;
    //促销活动信息
    private String promotionMessage;
    //促销活动减去的金额，针对每个商品
    private BigDecimal reduceAmount = BigDecimal.ZERO;
    //商品的真实库存（剩余库存-锁定库存）
    private Integer realStock;


}
