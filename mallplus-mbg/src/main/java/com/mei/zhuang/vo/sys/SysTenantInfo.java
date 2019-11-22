package com.mei.zhuang.vo.sys;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("租户信息")
@Data
@Accessors(chain = true)
public class SysTenantInfo {
    private int id;
    private String dbName;
    private String dbSchema;
}
