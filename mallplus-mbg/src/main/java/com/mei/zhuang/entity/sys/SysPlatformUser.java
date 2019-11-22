package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author meizhuang team
 * @since 2019-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_platform_user")
public class SysPlatformUser extends Model<SysPlatformUser> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String password;
    @TableField("last_login_time")
    private String lastLoginTime;
    @TableField("login_error_count")
    private Integer loginErrorCount;
    /**
     * 管理的租户，租户id数组
     */
    @TableField(value = "manage_tenant_ids")
    private String manageTenantIds;
    /**
     * 0=租户管理员
     * 1=普通用户
     */
    @TableField("user_type")
    private String userType;
    /**
     * 0=启用，1=禁用
     */
    @TableField
    private String status;


    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private String createDate;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    private String updateDate;
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private String updateTime;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
