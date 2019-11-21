package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ApiModel("角色类")
@Setter
@Getter
@Accessors(chain = true)
@TableName("crm_sys_role")
public class CrmSysRole extends Model<CrmSysRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID",hidden = true)
	private Integer id;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称",required = true)
	private String name;
    /**
     * 状态 1：启用 2：禁用
     */
    @ApiModelProperty(value = "状态 1：启用 2：禁用",hidden = true)
	private String status;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述",required = true)
	private String description;
    /**
     * 创建者ID
     */
	@TableField(value = "create_user_id", fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建者ID",hidden = true)
	private Integer createUserId;
    /**
     * 创建日期
     */
	@ApiModelProperty(value = "创建日期")
	@TableField(value = "create_date", fill = FieldFill.INSERT)
	private String createDate;
    /**
     * 创建时间
     */
	@ApiModelProperty(value = "创建时间")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private String createTime;
    /**
     * 更新者ID
     */
	@TableField(value = "update_user_id", fill = FieldFill.UPDATE)
	@ApiModelProperty(value = "更新者ID",hidden = true)
	private Integer updateUserId;
    /**
     * 更新日期
     */
	@ApiModelProperty(value = "更新日期")
	@TableField(value = "update_date", fill = FieldFill.UPDATE)
	private String updateDate;
    /**
     * 更新时间
     */
	@ApiModelProperty(value = "更新时间")
	@TableField(value = "update_time", fill = FieldFill.UPDATE)
	private String updateTime;
    /**
     * 乐观锁用
     */
	@Version
	@ApiModelProperty(value = "乐观锁用",hidden = true)
	private Integer version;

	@ApiModelProperty(value = "每页显示数",hidden = true)
	@TableField(exist = false)
	private Integer limit = 10;

	@ApiModelProperty(value = "开始行数",hidden = true)
	@TableField(exist = false)
	private Integer offset = 0;

	@TableField(exist = false)
	@ApiModelProperty(value = "上级角色ID",hidden = true)
	private Integer pId = 0;

	@TableField(exist = false)
	private Integer total;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
