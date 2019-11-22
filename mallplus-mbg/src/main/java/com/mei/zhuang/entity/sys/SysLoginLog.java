package com.mei.zhuang.entity.sys;

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
@TableName("sys_login_log")
public class SysLoginLog extends Model<SysLoginLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 登录用户名
     */
    @TableField("login_name")
    private String loginName;
    /**
     * 登录状态 0：用户不存在 1：密码错误 2：账号已锁定 3：未分配权限 4：登录成功
     */
    @TableField("login_result")
    private Integer loginResult;
    /**
     * 登录结果描述
     */
    @TableField("login_desc")
    private String loginDesc;
    /**
     * 登录时间
     */
    @TableField("login_time")
    private String loginTime;
    /**
     * 解锁标识
     * 0：密码错误时未解锁
     * 1：用户已解锁标识，用于用户解锁后重新统计密码错误次数
     */
    @TableField("unlock_flag")
    private Integer unlockFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
