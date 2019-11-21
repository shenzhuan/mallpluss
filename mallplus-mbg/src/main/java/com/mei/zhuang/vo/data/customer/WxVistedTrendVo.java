package com.mei.zhuang.vo.data.customer;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-28 16:17
 * @Description:
 */
@Data
public class WxVistedTrendVo {

    private String ref_date;//时间，格式为 yyyymm，如："201702"

    private int session_cnt;//打开次数（自然月内汇总）

    private int visit_pv;//访问次数（自然月内汇总）

    private int visit_uv;//访问人数（自然月内去重）

    private int visit_uv_new;//新用户数（自然月内去重）

    private double stay_time_session;//4 人均停留时长 (浮点型，单位：秒)

    private double visit_depth;//4平均访问深度 (浮点型)




}
