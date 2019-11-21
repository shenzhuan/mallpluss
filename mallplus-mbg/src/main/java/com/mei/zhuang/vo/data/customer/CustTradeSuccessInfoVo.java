package com.mei.zhuang.vo.data.customer;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 13:45
 * @Description:成交客户分析
 */
@Data
public class CustTradeSuccessInfoVo {

    private String custTypeEn;//客户类型汉译
    private Integer custType;//1,全部客户  2. 新客户 3. 老客户

    private int tradeSuccessCount;//成交客户数

    private double custCountToScale;//客户数占比

    private int payCount;//支付订单数 （后台支付 + 实际付款数）

    private BigDecimal unitPriceByOne = BigDecimal.valueOf(0.0);//客单价

    private BigDecimal payAmount = BigDecimal.valueOf(0.0);//支付金额

    private double payAmountToScale;//支付金额占比

    private String refDate;//关系日期

    public CustTradeSuccessInfoVo() {
    }

    public CustTradeSuccessInfoVo(String custTypeEn, Integer custType, int tradeSuccessCount, double custCountToScale, int payCount, BigDecimal unitPriceByOne, BigDecimal payAmount, double payAmountToScale) {
        this.custTypeEn = custTypeEn;
        this.custType = custType;
        this.tradeSuccessCount = tradeSuccessCount;
        this.custCountToScale = custCountToScale;
        this.payCount = payCount;
        this.unitPriceByOne = unitPriceByOne;
        this.payAmount = payAmount;
        this.payAmountToScale = payAmountToScale;
    }
}
