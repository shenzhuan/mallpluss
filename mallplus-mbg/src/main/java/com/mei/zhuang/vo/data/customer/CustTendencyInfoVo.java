package com.mei.zhuang.vo.data.customer;


import com.mei.zhuang.utils.DateUtil;
import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-07-04 17:35
 * @Description:客户趋势图数据Vo
 */
@Data
public class CustTendencyInfoVo implements Comparable<CustTendencyInfoVo> {

//    private String dataTypeEn;//数据条件中文

//    private Integer dataType;//1.成交次数 2. 支付订单数 3. 客单价 4.支付金额

    public static boolean isAsc = false;//升序(true)|降序(false)
    private String newCustValue;//新客户所对应的数据
    private String oldCustValue;//老客户所对应的数据
    private String refDate;//关系日期

    public CustTendencyInfoVo() {
    }

    public CustTendencyInfoVo(String newCustValue, String oldCustValue, String refDate) {
        this.newCustValue = newCustValue;
        this.oldCustValue = oldCustValue;
        this.refDate = refDate;
    }

    @Override
    public int compareTo(CustTendencyInfoVo o) {
        return CustTendencyInfoVo.isAsc == false ?
                new Long(DateUtil.getDaySub(o.refDate, this.refDate, DateUtil.YYYY_MM_DD)).intValue()
                : new Long(DateUtil.getDaySub(this.refDate, o.refDate, DateUtil.YYYY_MM_DD)).intValue();
    }


}
