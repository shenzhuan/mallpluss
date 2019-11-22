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
 * 商品详情的推荐商品
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_recom")
public class EsShopGoodsRecom extends Model<EsShopGoodsRecom> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;
    /**
     * 推荐商品名称
     */
    @TableField("title")
    private String title;

    /**
     * 推荐商品id
     */
    @TableField("recom_goods_id")
    private Long recomGoodsId;
    /**
     * 推荐商品价格
     */
    @TableField("price")
    private BigDecimal price;
    /**
     * 推荐虚拟商品的库存
     */
    @TableField("vitural_stock")
    private Integer vituralStock;

    @TableField("thumb")
    private String thumb;
    /**
     * 商品id
     */
    private Long goodsid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
