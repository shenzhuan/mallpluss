package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_package_goods_spec")
public class EsShopPackageGoodsSpec extends Model<EsShopPackageGoodsSpec> {
    @TableField("package_id")
    private long packageId;
    @TableField("goods_id")
    private long goodsId;
    @TableField("specs_id")
    private String specsId;
    @TableField("package_price")
    private BigDecimal packagePrice;
    @TableField(exist = false)
    private String specprice;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
