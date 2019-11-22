package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 13:42
 * @Description: 地域分析Vo
 */
@Data
public class CustAreaAnalyzeInfoVo {

    private int top; //排名

    private String area;//地区（省份）

    private int custCountByArea;//地区对应的总人数

    private double proCountToSale;//省份人数占比

    private Integer id;//微信对应省份的id

    public CustAreaAnalyzeInfoVo() {
    }

    public CustAreaAnalyzeInfoVo(String area, int custCountByArea, Integer id, double proCountToSale) {
        this.area = area;
        this.custCountByArea = custCountByArea;
        this.id = id;
        this.proCountToSale = proCountToSale;
    }

    public CustAreaAnalyzeInfoVo(int top, String area, int custCountByArea, Integer id, double proCountToSale) {
        this.top = top;
        this.area = area;
        this.custCountByArea = custCountByArea;
        this.id = id;
        this.proCountToSale = proCountToSale;
    }


}
