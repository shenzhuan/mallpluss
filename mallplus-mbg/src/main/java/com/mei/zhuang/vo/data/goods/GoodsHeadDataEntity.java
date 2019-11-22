package com.mei.zhuang.vo.data.goods;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 17:29
 * @Description:商品分析头部数据（商品分布+ 商品概述）
 */
@Data
public class GoodsHeadDataEntity {

    private int onSaleCount;//在售商品数

    private int kuCount;//仓库商品数

    private int saleOutCount;//售馨商品数

    private int saleTotalCount;//商品总销量

    private double payTotaAmount;//支付总金额

    private int refundCount;//退款数量

    private double refundTotalAmount;//退款总金额

    private ScaleData scaleData;//比例数据

    private Boolean setIsDisplayBeforeData = false;//是否显示比例数据 false：不显示 true：显示


}
