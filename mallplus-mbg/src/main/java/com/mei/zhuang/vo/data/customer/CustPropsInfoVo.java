package com.mei.zhuang.vo.data.customer;

import lombok.Data;

import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-07-19 10:14
 * @Description:
 */
@Data
public class CustPropsInfoVo {

    private CustPropSexInfoVo custPropSexInfoVo;//客户属性性别

    private List<Wx_CustAgeInfo> wxCustAgeList;//客户属性年龄

    private List<CustAreaAnalyzeInfoVo> areaAnalyzeInfoVoList;//地域(已排好序，数组位置顺序即为排名顺序)

    private String refDate;//关系日期


}
