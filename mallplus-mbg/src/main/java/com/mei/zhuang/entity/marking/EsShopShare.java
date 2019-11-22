package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mei.zhuang.entity.member.EsMember;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author meizhuang team
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_share")
public class EsShopShare extends Model<EsShopShare> {

    private static final long serialVersionUID = 1L;

    private long id;
    /**
     * 活动名称
     */
    @TableField("activity_name")
    private String activityName;
    /**
     * 活动段时间
     */
    @TableField("start_time")
    private String startTime;
    /**
     * 活动段时间
     */
    @TableField("end_time")
    private String endTime;
    /**
     * 活动类型
     */
    @TableField("activity_type")
    private Integer activityType;
    /**
     * 活动渠道
     */
    private Integer channels;
    /**
     * 状态 1.开启 2.禁用
     */
    private Integer status;
    /**
     * 周期日
     */
    private String weekday;
    /**
     * 有效期时间
     */
    @TableField("activitystart_time")
    private Date activitystartTime;
    /**
     * 有效期时间
     */
    @TableField("activityend_time")
    private Date activityendTime;
    /**
     * 背景图
     */
    private String bgimage;
    /**
     * 活动规则
     */
    @TableField("activity_rule")
    private String activityRule;
    /**
     * 发起者图片
     */
    @TableField("initiator_image")
    private String initiatorImage;
    /**
     * 助力者图片
     */
    @TableField("power_image")
    private String powerImage;
    /**
     * 分享图片
     */
    @TableField("share_image")
    private String shareImage;
    /**
     * 分享标题
     */
    @TableField("share_title")
    private String shareTitle;
    /**
     * 发起者好友助力满次
     */
    @TableField("help_number")
    private Integer helpNumber;
    /**
     * 可获赠件
     */
    @TableField("gift_number")
    private Integer giftNumber;
    /**
     * 发起者获赠奖品  1,优惠 2赠品
     */
    @TableField("gift_prize")
    private Integer giftPrize;
    /**
     * 重复发起 1,是 2，否
     */
    @TableField("is_repeat")
    private Integer isRepeat;
    /**
     * 单用户最多发起次
     */
    @TableField("member_number")
    private Integer memberNumber;
    /**
     * 助力者重复助力 1,可以 2,不可以
     */
    @TableField("is_help")
    private String isHelp;
    /**
     * 最多重复助力次数
     */
    @TableField("repeat_number")
    private Integer repeatNumber;
    /**
     * 助力有奖 1,有 2，无
     */
    @TableField("is_prize")
    private Integer isPrize;
    //1 显示 2.删除
    @TableField("is_del")
    private Integer isDel;
    /**
     * 助力者获赠奖品   1,优惠  2,赠品
     */
    @TableField("help_prize")
    private Integer helpPrize;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
    // 助力和发起类型
    @TableField("winning_type")
    private Integer winningType;
    //顶部广告图
    @TableField("top_picture")
    private String topPicture;
    //轮播图
    @TableField("shuffling_picture")
    private String shufflingPicture;
    //底部广告图
    @TableField("bottom_picture")
    private String bottomPicture;
    //按钮文字
    @TableField("button_text")
    private String buttonText;
    //按钮颜色
    @TableField("button_color")
    private String buttonColor;
    //文字颜色
    @TableField("text_color")
    private String textColor;
    // 助力者好友满次数
    @TableField("power_number")
    private Integer powerNumber;
    // 助力获赠件数
    @TableField("power_given")
    private Integer powerGiven;
    //最多获取次奖品
    @TableField("most_prize")
    private Integer mostPrize;
    //总助力次数
    @TableField("total_power")
    private Integer totalPower;
    //发起者
    @TableField(exist = false)
    private List<EsShopShareMap> sharemapList;
    //助力者
    @TableField(exist = false)
    private List<EsShopShareMap> sharemapList2;
    //发起者
    @TableField(exist = false)
    private String shareMapinitiate;
    @TableField(exist = false)
    private String sharehelp;
    @TableField(exist = false)
    private String activityTime;
    @TableField(exist = false)
    private Integer total;
    @TableField(exist = false)
    private Long shareMemberId;
    @TableField(exist = false)
    private EsMember member;
    @TableField(exist = false)
    private List<EsMember> memberList;
    @TableField(exist = false)
    private Integer isClose;//1任务完成 0没完成
    @TableField(exist = false)
    private Integer isConfirm;//发起者是否确认奖品0确认1没确认
    @TableField(exist = false)
    private Integer memberNumberCount;//用户已发起次数
    @TableField(exist = false)
    private Integer memberTotalAssis;//用户总助力次数
    @TableField(exist = false)
    private Integer memberThisAssis;//用户当前发起活动助力次数
    @TableField(exist = false)
    private Integer thisPrize;//已获得奖品件数
    @TableField(exist = false)
    private Integer countNumber;//统计发起次数

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
