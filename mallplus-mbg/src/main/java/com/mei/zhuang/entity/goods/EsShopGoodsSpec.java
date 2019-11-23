package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 规格信息表
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_spec")
public class EsShopGoodsSpec extends Model<EsShopGoodsSpec> {

    private static final long serialVersionUID = 1L;

    @TableField("goods_id")
    private Long goodsId;//商品id
    private Long id;
    @TableField("shop_id")
    private Long shopId=1l;//品牌的id
    private String title;//规格名称

    @TableField("is_mainspec")
    private Integer isMainSpec;//是否是主规格

    @TableField("display_order")
    private Integer displayOrder;//规格的排序


    @TableField(exist = false)
    private List<EsShopGoodsSpecItem> itemList;

    private String content;//规格的id合并


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
