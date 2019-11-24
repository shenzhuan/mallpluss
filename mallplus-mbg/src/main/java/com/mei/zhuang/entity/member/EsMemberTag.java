package com.mei.zhuang.entity.member;

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
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_member_tag")
public class EsMemberTag extends Model<EsMemberTag> {

    private static final long serialVersionUID = 1L;

    @TableField("goods_ids")
    private String goodsIds;
     @TableId(value = "id", type = IdType.AUTO)     private Integer id;
    @TableField("member_balance")
    private String memberBalance;
    private String name;
    @TableField("auto_make")
    private String autoMake;
    @TableField("member_credit")
    private Integer memberCredit;
    @TableField("order_count")
    private Integer orderCount;
    @TableField("order_money")
    private String orderMoney;
    @TableField("shop_id")
    private Integer shopId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
