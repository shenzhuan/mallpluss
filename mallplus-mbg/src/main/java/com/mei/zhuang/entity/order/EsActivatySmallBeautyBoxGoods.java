package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 小美盒活动--产品下的商品
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_activaty_small_beauty_box_goods")
public class EsActivatySmallBeautyBoxGoods extends Model<EsActivatySmallBeautyBoxGoods> {

    /**
     * 编号
     */
    private Long id;

    /**
     * 序号
     */
    @TableField(exist = false)
    private Integer serialNember;

    /**
     * 活动ID
     */
    @TableField("activaty_id")
    private Long activatyId;
    /**
     * 步数
     */
    private Integer product;
    /**
     * 产品名称
     */
    @TableField("product_name")
    private String productName;
    /**
     * 副标题
     */
    private String subtitle;
    /**
     * 产品编码
     */
    @TableField("product_code")
    private String productCode;
    /**
     * 产品图片
     */
    @TableField("product_img")
    private String productImg;
    /**
     * 赠品名称
     */
    @TableField("gift_name")
    private String giftName;
    /**
     * 赠品编码
     */
    @TableField("gift_code")
    private String giftCode;
    /**
     * 赠品图片
     */
    @TableField("gift_img")
    private String giftImg;
    @TableField(exist = false)
    private Integer current = 1;
    @TableField(exist = false)
    private Integer size = 10;
    @TableField(exist = false)
    private String date;
    @TableField(exist = false)
    private Long memberId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
