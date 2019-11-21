package com.mei.zhuang.vo.data.goods;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 17:59
 * @Description:商品支付金额Vo
 */
@Data
public class GoodsRankTopPayPriceVo {

    private int top;//排名

    private BigDecimal totalPayAmount= BigDecimal.valueOf(0.0);//该件商品总的支付金额

    private String thumb;//商品图片

    private String goodsNameAndOption;//商品名称



    public GoodsRankTopPayPriceVo() {
    }



}
