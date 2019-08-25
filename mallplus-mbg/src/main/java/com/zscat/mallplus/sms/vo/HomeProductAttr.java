package com.zscat.mallplus.sms.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HomeProductAttr {
    private Long id;
    private String productImg;
    private String productName;
    private BigDecimal productPrice;
}
