package com.mei.zhuang.vo.order;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-05-31 15:40
 * @Description:
 */
@Data
public class PaySettingParam {


    private Integer current = 1;
    private Integer size = 10;
    private Integer isAsc = 0;
    private Integer isDelete = 0;
}
