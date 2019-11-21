package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.*;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@ApiModel("用户类")
@Setter
@Getter
@Accessors(chain = true)
@TableName("crm_sys_user")
public class CrmSysUser extends Model<CrmSysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 用户id
     */
    @ApiModelProperty("主键 用户id")
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;
    /**
     * 生日
     */
    @ApiModelProperty("生日")
    private String birthday;
    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;
    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String mobile;
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;
    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String sex;
    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private String status;

    /**
     * 所属部门ID
     */
    @ApiModelProperty("所属部门ID")
    @TableField(value = "dept_id")
    private Integer deptId;

    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    @TableField(value = "last_login_time")
    private String lastLoginTime;

    /**
     * 连续密码输入错误次数
     */
    @ApiModelProperty("连续密码输入错误次数")
    @TableField(value = "login_error_count")
    private Integer loginErrorCount;

    /**
     * 管理部门ID，逗号分隔
     */
    @ApiModelProperty("管理部门ID，逗号分隔")
    @TableField(value = "manage_dept_id")
    private String manageDeptId;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Integer createUserId;
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String createDate;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;
    @TableField(value = "update_user_id", fill = FieldFill.UPDATE)
    private Integer updateUserId;
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    private String updateDate;
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private String updateTime;
    /**
     * 乐观锁用
     */
    @Version
    private Integer version;

    @TableField(exist = false)
    private Integer limit;

    @TableField(exist = false)
    private Integer offset;

    @TableField(exist = false)
    private String oldPassword;

    @TableField(exist = false)
    private String newPassword;

    @TableField(exist = false)
    private String newPasswordAgain;
    @TableField(exist = false)
    private Integer total;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
