package com.mei.zhuang.entity.marking;

import com.mei.zhuang.vo.marking.GoodsSepcVo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * <p> 限时折扣主体表
 * </p>
 *
 * @author arvato team
 * @since 2019-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_discount")
public class EsShopDiscount extends Model<EsShopDiscount> {

    private static final long serialVersionUID = 1L;

    /**
     * 时间间隔
     */
    @TableField("time_interval")
    private Integer timeInterval;
    /**
     * 类型1 消费金额 2 购买件数
     */
    private Integer type;
    /**
     * 工作日，每个周日
     */
    private String weekdays;
    /**
     * 禁用间隔时间
     */
    @TableField("disable_time_interval")
    private String disableTimeInterval;
    /**
     * 准备到期的启动时间
     */
    @TableField("expiry_fixed_time_start")
    private Long expiryFixedTimeStart;
    /**
     * 启动时间
     */
//    @TableField("start_time")
//    private Date startTime;
    @TableField("start_time")
    private Time startTime;
    @TableField("shop_id")
    private Long shopId;
    /**
     * 标题，主题
     */
    private String titles;
    /**
     * 描述
     */
    private String description;
    /**
     * 首先，开始到期时间
     */
    @TableField("expiry_begin_time")
    private Date expiryBeginTime;
    /**
     * 是否排序
     */
    @TableField("is_self_desc")
    private Integer isSelfDesc;
    /**
     * 到期固定时间
     */
    @TableField("expiry_fixed_time")
    private Long expiryFixedTime;
    /**
     * 商品模式，风格 1 全部商品 2 部分商品
     */
    @TableField("goods_mode")
    private Integer goodsMode;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 结束时间
     */
//    @TableField("end_time")
//    private Date endTime;
    @TableField("end_time")
    private Time endTime;
    /**
     * 到期结束时间
     */
    @TableField("expiry_end_time")
    private Date expiryEndTime;
    private Long id;
    /**
     * 活动对象1 全部用户 2 仅注册会员 3 会员等级
     */
    @TableField("activity_object")
    private Integer activityObject;
    /**
     * 活动对象值
     */
    @TableField("activity_object_value")
    private String activityObjectValue;
    /**
     * 状态：1：未启用 0：启用
     */
    private Integer status;
    /**
     * 支付订单数
     */
    @TableField("pay_order_count")
    private Integer payOrderCount;
    /**
     * 参与客户数
     */
    @TableField("attend_user_count")
    private Integer attendUserCount;
    /**
     * 实付金额
     */
    @TableField("pay_amount")
    private BigDecimal payAmount;
    /**
     * 笔单价
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;
    @TableField("update_time")
    private Date updateTime;
    private Integer source;

    //折扣活动的添加
    @TableField(exist = false)
    private List<EsShopDiscountRule> ruleList;
    //折扣商品的添加
    @TableField(exist = false)
    private List<GoodsSepcVo> goodsSepcVoList;
    //商品添加的JSON中转
    @TableField(exist = false)
    private String selectgoods;
    @TableField(exist = false)
    //活动添加的JSON中转
    private String selectrule;
    //小程序
    @TableField("small_program")
    private Integer smallProgram;
    //活动时间
    @TableField(exist = false)
    private String time;
    @TableField(exist =  false)
    //总页数
    private Integer total;
    //order联调的调用活动
    @TableField(exist =  false)
    List<EsShopDiscountRule> discountRuleList;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
