package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

//规则关联商品
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_rules_spec")
public class EsShopGoodsRulesSpec extends Model<EsShopGoodsRulesSpec> {
    @TableField("rules_id")
    private long rulesId;
    @TableField("goods_id")
    private Long goodsId;
    @TableField("spec_ids")
    private String specIds;
    @Override
    protected Serializable pkVal() {
        return null;
    }
}
