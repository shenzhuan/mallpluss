package com.mei.zhuang.vo.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新平台账号数据传输实体
 */
@Data
public class SysPlatformUserPutData {
    private Integer id;
    @ApiModelProperty("管理的租户id list")
    private String manageTenantIds;
}
