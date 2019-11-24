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
@TableName("es_member_goods_option")
public class EsMemberGoodsOption extends Model<EsMemberGoodsOption> {

    private static final long serialVersionUID = 1L;

    @TableField("shop_id")
    private Integer shopId;
    @TableField("goods_id")
    private Integer goodsId;
     @TableId(value = "id", type = IdType.AUTO)     private Integer id;
    @TableField("option_id")
    private Integer optionId;
    private String rule;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
