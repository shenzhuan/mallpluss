package com.mei.zhuang.vo.data.goods;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 17:58
 * @Description:商品销量排行Vo
 */
@Data
public class GoodsRankTopSaleVo {

    private int top;//排名

    private int saleCount;//销量

    private String thumb;//商品图片

    private String goodsNameAndOption;//商品名称

    public GoodsRankTopSaleVo() {
    }


}
