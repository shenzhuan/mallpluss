package com.mei.zhuang.vo.data.goods;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 17:44
 * @Description:商品趋势图Vo类
 */
@Data
public class GoodsTrendMapInfoVo{

    private String relationDate;//关系日期

    private int goodsSaleTotalCount;//商品总销售量

    private int goodsUvCount;//商品访问量


    public GoodsTrendMapInfoVo(){}

    public GoodsTrendMapInfoVo(String relaDate, int goodsSaleTotalCount, int goodsUvCount) {
        this.relationDate = relaDate;
        this.goodsSaleTotalCount = goodsSaleTotalCount;
        this.goodsUvCount = goodsUvCount;
    }


}
