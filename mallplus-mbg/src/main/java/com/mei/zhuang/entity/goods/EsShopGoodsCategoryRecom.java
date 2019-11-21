package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_category_recom")
public class EsShopGoodsCategoryRecom extends Model<EsShopGoodsCategoryRecom> {

    @TableField("category_id")
    private Long categoryId;
    @TableField("goods_id")
    private Long goodsId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}

