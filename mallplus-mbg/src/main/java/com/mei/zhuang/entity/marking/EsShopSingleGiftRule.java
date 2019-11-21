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
@TableName("es_shop_single_gift_rule")
public class EsShopSingleGiftRule extends Model<EsShopSingleGiftRule> {

    private static final long serialVersionUID = 1L;

    /**
     * 活动类型 1：仅送一件 2：按购买件数送 3：指定件数送【满多少送多少】
     */
    @TableField("activity_type")
    private Integer activityType;
    /**
     * 满赠礼id
     */
    @TableField("single_gift_id")
    private Long singleGiftId;
    private Long id;
    @TableField("shop_id")
    private Long shopId;
    /**
     * 满多少件
     */
    @TableField("alread_amount")
    private Integer alreadAmount;
    /**
     * 赠多少件
     */
    @TableField("song_amount")
    private Integer songAmount;
    /**
     * 最多赠
     */
    @TableField("max_amount")
    private Integer maxAmount;

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
