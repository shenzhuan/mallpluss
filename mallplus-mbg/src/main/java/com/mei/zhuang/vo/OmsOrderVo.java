package com.mei.zhuang.vo;

import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019/7/15.
 */
@Data
public class OmsOrderVo  implements Serializable{
    private EsShopOrder order;
    private List<EsShopOrderGoods> orderGoodsList;
    private EsMember member;
}
