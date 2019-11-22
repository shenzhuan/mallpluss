package com.mei.zhuang.vo.sys;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-07-09 9:32
 * @Description:系统日志参数
 */
@Data
public class SysOpertionLogParam {

    private String startTime;
    private String endTime;
//    private Integer operationTypeId; //1.商品管理  2.订单管理 3.店铺运营 4.营销管理 5.货架管理 6.数据中心 7.系统设置
    private String operationTypeName;//操作类型名称

    private String keyword;//店员

    private Integer current = 1;
    private Integer size = 10;
    private Integer isAsc = 0;



}
