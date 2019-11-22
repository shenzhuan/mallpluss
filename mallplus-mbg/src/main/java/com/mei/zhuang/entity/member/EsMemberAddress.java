package com.mei.zhuang.entity.member;

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
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_member_address")
public class EsMemberAddress extends Model<EsMemberAddress> {

    private static final long serialVersionUID = 1L;

    private String city;
    private String lat;
    private String lng;
    private String name;
    private String province;
    @TableField("area_code")
    private Integer areaCode;
    @TableField("city_code")
    private Integer cityCode;
    private Integer id;
    @TableField("is_default")
    private Long isDefault;
    private String mobile;
    private String address;
    private String area;
    private Long deleted;
    @TableField("province_code")
    private Integer provinceCode;
    @TableField("member_id")
    private Integer memberId;
    @TableField("shop_id")
    private Integer shopId;
    @TableField("zip_code")
    private String zipCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
