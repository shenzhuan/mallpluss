package com.mei.zhuang.entity.marking;

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
 * @author arvato team
 * @since 2019-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_full_gift_goods_map")
public class EsShopFullGiftGoodsMap extends Model<EsShopFullGiftGoodsMap> {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 满赠礼id
     */
    @TableField("full_gift_id")
    private Long fullGiftId;
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 商品类型 1商品 2 赠品
     */
    @TableField("goods_type")
    private Integer goodsType;
    /**
     * 规格
     */
    @TableField("spec_ids")
    private String specIds;
    @TableField("rule_id")
    private Long ruleId;
    //赠品名称
    @TableField("goods_name")
    private String goodsName;
    //图片
    private String pic;
    /**
     * 活动名称
     */
    @TableField("activity_name")
    private String activityName;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
