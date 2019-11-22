package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
@TableName("es_shop_full_gift_rule")
public class EsShopFullGiftRule extends Model<EsShopFullGiftRule> {

    private static final long serialVersionUID = 1L;

    /**
     * 活动类型
     */
    @TableField("activity_type")
    private Integer activityType;
    /**
     * 满赠礼id
     */
    @TableField("full_gift_id")
    private Long fullGiftId;
    private Long id;
    @TableField("shop_id")
    private Long shopId;
    /**
     * 消费满元
     */
    private BigDecimal price;
    /**
     * 0：无条件 1 ，与 2 或
     */
    private Integer conditions;
    /**
     * 购满 件
     */
    private Integer amount;
    /**
     * 选赠
     */
    @TableField("song_amount")
    private Integer songAmount;
    //满赠礼优先级
    @TableField("full_level")
    private Integer fullLevel;

    /**
     * 活动名称
     */
    @TableField("activity_name")
    private String activityName;
    //小程序
    @TableField(exist = false)
    private List<EsShopFullGiftGoodsMap> fullGiftGoodsMapList;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
