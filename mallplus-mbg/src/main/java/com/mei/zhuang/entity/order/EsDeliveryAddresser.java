package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("es_delivery_addresser")
public class EsDeliveryAddresser extends Model<EsDeliveryAddresser> {

    private static final long serialVersionUID = 1L;

    /**
     * 详细地址
     */
    private String address;
    private Long id;
    /**
     * 邮政编码
     */
    private String postcode;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 地址状态
     */
    @TableField("addresser_sign")
    private String addresserSign;
    /**
     * 地区
     */
    private String area;
    /**
     * 城市
     */
    private String city;
    /**
     * 是否默认 ： 0：不默认 1：默认
     */
    @TableField("is_default")
    private Integer isDefault;
    /**
     * 收件人姓名
     */
    private String name;
    /**
     * 电话
     */
    private String phone;
    /**
     * 省份
     */
    private String province;

    @TableField("member_id")
    private Long memberId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
