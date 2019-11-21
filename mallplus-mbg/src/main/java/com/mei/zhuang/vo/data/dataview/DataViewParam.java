package com.mei.zhuang.vo.data.dataview;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-08-08 14:36
 * @Description:数据概览vo
 */
@Data
public class DataViewParam {

    private String startTime;

    private String endTime;

    private Long shopId;

    private String source;


}
