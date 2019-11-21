package com.mei.zhuang.vo.data.trade;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-27 14:13
 * @Description:交易分析vo类
 */
@Data
public class OrderCustTotalVo {


    private int newClientCount;//新客户数量

    private int oldClientCount;//老客户数量

    private double newConsumeAmount;//新客户消费金额

    private double oldConsumeAmount;//老客户消费金额

    private double total;//总金额

    private int count;//总数量


    private String relationEndDate;//选中日期内的数据  取截止日期





}
