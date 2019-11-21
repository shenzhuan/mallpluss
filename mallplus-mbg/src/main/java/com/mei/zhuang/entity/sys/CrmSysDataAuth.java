package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@TableName("crm_sys_data_auth")
public class CrmSysDataAuth extends Model<CrmSysDataAuth> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Integer id;
    /**
     * 角色ID
     */
	@TableField("role_id")
	private Integer roleId;
    /**
     * 菜单ID
     */
	@TableField("menu_id")
	private Integer menuId;
    /**
     * 数据权限级别 1：组织 2：上下级 3：部门
     */
	@TableField("data_auth_level")
	private Integer dataAuthLevel;
    /**
     * 创建人ID
     */
	@TableField(value = "create_user_id", fill = FieldFill.INSERT)
	private Integer createUserId;
    /**
     * 创建日期
     */
	@TableField(value = "create_date", fill = FieldFill.INSERT)
	private String createDate;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private String createTime;
    /**
     * 最后修改人ID
     */
	@TableField(value = "update_user_id", fill = FieldFill.UPDATE)
	private Integer updateUserId;
    /**
     * 最后修改日期
     */
	@TableField(value = "update_date", fill = FieldFill.UPDATE)
	private String updateDate;
    /**
     * 最后修改时间
     */
	@TableField(value = "update_time", fill = FieldFill.UPDATE)
	private String updateTime;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
