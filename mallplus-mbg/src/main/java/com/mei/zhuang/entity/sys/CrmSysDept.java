package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ApiModel("部门类")
@Setter
@Getter
@Accessors(chain = true)
@TableName("crm_sys_dept")
public class CrmSysDept extends Model<CrmSysDept> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
     @TableId(value = "id", type = IdType.AUTO)     private Integer id;
    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String name;
    /**
     * 上级部门ID
     */
    @ApiModelProperty("上级部门ID")
    @TableField("p_id")
    @JsonProperty("pId")
    private Integer pId;

    /**
     * 部门类型
     */
    @ApiModelProperty("部门类型")
    @TableField("org_type")
    private Integer orgType;

    /**
     * 渠道ID
     */
    @ApiModelProperty("渠道ID")
    @TableField("channel_id")
    private Integer channelId;

    /**
     * 部门顺序
     */
    @ApiModelProperty("部门顺序")
    private Integer num;

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

    /**
     * 渠道ID
     */
    @ApiModelProperty("渠道ID")
    @TableField("dept_no")
    private String deptNo;

    @ApiModelProperty("商店")
    @TableField(exist = false)
    private CrmStore store;

    @TableField(exist = false)
    private CrmDeptCreditList crmDeptCreditList;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
