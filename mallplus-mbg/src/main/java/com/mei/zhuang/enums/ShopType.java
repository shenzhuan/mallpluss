package com.mei.zhuang.enums;

/**
 * @Auther: Tiger
 * @Date: 2019-05-30 16:32
 * @Description:
 */
public enum ShopType {


    APPLET(1),//小程序
    OTHER(2);//其他

    private Integer value;

    ShopType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    ;

}
