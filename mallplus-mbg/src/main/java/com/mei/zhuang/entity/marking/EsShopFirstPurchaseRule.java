package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("es_shop_first_purchase_rule")
public class EsShopFirstPurchaseRule extends Model<EsShopFirstPurchaseRule> {

    private static final long serialVersionUID = 1L;

    /**
     * 活动类型
     */
    @TableField("activity_type")
    private Integer activityType;
    /**
     * 首购礼id
     */
    @TableField("first_purchase_id")
    private Long firstPurchaseId;
    /**
     * 活动名称
     */
    @TableField("activity_name")
    private String activityName;


    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 赠品id
     */
    @TableField("gifts_id")
    private Long giftsId;
    @TableField("goods_name")
    private String goodsName;

    private String pic;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
