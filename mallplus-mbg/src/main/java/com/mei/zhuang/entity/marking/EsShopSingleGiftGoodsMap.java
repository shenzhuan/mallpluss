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
@TableName("es_shop_single_gift_goods_map")
public class EsShopSingleGiftGoodsMap extends Model<EsShopSingleGiftGoodsMap> {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 满赠礼id
     */
    @TableField("single_gift_id")
    private Long singleGiftId;
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 商品类型 1商品1 ，2商品2  ，3 赠品
     */
    @TableField("goods_type")
    private Integer goodsType;
    /**
     * 规格
     */
    @TableField("spec_ids")
    private String specIds;
    @TableField("goods_name")
    private String goodsName;

    private String pic;
    /**
     * 活动名称
     */
    @TableField("activity_name")
    private String activityName;
    @TableField(exist = false)
    private Integer count;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
