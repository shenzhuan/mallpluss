package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 抽奖有礼
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_activity")
public class EsShopActivity extends Model<EsShopActivity> {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 活动名称
     */
    private String name;
    /**
     * 活动开始时间（时间戳）
     */
    @TableField("activity_start_time")
    private Long activityStartTime;
    /**
     * 活动结束时间（时间戳）
     */
    @TableField("activity_end_time")
    private Long activityEndTime;
    /**
     * 活动规则
     */
    @TableField("activaty_rule")
    private String activatyRule;
    /**
     * 参与人群（1、全部用户 2、指定用户）
     */
    private Integer participants;
    /**
     * 消耗积分 （1、是 0、否）
     */
    @TableField("consumer_integral")
    private Integer consumerIntegral;
    /**
     * 参与送积分（1、是 0、否）
     */
    @TableField("send_integral")
    private Integer sendIntegral;
    /**
     * 单人中奖次数
     */
    @TableField("win_num")
    private Integer winNum;
    /**
     * 单人参与总次数
     */
    @TableField("participant_num")
    private Integer participantNum;
    /**
     * 每人每天参与次数
     */
    @TableField("everyone_participant_num")
    private Integer everyoneParticipantNum;
    /**
     * 奖项通知模版
     */
    @TableField("prize_notice")
    private String prizeNotice;
    /**
     * 未中奖提示语
     */
    @TableField("nowin_tips")
    private String nowinTips;
    /**
     * 提示跳转（1、跳转 0、不跳转）
     */
    private Integer jump;
    /**
     * 未中奖图片
     */
    @TableField("nowin_img")
    private String nowinImg;
    /**
     * 背景图片
     */
    @TableField("backage_img")
    private String backageImg;
    /**
     * 抽奖类型
     */
    @TableField("smoke_type")
    private Integer smokeType;
    /**
     * 抽奖模版
     */
    @TableField("smoke_template")
    private String smokeTemplate;
    @TableField("win_rate")
    private Integer winRate; //未中奖率
    private Integer position;//未中奖位置
    @TableField(exist = false)
    private List<EsShopActivityPrize> list;

    @TableField(exist = false)
    private Integer status;//1.进行中 2.未开始 3.已结束
    @TableField(exist = false)
    private Long currentTime = System.currentTimeMillis();

    @TableField("not_win_status")
    private Integer notWinStatis;//未中奖状态
    @TableField("com_win")
    private String comWin;//综合中奖率

    @TableField(exist = false)
    private Integer current = 1;
    @TableField(exist = false)
    private Integer size = 10;
    @TableField(exist = false)
    private String activatyPrize;
    @TableField(exist = false)
    private Long memberId;
    @TableField(exist = false)
    private Integer winPrizeNumber = 0;//中奖人数
    @TableField(exist = false)
    private Integer totalNumber = 0;//参与总人数
    @TableField(exist = false)
    private Integer totalFrequency = 0;//参与总次数
    @TableField(exist = false)
    private Integer noWinPrizeNumber = 0;//未中奖人数
    @TableField(exist = false)
    private String formId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
