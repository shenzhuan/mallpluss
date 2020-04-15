package com.zscat.mallplus.pms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_group_map")
public class EsShopGoodsGroupMap implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableField("goods_id")
    private Long goodsId;
    @TableField("group_id")
    private Long groupId;


}
