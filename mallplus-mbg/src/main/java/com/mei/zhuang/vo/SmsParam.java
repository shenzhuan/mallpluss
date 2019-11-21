package com.mei.zhuang.vo;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-05-30 14:48
 * @Description:
 */
@Data
public class SmsParam {

    private String startTime;
    private String endTime;
    private Long serverId;//服务商名称
    private Integer status;//状态
    private String smsName;//模板名称

    private Integer current = 1;
    private Integer size = 10;
    private Integer isAsc = 0;
    private Integer isDelete = 0;

}
