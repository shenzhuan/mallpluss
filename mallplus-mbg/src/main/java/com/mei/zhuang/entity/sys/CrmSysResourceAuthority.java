package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author arvato team
 * @since 2017-10-16
 */
@Data
@Accessors(chain = true)
@TableName("crm_sys_resource_authority")
public class CrmSysResourceAuthority extends Model<CrmSysResourceAuthority> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 角色ID
     */
	@TableField("authority_id")
	private String authorityId;
    /**
     * 角色类型
     */
	@TableField("authority_type")
	private String authorityType;
    /**
     * 资源ID
     */
	@TableField("resource_id")
	private String resourceId;
    /**
     * 资源类型
     */
	@TableField("resource_type")
	private String resourceType;
	@TableField("parent_id")
	private String parentId;
	private String path;
	private String description;
	@TableField(value = "create_user_id", fill = FieldFill.INSERT)
	private Integer createUserId;
	@TableField(value = "create_date", fill = FieldFill.INSERT)
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


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
