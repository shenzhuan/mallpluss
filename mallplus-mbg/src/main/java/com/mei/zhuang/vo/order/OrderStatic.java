package com.mei.zhuang.vo.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/15 22:16
 * @Description:
 */
@Data
public class OrderStatic {
    private String date;
    private BigDecimal payTotal;
    private int payCount;

    public OrderStatic() {
    }

    public OrderStatic(String date, BigDecimal payTotal, int payCount) {
        this.date = date;
        this.payTotal = payTotal;
        this.payCount = payCount;
    }
}
