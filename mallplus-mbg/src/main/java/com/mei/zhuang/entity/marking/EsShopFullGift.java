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
 * <p>     满赠礼
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_full_gift")
public class EsShopFullGift extends Model<EsShopFullGift> {

    private static final long serialVersionUID = 1L;

    /**
     * 时间间隔
     */
    @TableField("time_interval")
    private Integer timeInterval;
    /**
     * 类型1 选赠礼 2 满赠礼
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
     * 商品模式，风格 1 全部商品 2 部分商品 3 不参与商品
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
     * 状态: 0：启用 1 禁用
     */
    private Integer status;

    @TableField("update_time")
    private Date updateTime;
    private Integer source;
    //满赠优惠设置添加
    @TableField(exist = false)
    private List<EsShopFullGiftRule> ruleList;
    @TableField(exist = false)
    private String selectrule;
    //满赠商品添加
    @TableField(exist = false)
    private List<GoodsSepcVo> goodsSepcVoList;
    //商品添加的JSON中转
    @TableField(exist = false)
    private String selectgoods;
    //满赠赠品添加
    @TableField(exist = false)
    private List<EsShopFullGiftGoodsMap> fullGiftGoodsList;
    @TableField(exist = false)
    private String selectfullgift;
    @TableField(exist = false)
    private List<EsShopFullGift> fullGiftList;
    @TableField(exist = false)
    private String selectfullList;
    //小程序
    @TableField("small_program")
    private Integer smallProgram;
    @TableField(exist = false)
    private String time;
    @TableField(exist = false)
    private Integer total;
    //小程序优惠设置调用
    @TableField(exist = false)
    private List<EsShopFullGiftRule> fullGiftRuleList;
    @TableField(exist = false)
    private List<EsShopFullGiftRule> fullGiftRuleList2;

    //小程序调用
    @TableField(exist = false)
    private List<EsShopFullGiftGoodsMap> fullGiftGoodsMapList;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
