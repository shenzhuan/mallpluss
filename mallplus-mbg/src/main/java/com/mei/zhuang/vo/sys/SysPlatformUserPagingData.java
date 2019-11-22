package com.mei.zhuang.vo.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysPlatformUserPagingData {
    private Integer id;
    @ApiModelProperty("账户")
    private String username;
    @ApiModelProperty("管理的租户")
    private String[] manageTenantNames;
    @ApiModelProperty("创建日期")
    private String createDate;
    @ApiModelProperty("状态")
    private String status;
}
