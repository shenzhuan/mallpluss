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
 * @author meizhuang team
 * @since 2019-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_first_purchase_goods_map")
public class EsShopFirstPurchaseGoodsMap extends Model<EsShopFirstPurchaseGoodsMap> {

    private static final long serialVersionUID = 1L;

    /**
     * 首购礼id
     */
    @TableField("first_purchase_id")
    private Long firstPurchaseId;
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 规格
     */
    @TableField("spec_ids")
    private String specIds;
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
