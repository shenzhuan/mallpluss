package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 13:32
 * @Description:客户属性年龄占比  （不用）
 */
@Data
public class CustPropAgeInfoVo {

    private int ageRangeCount1;//0-19岁总人数
    private double ageRangeToScale1;//0-19岁占比例

    private int ageRangeCount2;//20-29岁总人数
    private double ageRangeToScale2;//20-29岁占比例

    private int ageRangeCount3;//30-39岁总人数
    private double ageRangeToScale3;//30-39岁占比例

    private int ageRangeCount4;//40-49岁总人数
    private double ageRangeToScale4;//40-49岁占比例

    private int ageRangeCount5;//50岁以上总人数
    private double ageRangeToScale5;//50岁以上占比例

    private int ageRangeCount6;//其他岁总人数
    private double ageRangeToScale6;//其他岁占比例

    public CustPropAgeInfoVo(){

    }

    public CustPropAgeInfoVo(int ageRangeCount1, double ageRangeToScale1, int ageRangeCount2, double ageRangeToScale2, int ageRangeCount3, double ageRangeToScale3, int ageRangeCount4, double ageRangeToScale4, int ageRangeCount5, double ageRangeToScale5, int ageRangeCount6, double ageRangeToScale6) {
        this.ageRangeCount1 = ageRangeCount1;
        this.ageRangeToScale1 = ageRangeToScale1;
        this.ageRangeCount2 = ageRangeCount2;
        this.ageRangeToScale2 = ageRangeToScale2;
        this.ageRangeCount3 = ageRangeCount3;
        this.ageRangeToScale3 = ageRangeToScale3;
        this.ageRangeCount4 = ageRangeCount4;
        this.ageRangeToScale4 = ageRangeToScale4;
        this.ageRangeCount5 = ageRangeCount5;
        this.ageRangeToScale5 = ageRangeToScale5;
        this.ageRangeCount6 = ageRangeCount6;
        this.ageRangeToScale6 = ageRangeToScale6;
    }
}
