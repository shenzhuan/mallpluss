package com.zscat.mallplus.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.pms.entity.PmsProduct;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 砍价免单设置表
 * </p>
 *
 * @author zscat
 * @since 2019-12-25
 */
@Setter
@Getter
@TableName("sms_bargain_config")
public class SmsBargainConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    PmsProduct goods;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 每次砍价的参数
     */
    private String parameter;
    /**
     * 逾期失效时间
     */
    @TableField("invalid_time")
    private Date invalidTime;
    /**
     * 修改时间
     */
    @TableField("create_time")
    private Date createTime;
    @TableField("store_id")
    private Integer storeId;
    private Integer pepoles;
    /**
     * 商品
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 原价
     */
    @TableField("origin_price")
    private BigDecimal originPrice;
    /**
     * 成功价
     */
    private BigDecimal price;
    @TableField("goods_name")
    private String goodsName;
    private String pic;

}
