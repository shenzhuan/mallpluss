package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Setter
@Getter
@Accessors(chain = true)
@TableName("crm_sys_role_menu")
public class CrmSysRoleMenu extends Model<CrmSysRoleMenu> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    @TableField("role_id")
    private Integer roleId;
    @TableField("menu_id")
    private Integer menuId;
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Integer createUserId;
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private String createDate;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
