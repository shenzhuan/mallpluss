package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 13:26
 * @Description:客户属性性别占比
 */
@Data
public class CustPropSexInfoVo {

    private int totalCustCount;//总客户数

    private int maleCount;//男性总数量
    private double maleToScale;//男性占比

    private int femaleCount;//女性总数量
    private double femaleToScale;//女性占比

    private int unknownCount;//未知总数量
    private double unknownToScale;//未知占比

    private String refDate;//关系日期

    public CustPropSexInfoVo(){}

    public CustPropSexInfoVo(int totalCustCount, int maleCount, double maleToScale, int femaleCount, double femaleToScale, int unknownCount, double unknownToScale, String refDate) {
        this.totalCustCount = totalCustCount;
        this.maleCount = maleCount;
        this.maleToScale = maleToScale;
        this.femaleCount = femaleCount;
        this.femaleToScale = femaleToScale;
        this.unknownCount = unknownCount;
        this.unknownToScale = unknownToScale;
        this.refDate = refDate;
    }
}
