package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 小美盒定制——定制礼盒
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_activaty_small_beauty_box_gift_box")
public class EsActivatySmallBeautyBoxGiftBox extends Model<EsActivatySmallBeautyBoxGiftBox> {

    /**
     *
     */
    private Long id;
    /**
     *礼盒名称
     */
    @TableField("box_name")
    private String boxName;
    /**
     *礼盒价格
     */
    @TableField("box_money")
    private BigDecimal boxMoney;
    /**
     *礼盒编码
     */
    @TableField("box_code")
    private String boxCode;
    /**
     *礼盒虚拟库存
     */
    @TableField("vitural_stock")
    private Integer vituralStock;
    /**
     *礼盒库存
     */
    @TableField("stock")
    private Integer stock;
    /**
     *礼盒图片
     */
    @TableField("box_img")
    private String boxImg;
    /**
     *所属活动
     */
    @TableField("activaty_id")
    private Long activatyId;
    /**
     *所属活动
     */
    @TableField(exist = false)
    private String activatyName;
    /**
     *填写产品编码（用，隔开）
     */
    @TableField("product_code")
    private String productCode;
    /**
     *赠品管理
     */
    @TableField("gift_date")
    private String giftDate;

    @TableField(exist = false)
    private Long memberId;
    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
