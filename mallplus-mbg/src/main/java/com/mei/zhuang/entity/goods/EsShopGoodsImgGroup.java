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
@TableName("es_shop_goods_img_group")
public class EsShopGoodsImgGroup extends Model<EsShopGoodsImgGroup> {

    private Long id;
    @TableField("group_name")
    private String groupName;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
