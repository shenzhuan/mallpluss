package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author arvato team
 * @since 2018-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("crm_prevent_harassment_config")
public class CrmPreventHarassmentConfig extends Model<CrmPreventHarassmentConfig> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 防骚扰类别 1=营销类
     */
    private String type;
    private Integer number;
    /**
     * 状态 1=启用  2=禁用
     */
    private String status;
    /**
     * 数据归属组织
     */
    @TableField("dept_id")
    private Integer deptId;
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
     * 新增短信、微信、邮件、彩信渠道防骚扰配置，限制次数、是否启用防骚扰设置
     */
    @TableField(value = "data_config")
    private String dataConfig;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
