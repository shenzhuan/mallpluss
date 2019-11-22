package com.mei.zhuang.vo.data.goods;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 17:29
 * @Description:商品排行明细Vo类
 */
@Data
public class GoodsRankTopDetailVo {


    private String goodsNameAndOption;//商品名称

    private String thumb;//商品图片

    private int payCount;//支付数量(销量)

    private double totalPayAmount;//该件商品总的支付金额

    private int paiedPeopleCount;//复购人数

    private int goodsUV;//商品访客数

    private int gooodsPU;//商品浏览量


}
