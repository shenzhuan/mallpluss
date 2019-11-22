package com.mei.zhuang.entity.order;

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
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_delivery_invoice")
public class EsDeliveryInvoice extends Model<EsDeliveryInvoice> {

    private static final long serialVersionUID = 1L;

    /**
     * 公司
     */
    private String company;
    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
    /**
     * 是否默认： 0：不默认  1：默认
     */
    @TableField("is_default")
    private Integer isDefault;
    /**
     * 值
     */
    private String value;
    /**
     * 商品
     */
    @TableField("goods_value")
    private String goodsValue;
    private Long id;
    /**
     * 图片
     */
    private String image;
    /**
     * 名称
     */
    private String name;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 修改时间
     */
    @TableField("updated_time")
    private Date updatedTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
