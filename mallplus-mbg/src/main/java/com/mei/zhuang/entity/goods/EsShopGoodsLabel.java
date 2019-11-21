package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author arvato team
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_label")
public class EsShopGoodsLabel extends Model<EsShopGoodsLabel> {

    private static final long serialVersionUID = 1L;

    private String desc;
    private Long id;
    private String name;
    @TableField("shop_id")
    private Long shopId;
    @TableField("sort_type")
    private String sortType;
    @TableField("create_time")
    private Date createTime;
    @TableField(exist = false)
    private Integer current = 0;
    @TableField(exist = false)
    private Integer size = 10;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
