package com.mei.zhuang.entity.goods;

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
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_label_map")
public class EsShopGoodsLabelMap extends Model<EsShopGoodsLabelMap> {

    private static final long serialVersionUID = 1L;

    @TableField("goods_id")
    private Long goodsId;
    @TableField("label_id")
    private Long labelId;
    @TableField("shop_id")
    private Long shopId=1l;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
