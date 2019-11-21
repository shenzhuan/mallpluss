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
@TableName("es_member_tag_goods")
public class EsMemberTagGoods extends Model<EsMemberTagGoods> {

    private static final long serialVersionUID = 1L;

    @TableField("goods_id")
    private Integer goodsId;
    @TableField("shop_id")
    private Integer shopId;
    @TableField("tag_id")
    private Integer tagId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
