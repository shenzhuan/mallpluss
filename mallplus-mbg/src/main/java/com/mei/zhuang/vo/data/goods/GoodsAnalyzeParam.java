package com.mei.zhuang.vo.data.goods;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 17:26
 * @Description:商品分析参数
 */
@Data
public class GoodsAnalyzeParam {

    private String source;//来源

        private String startTime;//开始时间

    private String endTime;//结束时间

    private Long shopId;//店铺id

}
