package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-07-04 11:11
 * @Description:客户交易留存分析
 */
@Data
public class CustTradeRemainAnalVo {

    private String refDate;//关系日期

    private int newTradedCount;//新成交客户数

    private double remainScale;//留存率
}
