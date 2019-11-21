package com.mei.zhuang.entity.member;

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
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_member_level")
public class EsMemberLevel extends Model<EsMemberLevel> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    @TableField("upgrade_type")
    private Long upgradeType;
    private String description;
    private String discount;
    @TableField("goods_ids")
    private String goodsIds;
    private Long status;
    private Integer level;
    @TableField("order_money")
    private String orderMoney;
    @TableField("shop_id")
    private Integer shopId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
