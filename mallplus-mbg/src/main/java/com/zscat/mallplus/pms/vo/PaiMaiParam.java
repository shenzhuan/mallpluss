package com.zscat.mallplus.pms.vo;


import com.zscat.mallplus.pms.entity.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 创建和修改商品时使用的参数
 * https://github.com/shenzhuan/mallplus on 2018/4/26.
 */
@Data
public class PaiMaiParam implements Serializable {
    private Long id;
    private BigDecimal price;
}
