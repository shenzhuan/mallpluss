package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 商品与模板关联表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_diypage_map")
public class EsShopGoodsDiyPageMap extends Model<EsShopGoodsDiyPageMap> {

    private Long id;

    @TableField("goods_id")
    private Long goodsId;

    @TableField("diypage_id")
    private Long diyPageId;
    @TableField("shop_id")
    private Long shopId;
    private String data;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
