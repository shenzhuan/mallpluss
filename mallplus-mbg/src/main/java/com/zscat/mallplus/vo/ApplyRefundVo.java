package com.zscat.mallplus.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplyRefundVo {
    private Long itemId;
    private Integer type;
    private String desc;
    private String[] images;
}
