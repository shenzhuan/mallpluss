package com.mei.zhuang.entity.marking;


import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
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
 * <p>满额立减
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_manjian")
public class EsShopManjian extends Model<EsShopManjian> {

    private static final long serialVersionUID = 1L;
    //order联调的活动调用
    @TableField(exist = false)
    List<EsShopManjianRule> manjianRuleList;
    /**
     * 时间间隔
     */
    @TableField("time_interval")
    private Integer timeInterval;
    /**
     * 优惠类型 1 按消费金额优惠 2 按购买件数优惠
     */
    private Integer type;
    /**
     * 工作日，每个周日 1-7
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
    @TableField("start_time")
    // @JSONField(format="HH:mm:ss", deserializeUsing = SqlTimeDeserializer.class)
    private Time startTime;
    @TableField("shop_id")
    private Long shopId=1l;
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
     * 优惠商品模式 1 全部商品 2 部分商品
     */
    @TableField("goods_mode")
    private Integer goodsMode;
    //商品id 绑定添加
    @TableField(exist = false)
    private List<GoodsSepcVo> goodsSepcVoList;
    @TableField(exist = false)
    private String selectgoods;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 结束时间
     */
    @TableField("end_time")
    // @JSONField(format="HH:mm:ss", deserializeUsing = SqlTimeDeserializer.class)
    private Time endTime;
    /**
     * 到期结束时间
     */
    @TableField("expiry_end_time")
    private Date expiryEndTime;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 活动对象 1 全部用户 2 仅注册会员 3 会员等级
     */
    @TableField("activity_object")
    private Integer activityObject;
    /**
     * 活动对象值
     */
    @TableField("activity_object_value")
    private String activityObjectValue;
    /**
     * 状态 0 启用 1 禁用
     */
    private Integer status;
    /**
     * 状态 1 小程序
     */
    private Integer source;
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
    // 活动渠道
    @TableField("small_program")
    private Integer smallProgram;
    // 优惠规则设置
    @TableField(exist = false)
    private List<EsShopManjianRule> ruleList;
    /**
     * 优惠设置中转
     */
    @TableField(exist = false)
    private String selectrule;
   /* //高级选项活动段时间
    @TableField(exist = false)
    private String periodtime;*/
    //活动时间
    @TableField(exist = false)
    private String time;
    @TableField(exist = false)
    private Integer total;
    @TableField(exist = false)
    private Integer counts;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
