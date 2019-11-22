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
import java.util.List;

/**
 * <p>
 * <p> 优惠券规则
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_coupon_rule")
public class EsShopCouponRule extends Model<EsShopCouponRule> {

    private static final long serialVersionUID = 1L;

    /**
     * 使用条件 1无条件 2使用条件
     */
    @TableField("activity_type")
    private Integer activityType;
    /**
     * 优惠券id
     */
    @TableField("coupon_id")
    private Long couponId;

    //商品限制类型1商品分类,2指定商品
    private Integer extra;

    private Long id;
    @TableField("shop_id")
    private Long shopId;
    //单笔消费
    @TableField("single_consumer")
    private BigDecimal singleConsumer;
    //单笔件数满
    @TableField("single_batch")
    private Integer singleBatch;
    //营销规则——基本营销 赠品营销 1-6
    @TableField("marketing_id")
    private String marketingId;
    //商品分类id
    @TableField("classification_id")
    private String classificationId;
    //指定限制的商品
    @TableField(exist = false)
    private List<GoodsSepcVo> goodsSepcVoList;
    @TableField(exist = false)
    private String rulegoodslist;
    //商品使用限制1不添加商品使用限制，2允许以下商品使用，3不允许以下商品使用
    @TableField("goods_limited_id")
    private Integer goodsLimitedId;
    //可使用优惠条件 0单选，1且，2或 ，3无条件
    private Integer conditions;
    /*  //营销规则——赠品营销 2
      @TableField("marketing_gid")
      private String marketingGid;*/
    //优惠使用限制 1无使用限制,2不与其他营销规则同时使用
    @TableField("coupon_limit")
    private Integer couponLimit;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
