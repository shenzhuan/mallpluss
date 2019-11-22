package com.mei.zhuang.vo.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysTenantPagingData {
    private Integer id;
    @ApiModelProperty("租户名称")
    private String tenantName;
    @ApiModelProperty("租户数据源")
    private String dbResourceName;
    @ApiModelProperty("租户schema")
    private String dbSchema;
    @ApiModelProperty("创建日期")
    private String createDate;
    @ApiModelProperty("状态")
    private String status;
}
