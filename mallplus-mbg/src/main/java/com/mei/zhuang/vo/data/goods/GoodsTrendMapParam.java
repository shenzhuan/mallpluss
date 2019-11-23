package com.mei.zhuang.vo.data.goods;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 18:05
 * @Description:商品趋势图选择参数
 */
@Data
public class GoodsTrendMapParam {

    private String source;//来源

    private String startTime;//开始时间

    private String endTime;//结束时间

    private Long shopId=1l;//店铺id

}
