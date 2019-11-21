package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author arvato team
 * @since 2017-12-07
 */
@ApiModel("接口信息")
@Data
@Accessors(chain = true)
@TableName("crm_api_interface")
public class CrmApiInterface extends Model<CrmApiInterface> {

    private static final long serialVersionUID = 1L;

	private Integer id;

	@ApiModelProperty("接口类型id")
	@TableField("type_id")
	private String typeId;
    /**
     * 接口名称
     */
	@ApiModelProperty(" 接口名称")
	private String name;

    /**
     * 状态：1-启用、2-维护、3-废弃
     */
	@ApiModelProperty("状态：1-启用、2-维护、3-废弃")
	private String status;

    /**
     * Request URL：相对地址
     */
	@ApiModelProperty("Request URL：相对请求地址")
	@TableField("request_url")
	private String requestUrl;

    /**
     * 备注
     */
	@ApiModelProperty(" 备注 ")
	private String remark;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private String createTime;
    /**
     * 更新时间
     */
	@TableField(value = "update_time", fill = FieldFill.UPDATE)
	private String updateTime;
    /**
     * 创建人
     */
	@TableField(value = "create_user_id", fill = FieldFill.INSERT)
	private Integer createUserId;
    /**
     * 更新人
     */
	@TableField(value = "update_user_id", fill = FieldFill.UPDATE)
	private Integer updateUserId;
    /**
     * 创建日期
     */
	@TableField(value = "create_date", fill = FieldFill.INSERT)
	private String createDate;
    /**
     * 更新日期
     */
	@TableField(value = "update_date", fill = FieldFill.UPDATE)
	private String updateDate;
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
