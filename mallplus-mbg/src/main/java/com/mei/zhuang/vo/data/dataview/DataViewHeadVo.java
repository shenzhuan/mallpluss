package com.mei.zhuang.vo.data.dataview;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-08-08 14:36
 * @Description:
 */
@Data
public class DataViewHeadVo {

    private int payCountByPeople;//    付款人数

    private int payCount;    //付款订单数

    private double trasPayScale;//付款转化率

    private double memberPrice;//客单价


}
