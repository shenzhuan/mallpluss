package com.mei.zhuang.vo.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新增平台账号数据传输实体
 */
@Data
public class SysPlatformUserPostData {

    @ApiModelProperty("账号")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("管理的租户id list")
    private String manageTenantIds;
}
