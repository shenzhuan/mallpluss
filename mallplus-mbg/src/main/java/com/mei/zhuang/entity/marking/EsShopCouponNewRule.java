package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/*
    公共优惠券【定义新人发券规则选择优惠券的表】
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_coupon_new_rule")
public class EsShopCouponNewRule extends Model<EsShopCouponNewRule> {
    private static final long serialVersionUID = 1L;
    private Long id;
    //优惠券id
    private Long couponid;
    // 优惠券库存
    private Integer inventory;
    @TableField("coupon_name")
    private String couponName;
    //公用关联优惠券id
    @TableField("public_couponid")
    private String publicCouponid;
    //优惠券类型（1现金，2折扣，3实物 4赠品券）
    @TableField("coupon_types")
    private Integer couponTypes;

    @Override
    protected Serializable pkVal() {
        return id;
    }

}
