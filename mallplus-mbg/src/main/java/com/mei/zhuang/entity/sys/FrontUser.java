package com.mei.zhuang.entity.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("登陆用户信息")
@Data
public class FrontUser {
    public String id;
    @ApiModelProperty("用户名")
    public String username;
    @ApiModelProperty("当前租户Logo")
    public String currTenantLogo;


    public String name;
    private String birthday;
    private String address;
    private String mobile;
    private String email;
    private String sex;
    private String description;
    private String image;
    private List<PermissionInfo> menus;
    private List<PermissionInfo> elements;

    @ApiModelProperty("管理的租户列表")
    private List<SysTenant> tenants;
}
