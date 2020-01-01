package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long brandNo;
    private Long brandId;
    private String brandName;

    private String contactMobile;

    private String contactName;

    private String logo;

    private Integer isDelete;

    private Date createTime;


}
