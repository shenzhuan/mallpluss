package com.mei.zhuang.vo;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-05-28 14:18
 * @Description:
 */
@Data
public class ShopParam {
    /**
     * 值可能是 品牌名| 店铺名称
     */
    private String keyWord;

    /**
     * 店铺类型： -1：全部  1：小程序
     */
    private Integer typeId;

    private Integer current = 1;

    private Integer size = 10;

    private Integer isAsc = 0;

    private Integer isDelete = 0;

}
