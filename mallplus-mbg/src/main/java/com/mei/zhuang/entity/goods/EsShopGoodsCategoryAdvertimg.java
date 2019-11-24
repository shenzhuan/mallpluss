package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_category_advert_img")
public class EsShopGoodsCategoryAdvertimg extends Model<EsShopGoodsCategoryAdvertimg> {

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    @TableField("category_id")
    private Long categoryId;//分类广告id
    @TableField("categoru_adimg")
    private String categoruAdimg;//分类广告图片
    @TableField("categoru_adaddress")
    private String categoruAdaddress;//分类广告地址

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
