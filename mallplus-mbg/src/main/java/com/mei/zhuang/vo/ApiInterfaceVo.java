package com.mei.zhuang.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/4
 */
@ApiModel("用户接口实体")
@Data
public class ApiInterfaceVo implements Serializable {

    private Integer id;

    @ApiModelProperty(" 接口名称")
    private String name;

    @ApiModelProperty("接口类型名称")
    private String typeName;

    @ApiModelProperty("状态：启用 维护 废弃")
    private String status;

    @ApiModelProperty("相对请求地址")
    private String requestUrl;

    @ApiModelProperty("当前用户改接口的权限：true:拥有权限，false:没有权限")
    private boolean permission;
}
