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
@TableName("es_delivery_templet")
public class EsDeliveryTemplet extends Model<EsDeliveryTemplet> {

    private static final long serialVersionUID = 1L;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;
    /**
     * 值
     */
    private String value;
    /**
     * 订单类型
     */
    private String company;
    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
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
    private Long id;
    /**
     * 是否默认： 0：不默认 1： 默认
     */
    @TableField("is_default")
    private Integer isDefault;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
