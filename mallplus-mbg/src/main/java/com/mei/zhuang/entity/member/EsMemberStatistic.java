package com.mei.zhuang.entity.member;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author arvato team
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_member_statistic")
public class EsMemberStatistic extends Model<EsMemberStatistic> {

    private static final long serialVersionUID = 1L;

    @TableField("coupon_enabled_ids")
    private String couponEnabledIds;
    @TableField("coupon_used_ids")
    private String couponUsedIds;
    @TableField("member_id")
    private Integer memberId;
    @TableField("no_cost_day_count")
    private Integer noCostDayCount;
    @TableField("shop_id")
    private Integer shopId;
    @TableField("activity_earnings")
    private String activityEarnings;
    @TableField("cost_time_average")
    private Integer costTimeAverage;
    @TableField("coupon_use_total")
    private Integer couponUseTotal;
    @TableField("recharge_price_total")
    private String rechargePriceTotal;
    @TableField("cost_balance_today")
    private String costBalanceToday;
    @TableField("cost_credit_today")
    private Integer costCreditToday;
    @TableField("cost_goods_ids_today")
    private String costGoodsIdsToday;
    @TableField("coupon_send_total")
    private Integer couponSendTotal;
    @TableField("activity_join_rate")
    private String activityJoinRate;
    @TableField("cost_price_total")
    private String costPriceTotal;
    @TableField("cost_times_count")
    private Integer costTimesCount;
    @TableField("coupon_enabled_count")
    private Integer couponEnabledCount;
    private String date;
    @TableField("recharge_times_count")
    private Integer rechargeTimesCount;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
