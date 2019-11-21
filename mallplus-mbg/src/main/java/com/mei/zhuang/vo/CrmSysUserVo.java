package com.mei.zhuang.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CrmSysUserVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 姓名
     */
    private String name;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 地址
     */
    private String address;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private String sex;
    /**
     * 类型
     */
    private String type;
    /**
     * 状态
     */
    private String status;

    /**
     * 所属部门ID
     */
    private Integer deptId;

    /**
     * 最后登录时间
     */
    private String lastLoginTime;

    /**
     * 连续密码输入错误次数
     */
    private Integer loginErrorCount;

    /**
     * 管理部门ID，逗号分隔
     */
    private String manageDeptId;

    private String oldPassword;

    private String newPassword;

    private String newPasswordAgain;


}
