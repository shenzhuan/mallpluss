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
@TableName("es_shop_goods_cate_map")
public class EsShopGoodsCateMap extends Model<EsShopGoodsCateMap> {

    private static final long serialVersionUID = 1L;

    @TableField("category_id")
    private Long categoryId;
    @TableField("goods_id")
    private Long goodsId;
    @TableField("shop_id")
    private Long shopId;
    @TableField("category_name")
    private String categoryName;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
