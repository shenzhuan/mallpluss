package com.mei.zhuang.vo.marking;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/6 11:28
 * @Description:
 */
@Data
public class GoodsVo implements Serializable {
    private static final long serialVersionUID = 1L;
    //优惠券管理的条件
    private Integer type;

    private Integer channelId;

    private Date endTime;

    private Date startTime;

    private String couponsName;


}
