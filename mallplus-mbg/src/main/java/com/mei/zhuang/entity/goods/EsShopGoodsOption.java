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
 * goods_option规格选项的扩展表
 * </p>
 *
 * @author arvato team
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_option")
public class EsShopGoodsOption extends Model<EsShopGoodsOption> {

    private static final long serialVersionUID = 1L;

    private String thumb;
    private BigDecimal weight;  //重量
    @TableField("display_order")
    private Integer displayOrder;//显示次序
    @TableField("goods_id")
    private Long goodsId;//商品编号
    private Long id;//编号
    private Long sales;
    @TableField("shop_id")
    private Long shopId;
    private String title;//规格名称
    @TableField("goods_code")
    private String goodsCode ;//编码
    @TableField("suit_code")
    private String suitCode;//套装编码
    @TableField("color_test_code")
    private String colorTestCode;//试色编码
    @TableField("productsn")
    private String productsn ;//条码
    @TableField("goods_name")
    private String goodsName;//商品名称
    private BigDecimal price;//价格
    private String specs;//规格
    @TableField("spec_ids")
    private String specIds;//规格编号
    private Integer stock;//商品库存
    @TableField("stock_warning")
    private Integer stockWarning;
    @TableField("is_default")
    private Integer  isDefault;// 字段 1是默认 2非默认

    private String banner;//规格的轮播图

    private String shareimg;//规格的分享图
    @TableField("virtual_stock")
    private Integer virtualStock;//规格的虚拟库存


    private BigDecimal marketprice;//现价
    private BigDecimal costprice;//原价
    private BigDecimal productprice;//预售价
    @TableField(exist = false)
    private Integer current =1;
    @TableField(exist = false)
    private Integer size = 10;
    @TableField(exist = false)
    private String optionList;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
