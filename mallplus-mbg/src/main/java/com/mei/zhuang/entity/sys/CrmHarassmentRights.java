package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
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
 * @author meizhuang team
 * @since 2018-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("crm_harassment_rights")
public class CrmHarassmentRights extends Model<CrmHarassmentRights> {

    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    /**
     * 防骚扰配置id
     */
    @TableField("prevent_harassment_config_id")
    private Integer preventHarassmentConfigId;
    /**
     * 剩余次数
     */
    private Integer residue;
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
     * 短信渠道已发送次数
     */
    @TableField("sms_used_num")
    private Integer smsUsedNum;

    /**
     * 微信渠道已发送次数
     */
    @TableField("wechat_used_num")
    private Integer wechatUsedNum;

    /**
     * 邮件渠道已发送次数
     */
    @TableField("mail_used_num")
    private Integer mailUsedNum;

    /**
     * 彩信渠道已发送次数
     */
    @TableField("mms_used_num")
    private Integer mmsUsedNum;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
