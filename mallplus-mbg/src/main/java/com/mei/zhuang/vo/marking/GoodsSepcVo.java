package com.mei.zhuang.vo.marking;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/6 11:28
 * @Description:
 */
@Data
public class GoodsSepcVo implements Serializable {

    private Long goodsId;
    // 规格IDS
    private String specIds;
    private Long specId;
    private String goodsName;
    private String pic;
    /**
     * 首购礼
     * 类型1 第一单获取 2 所有订单获取
     */
    private Integer type;
    /**
     * 商品的spu或者sku的价格
     */
    private BigDecimal price;
    private Integer total ;
    public GoodsSepcVo(){}
    public GoodsSepcVo(Long goodsId,Long specId){
        this.goodsId = goodsId;
        this.specId = specId;
    }
}
