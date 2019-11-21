package com.mei.zhuang.vo.data.trade;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: Tiger
 * @Date: 2019-06-27 10:07
 * @Description:交易数据前一日Vo
 */
@Data
public class TradeDataBeforeVo implements Serializable {

    private double payAmountBeforeOnePerc;//支付金额前一日比例

    private double payCountBeforeOnePerc;//支付订单数前一日比例

    private double payCountByPeopleBeforeOnePerc;//付款人数前一日比例

    private double actualPayCountBeforeOnePerc;//付款订单数前一日比例

    private double unitPriceByOneBeforeOnePerc;//客单价前一日比例

    private double refundPriceBeforeOnePerc;//退款订单金额前一日比例


    public TradeDataBeforeVo(){

    }

    public TradeDataBeforeVo(double payAmountBeforeOnePerc, double payCountBeforeOnePerc, double payCountByPeopleBeforeOnePerc, double actualPayCountBeforeOnePerc, double unitPriceByOneBeforeOnePerc, double refundPriceBeforeOnePerc) {
        this.payAmountBeforeOnePerc = payAmountBeforeOnePerc;
        this.payCountBeforeOnePerc = payCountBeforeOnePerc;
        this.payCountByPeopleBeforeOnePerc = payCountByPeopleBeforeOnePerc;
        this.actualPayCountBeforeOnePerc = actualPayCountBeforeOnePerc;
        this.unitPriceByOneBeforeOnePerc = unitPriceByOneBeforeOnePerc;
        this.refundPriceBeforeOnePerc = refundPriceBeforeOnePerc;
    }



}
