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
 * @author meizhuang team
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_params")
public class EsShopGoodsParams extends Model<EsShopGoodsParams> {

    private static final long serialVersionUID = 1L;

    @TableField("create_time")
    private Date createTime;
    private Long id;
    private String img;
    private String name;
    @TableField("shop_id")
    private Long shopId=1l;
    private String text;
    private Integer type;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
