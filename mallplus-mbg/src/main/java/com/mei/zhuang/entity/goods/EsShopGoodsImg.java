package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_img")
public class EsShopGoodsImg extends Model<EsShopGoodsImg> {

    private Long id;
    @TableField("img_group_id")
    private Long imgGroupId;
    @TableField("img_address")
    private String imgAddress;
    @TableField("img_name")
    private String imgName;

    @TableField(exist = false)
    private Integer current = 1;
    @TableField(exist = false)
    private Integer size = 10;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
