package com.mei.zhuang.vo.data.goods;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-08-05 16:35
 * @Description:
 */
@Data
public class GoodsVisitInfo {

    private int goodsUV;//商品访客数

    private int goodsPV;//商品浏览量


    public GoodsVisitInfo() {

    }

    public GoodsVisitInfo(int goodsUV, int goodsPV) {
        this.goodsUV = goodsUV;
        this.goodsPV = goodsPV;
    }



}
