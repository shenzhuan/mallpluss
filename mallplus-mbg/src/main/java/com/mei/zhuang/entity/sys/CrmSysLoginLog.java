package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 后期多租户可以删除，可以删除，可以删除，可以删除
 *
 * @author meizhuang team
 * @since 2018-01-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_sys_login_log")
public class CrmSysLoginLog extends Model<CrmSysLoginLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
     @TableId(value = "id", type = IdType.AUTO)     private Integer id;
    /**
     * 登录用户名
     */
    @TableField("login_name")
    private String loginName;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
