package com.mei.zhuang.entity.marking;


import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//规则商品
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_rules")
public class EsShopGoodsRules extends Model<EsShopGoodsRules> {
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    private String goodsname;
    private BigDecimal price;
    @TableField("create_time")
    private Date createTime;
    //1.搭配换购
    @TableField("rule_conditions")
    private String ruleConditions;
    // 1.参与换购商品，2.不参与
    @TableField("goods_scope")
    private String goodsScope;
    @TableField("goods_id")
    private Long goodsId;
    //1.显示  2.隐藏
    private int according = 1;

    @TableField(exist = false)
    private List<GoodsSepcVo> Listrulesgoods;
    @TableField(exist = false)
    private String rulegoods;
    @TableField(exist = false)
    private Integer total;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
