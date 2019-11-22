package com.mei.zhuang.vo.sys;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/21
 */
@Data
@Component
public class FeignTenantInfoDto {

    private String dbname;
    private String schema;
    private String username;
    private Integer currTenantId;
}
