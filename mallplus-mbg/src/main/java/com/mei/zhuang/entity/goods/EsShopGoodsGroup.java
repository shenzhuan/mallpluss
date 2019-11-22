package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@TableName("es_shop_goods_group")
public class EsShopGoodsGroup extends Model<EsShopGoodsGroup> {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;  //商品组名称
    @TableField(exist = false)
    private Long goodsId;    //商铺ID
    @TableField("sort_type")
    private String sortType;//分类类型
    @TableField("create_time")
    private Date createTime;//创建时间
    @TableField("goods_num")
    private Integer goodsNum;   //数量
    @TableField("status")
    private Integer status;//状态

    @TableField(exist = false)
    private List<EsShopGoods> listGoods;


    @TableField(exist = false)
    private String goodsIds;
    @TableField(exist = false)
    private Integer current = 0;
    @TableField(exist = false)
    private Integer size = 10;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
