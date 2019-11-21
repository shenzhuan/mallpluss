package com.mei.zhuang.vo.order;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-21 16:02
 * @Description:
 */
@Data
public class EsCoreLogParam {
    private String startTime;
    private String endTime;
    private Integer operationTypeId; //1.商品管理  2.订单管理 3.店铺运营 4.营销管理 5.货架管理 6.数据中心 7.系统设置
    private String keyword;//店员


    private Integer current = 1;
    private Integer size = 10;
    private Integer isAsc = 0;


}
