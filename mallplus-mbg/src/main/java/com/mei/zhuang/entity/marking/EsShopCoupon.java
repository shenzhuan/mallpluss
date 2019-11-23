package com.mei.zhuang.entity.marking;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 优惠券
 * <p>
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_coupon")
public class EsShopCoupon extends Model<EsShopCoupon> {

    private static final long serialVersionUID = 1L;

    /**
     * 时间间隔
     */
    @TableField("time_interval")
    private String timeInterval;
    /**
     * 优惠券类型 1现金,2折扣，3实物券 4赠品券
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
     * 消费门槛：满足此金额才可使用
     */
    private BigDecimal enough;
    /**
     * 准备到期的启动时间
     */
    @TableField("expiry_fixed_time_start")
    private Long expiryFixedTimeStart;
    /**
     * 启动时间
     */
    @TableField("start_time")
    private Date startTime;
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 库存
     */
    private Long stock;
    /**
     * 标题，主题
     */
    private String title;
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
     * 有效期 1固定，2当前，3次日
     */
    private Integer amount;
    /**
     * 到期固定时间
     */
    @TableField("expiry_fixed_time")
    private Long expiryFixedTime;
    /**
     * 商品模式，风格
     */
    @TableField("goods_mode")


    private Integer goodsMode;
    /**
     * 使用条件 1无条件 2使用条件
     */
    private Integer limit;
    /**
     * 创建时间
     */

    @TableField("create_time")
    private Date createTime;
    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 到期结束时间
     */
    @TableField("expiry_end_time")
    private Date expiryEndTime;
    //接受结束时间
    @TableField(exist = false)
    private Date exEndTime;
    @TableField(exist = false)
    private Date exBeginTime;
    private Long id;
    //小程序
    @TableField("channel_id")
    private String channelId;

    //状态1有效，2未生效，3失效
    private Integer status;

    //1，启用 2，禁用
    @TableField("status_open")
    private String statusOpen;
    //优惠券名称
    @TableField("coupons_name")
    private String couponsName;
    //优惠券券号
    @TableField("coupon_number")
    private Integer couponNumber;

    //未使用图片
    @TableField("don_use_pic")
    private String donUsePic;
    //已使用图片
    @TableField("to_use_pic")
    private String toUsePic;
    //已过期图片
    @TableField("expired_pic")
    private String expiredPic;

    @TableField(exist = false)
    private String time;
    //实物的商品和赠品商品
    @TableField(exist = false)
    private List<GoodsSepcVo> goodsSepcVoLists;
    @TableField(exist = false)
    private String goodsList;


    @TableField(exist = false)
    private Integer total = 0;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
