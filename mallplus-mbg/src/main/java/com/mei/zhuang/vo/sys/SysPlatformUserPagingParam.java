package com.mei.zhuang.vo.sys;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysPlatformUserPagingParam extends PagingParam {

    @ApiModelProperty("账号")
    private String username;
    @ApiModelProperty("管理的租户")
    private Integer manageTenantId;

}
