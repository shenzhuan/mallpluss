package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-07-04 9:57
 * @Description: 微信Vo类
 */
@Data
public class Wx_CustAgeInfo {

    private Integer id;//微信name 对应id

    private String name;//微信属性名称

    private Integer value;//value值

    private double scale;//比例

    public Wx_CustAgeInfo(Integer id, String name, Integer value, double scale) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.scale = scale;
    }
}
