package com.mei.zhuang.entity.marking;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author arvato team
 * @since 2019-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_single_gift")
public class EsShopSingleGift extends Model<EsShopSingleGift> {

    private static final long serialVersionUID = 1L;

    /**
     * 时间间隔
     */
    @TableField("time_interval")
    private Integer timeInterval;
    /**
     * 类型1 仅送一件 2 按购买件数 3 指定件数送
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
     * 商品模式，风格 1 单品购买 2 组合购买
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
     * 状态1 启用 2 禁用
     */
    private Integer status;
    @TableField("update_time")
    private Date updateTime;
    private Integer source;
    //单品获取规则添加
    @TableField(exist = false)
    private List<EsShopSingleGiftRule> ruleList;
    @TableField(exist = false)
    private String selectrule;
    //赠品添加
    @TableField(exist = false)
    private List<EsShopSingleGiftGoodsMap> giftGoodsList;
    @TableField(exist = false)
    private String giftList;
    //单品的单组合商品添加
    @TableField(exist = false)
    private List<GoodsSepcVo> goodsSepcVoList1;
    @TableField(exist = false)
    private String SepcVoList1;
    //单品的多组合商品添加
    @TableField(exist = false)
    private List<GoodsSepcVo> goodsSepcVoList2;
    @TableField(exist = false)
    private String SepcVoList2;
   /* //赠品的添加数组
    @TableField(exist = false)
    private String songGoodsIds;*/

    @TableField("small_program")
    private Integer smallProgram;
    @TableField(exist = false)
    //活动时间
    private String time;
    @TableField(exist = false)
    private Integer total;
    //小程序获取规则调用
    @TableField(exist = false)
    private List<EsShopSingleGiftRule> shopSingleGiftRuleList;
    //小程序商品和赠品调用
    @TableField(exist = false)
    private List<EsShopSingleGiftGoodsMap> giftGoodsMapsList;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
