package com.zscat.mallplus.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 首页推荐品牌表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Setter
@Getter
@TableName("sms_home_brand")
public class SmsHomeBrand extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("brand_id")
    private Long brandId;

    @TableField("brand_name")
    private String brandName;

    @TableField("recommend_status")
    private Integer recommendStatus;

    private Integer sort;

    private String logo;

}
