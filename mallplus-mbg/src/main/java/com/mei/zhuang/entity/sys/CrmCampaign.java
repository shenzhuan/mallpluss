package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.*;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 活动表
 * </p>
 *
 * @author arvato team
 * @since 2017-10-11
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName("crm_campaign")
public class CrmCampaign extends Model<CrmCampaign> {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
	@TableId(value="campaign_id", type= IdType.AUTO)
	private Integer campaignId;
    /**
     * 活动名称
     */
	@TableField("campaign_name")
	private String campaignName;
    /**
     * 活动类型
     */
	@TableField("campaign_type")
	private Integer campaignType;
    /**
     * 活动状态
     */
	private Integer status;
	/**
	 * 活动测试状态
	 */
	@TableField("test_status")
	private Integer testStatus;

	/**
	 * 活动流程选择
	 */
	@TableField("activity_process_selection")
	private Integer activityProcessSelection;
	/**
	 * 活动模板id
	 */
	@TableField("template_campaign_id")
	private Integer templateCampaignId;
    /**
     * 开始日期
     */
	@TableField("start_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
    /**
     * 结束日期
     */
	@TableField("end_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
    /**
     * 备注
     */
	private String remark;
    /**
     * 是否开启黑名单
     */
	@TableField("is_blacklist")
	private String isBlacklist;
    /**
     * 业务单元
     */
	@TableField("bu_id")
	private Integer buId;
    /**
     * 负责人
     */
	@TableField("owner_id")
	private Integer ownerId;
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

	/** 是否开启静默防骚扰设置  */
	@TableField(value = "is_open_silent")
	private Integer isOpenSilent;

	/** 静默开始时间  */
	@TableField(value = "silent_begin_time")
	private String silentBeginTime;

	/** 静默结束时间  */
	@TableField(value = "silent_end_time")
	private String silentEndTime;


	@Override
	protected Serializable pkVal() {
		return this.campaignId;
	}

}
