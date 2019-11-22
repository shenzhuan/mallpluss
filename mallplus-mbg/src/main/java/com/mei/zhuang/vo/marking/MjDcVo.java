package com.mei.zhuang.vo.marking;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2019/7/15.
 */
@Data
public class MjDcVo implements Serializable {
    private Long id;
    private BigDecimal basicAmount = new BigDecimal(0);
}
