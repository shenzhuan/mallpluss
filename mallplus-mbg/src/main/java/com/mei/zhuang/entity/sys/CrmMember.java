package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author arvato team
 * @since 2018-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_member")
public class CrmMember extends Model<CrmMember> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	private Long id;
    /**
     * 俱乐部ID
     */
	@TableField("club_id")
	private Long clubId;
    /**
     * 会员编号
     */
	@TableField("member_code")
	private String memberCode;
    /**
     * 手机号码
     */
	private String mobile;
    /**
     * 1 启用 2 冻结 3 删除
     */
	private String status;
    /**
     * 01(默认)  02  03  04
     */
	@TableField("grade_code")
	private String gradeCode;
    /**
     * 去年等级
     */
	@TableField("last_grade_code")
	private String lastGradeCode;
    /**
     * 姓名
     */
	private String name;
    /**
     * 证件类型
     */
	@TableField("id_type")
	private String idType;
    /**
     * 证件号码
     */
	@TableField("id_no")
	private String idNo;
    /**
     * 邮箱
     */
	private String mail;
    /**
     * 1 男 2 女 3 其他
     */
	private String sex;
    /**
     * 生日
     */
	@TableField("birthday")
	private String birthday;
    /**
     * 1 默认 0 已被修改
     */
	@TableField("is_edit_birthday")
	private Integer isEditBirthday;
    /**
     * 1 默认 2 已奖励
     */
	@TableField("do_birthday_flag")
	private Integer doBirthdayFlag;
    /**
     * 生日奖励时间
     */
	@TableField("do_birthday_date")
	private String doBirthdayDate;
    /**
     * 注册时间
     */
	@TableField("register_time")
	private Date registerTime;
    /**
     * 是否会员
     */
	@TableField("is_member")
	private Integer isMember;

    /**
     * 是否黑卡
     */
	@TableField("is_black_card")
	private Integer isBlackCard;

    /**
     * 删除状态
     */
	@TableField("is_deleted")
	private Integer isDeleted;

    /**
     * 入会渠道
     */
	private String channel;
	@TableField("store_id")
	private Integer storeId;
	@TableField("store_num")
	private String storeNum;
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
    /**
     * 1 否（默认） 2 是
     */
	@TableField("employee_flag")
	private String employeeFlag;
	@TableField("bu_id")
	private Integer buId;
    /**
     * 等级优先级，值越小等级越高
     */
	@TableField("grade_priority")
	private Integer gradePriority;
	private String brandcode;
    /**
     * 备注
     */
	private String remark;

	@TableField(exist = false)
	private String wechat;
	@TableField(exist = false)
	private String address;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
