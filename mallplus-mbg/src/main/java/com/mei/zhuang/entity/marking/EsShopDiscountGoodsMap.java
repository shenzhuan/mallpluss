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
 * <p>
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_discount_goods_map")
public class EsShopDiscountGoodsMap extends Model<EsShopDiscountGoodsMap> {

    private static final long serialVersionUID = 1L;

    /**
     * 折扣id
     */
    @TableField("discount_id")
    private Long discountId;
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 规格数组
     */
    @TableField("spec_ids")
    private String specIds;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
