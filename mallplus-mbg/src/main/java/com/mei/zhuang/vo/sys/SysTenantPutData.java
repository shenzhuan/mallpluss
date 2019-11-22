package com.mei.zhuang.vo.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新租户数据传输实体
 */
@Data
public class SysTenantPutData {

    private Integer id;

    @ApiModelProperty("租户名称")
    private String tenantName;

    @ApiModelProperty("租户数据源id")
    private Integer dbResourceId;

    @ApiModelProperty("租户数据源id对应的schema")
    private String dbSchema;

    @ApiModelProperty("租户品牌名称")
    private String brandName;

    @ApiModelProperty("租户品牌Logo地址")
    private String brandLogo;

    @ApiModelProperty("租户公司名称")
    private String companyName;

    @ApiModelProperty("租户公司电话")
    private String companyTel;

    @ApiModelProperty("租户公司邮箱")
    private String companyEmail;

    @ApiModelProperty("租户公司地址")
    private String companyAddress;
}
