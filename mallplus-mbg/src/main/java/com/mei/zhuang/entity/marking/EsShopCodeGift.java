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
@TableName("es_shop_code_gift")
public class EsShopCodeGift extends Model<EsShopCodeGift> {

    private static final long serialVersionUID = 1L;

    /**
     * 时间间隔
     */
    @TableField("time_interval")
    private Integer timeInterval;
    /**
     * 类型1 唯一验证码 2 批量验证码
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
     * 商品模式，风格 1 全部商品 2 指定商品 3 不指定商品
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
     * 状态1 启用 2 禁用
     */
    private Integer status;
    @TableField("update_time")
    private Date updateTime;
    //验证码
    @TableField(exist = false)
    private List<EsShopCodeGiftRule> CoderuleList;
    @TableField(exist = false)
    private String codeList;
    //商品添加
    @TableField(exist = false)
    private List<GoodsSepcVo> goodsSepcVoList;
    @TableField(exist = false)
    private String goodSpecList;
    //赠品添加
    @TableField(exist = false)
    private List<EsShopCodeGiftGoodsMap> goodsCouponList;
    //赠品接收字段
    @TableField(exist = false)
    private String songGoodsIds;

    @TableField("small_program")
    private Integer smallProgram;

    @TableField(exist = false)
    private String time;

    @TableField(exist = false)
    private Integer size=10;
    @TableField(exist = false)
    private Integer current=1;
  /* //接受上传验证码
    @TableField(exist = false)
    private String code;*/
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
