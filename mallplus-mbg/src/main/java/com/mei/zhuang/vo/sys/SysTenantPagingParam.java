package com.mei.zhuang.vo.sys;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysTenantPagingParam extends PagingParam {

    @ApiModelProperty("租户名称")
    private String tenantName;
    @ApiModelProperty("租户数据源id")
    private Integer dbResourceId;
    @ApiModelProperty("租户schema")
    private String dbSchema;

}
