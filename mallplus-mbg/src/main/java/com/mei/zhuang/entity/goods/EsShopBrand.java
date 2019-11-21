package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 品牌
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_brand")
public class EsShopBrand {

    private Long id;

    private Long brandId;

    private String brandName;

    private String contactMobile;

    private String contactName;

    private String logo;

    private Integer status;

    private Date createTime;


}
