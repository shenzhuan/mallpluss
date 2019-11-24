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
@TableName("es_member_goods")
public class EsMemberGoods extends Model<EsMemberGoods> {

    private static final long serialVersionUID = 1L;

    @TableField("shop_id")
    private Integer shopId;
    @TableField("goods_id")
    private Integer goodsId;
    @TableField("has_option")
    private Long hasOption;
     @TableId(value = "id", type = IdType.AUTO)     private Integer id;
    private String rule;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
