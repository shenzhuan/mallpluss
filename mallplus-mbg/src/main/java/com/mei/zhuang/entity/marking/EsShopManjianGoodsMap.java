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
 * <p> 指定的优惠商品
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_manjian_goods_map")
public class EsShopManjianGoodsMap extends Model<EsShopManjianGoodsMap> {

    private static final long serialVersionUID = 1L;

    /**
     * 满减id
     */
    @TableField("manjian_id")
    private Long manjianId;
    @TableField("goods_id")
    private Long goodsId;
    //规格数组
    @TableField("spec_ids")
    private String specIds;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
