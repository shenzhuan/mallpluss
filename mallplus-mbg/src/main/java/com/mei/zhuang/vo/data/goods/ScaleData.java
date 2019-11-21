package com.mei.zhuang.vo.data.goods;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 17:29
 * @Description:商品分析头部比例数据
 */
@Data
public class ScaleData {


    private double saleTotCounToScale;//商品总销量前一日比例

    private double payTotAmountToScale;//支付总金额前一日比例

    private double refuCountToScale;//退款数量前一日比例

    private double refuTotaAmountToScale;//退款总金额前一日比例


    public ScaleData() {
    }

    public ScaleData(double saleTotCounToScale, double payTotAmountToScale, double refuCountToScale, double refuTotaAmountToScale) {
        this.saleTotCounToScale = saleTotCounToScale;
        this.payTotAmountToScale = payTotAmountToScale;
        this.refuCountToScale = refuCountToScale;
        this.refuTotaAmountToScale = refuTotaAmountToScale;
    }


}
