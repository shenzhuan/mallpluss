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
 * 销售渠道表
 * </p>
 *
 * @author arvato team
 * @since 2018-12-14
 */
@ApiModel("渠道")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("crm_channel")
public class CrmChannel extends Model<CrmChannel> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty("渠道id")
    private Integer id;
    /**
     * 俱乐部id
     */
    @TableField("club_id")
    private Integer clubId;
    /**
     *
     */
    @ApiModelProperty("渠道名称")
    private String name;
    /**
     * 备注
     */
    private String description;
    /**
     * 创建人
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
     * 最后修改人
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
