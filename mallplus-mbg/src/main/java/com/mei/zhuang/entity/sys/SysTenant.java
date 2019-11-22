package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("租户信息")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tenant")
public class SysTenant extends Model<SysTenant> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    @ApiModelProperty("租户名称")
    @TableField("tenant_name")
    private String tenantName;

    @ApiModelProperty("租户数据源id")
    @TableField("db_resource_id")
    private Integer dbResourceId;

    @ApiModelProperty("租户数据源id对应的schema")
    @TableField("db_schema")
    private String dbSchema;

    @ApiModelProperty("租户品牌名称")
    @TableField("brand_name")
    private String brandName;

    @ApiModelProperty("租户品牌Logo地址")
    @TableField("brand_logo")
    private String brandLogo;

    @ApiModelProperty("租户公司名称")
    @TableField("company_name")
    private String companyName;

    @ApiModelProperty("租户公司电话")
    @TableField("company_tel")
    private String companyTel;

    @ApiModelProperty("租户公司邮箱")
    @TableField("company_email")
    private String companyEmail;

    @ApiModelProperty("租户公司地址")
    @TableField("company_address")
    private String companyAddress;
    /**
     * 0=启用，1=禁用
     */
    @ApiModelProperty("租户状态,0=启用，1=禁用")
    @TableField
    private String status;


    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private String createDate;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    private String updateDate;
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private String updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
