package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * goods_spec_item
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_spec_item")
public class EsShopGoodsSpecItem extends Model<EsShopGoodsSpecItem> {

    private static final long serialVersionUID = 1L;

    @TableField("goods_id")
    private Long goodsId;
    private Long id;
    @TableField("shop_id")
    private Long shopId=1l;
    @TableField("spec_id")
    private Long specId;
    private String title;
    @TableField("title_item")
    private String titleItem;//规格名称
    @TableField("display_order")
    private Integer displayOrder;
    @TableField("option_id")
    private Integer optionId;//规格扩展的id（goods_option的id）

    private String show;//规格选项的显示

    private Integer status;//规格选项的默认值（1-默认、2-不默认

    private Integer type;//规格选项的类型（1-文字、2-颜色、3-图片）

    private String typeword;//规格选项的内容（和type一一对应）
    private BigDecimal money;//规格选项的价格

    private Integer level;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
