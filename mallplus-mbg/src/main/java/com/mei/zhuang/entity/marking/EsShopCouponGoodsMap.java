package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * <p>优惠券商品
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_coupon_goods_map")
public class EsShopCouponGoodsMap extends Model<EsShopCouponGoodsMap> {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券id
     */
    @TableField("coupon_id")
    private Long couponId;
    @TableField("goods_id")
    private Long goodsId;
    @TableField("spec_ids")
    private String specIds;
    // 1.实物商品 2.限制使用的商品，3赠品商品
    @TableField("goodslimit")
    private Integer goodslimit;
    /*  //规格
      @TableField("spec_id")
      private Integer specId;*/
    //商品名称
    @TableField("goods_name")
    private String goodsName;
    private String pic;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
