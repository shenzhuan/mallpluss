package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_perm_map")
public class EsShopGoodsPermMap extends Model<EsShopGoodsPermMap> {

    private static final long serialVersionUID = 1L;

    @TableField("type_id")
    private Long typeId;
    @TableField("goods_id")
    private Long goodsId;
    @TableField("member_type")
    private Integer memberType;
    @TableField("perm_type")
    private Long permType;
    @TableField("shop_id")
    private Long shopId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
