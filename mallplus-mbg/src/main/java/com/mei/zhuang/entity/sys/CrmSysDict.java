package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.*;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author arvato team
 * @since 2017-09-27
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName("ec_sys_dict")
public class CrmSysDict extends Model<CrmSysDict> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private String value;
	private String text;
	@TableField("table_name")
	private String tableName;
	private String field;
	@TableField("field_name")
	private String fieldName;
	private Integer sort;
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
    @TableField(exist = false)
    private Integer limit;
    @TableField(exist = false)
    private Integer offset;
	@Version
	private Integer version;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
