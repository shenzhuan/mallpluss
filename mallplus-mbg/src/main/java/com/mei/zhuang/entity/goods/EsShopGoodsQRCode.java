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
@TableName("es_shop_goods_qrcode")
public class EsShopGoodsQRCode extends Model<EsShopGoodsQRCode> {

    @TableField("goods_id")
    private Long goodsId;
    @TableField("img_base")
    private String imgBase;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
