package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *       发券公用的指定商品
 * </p>
 *
 * @author arvato team
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_coupons_topup_goods")
public class EsShopCouponsTopupGoods extends Model<EsShopCouponsTopupGoods> {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 商品id
     */
    @TableField("good_id")
    private Long goodId;
    /**
     * 规格
     */
    @TableField("specifications_id")
    private String specificationsId;
    //公共关联商品id
    @TableField("public_goodsid")
    private String publicGoodsid;

    //实物发券id
    @TableField("physical_id")
    private Long physicalId;

    @Override
    protected Serializable pkVal() {
        return null;
    }



}
