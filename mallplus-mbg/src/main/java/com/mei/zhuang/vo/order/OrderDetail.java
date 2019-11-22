package com.mei.zhuang.vo.order;

import com.mei.zhuang.entity.marking.EsShopFriendGiftCard;
import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/13 18:13
 * @Description:
 */
@Data
public class OrderDetail {
    private EsShopOrder order;
    private List<EsShopOrderGoods> orderGoodsList;
    private EsShopFriendGiftCard giftCard;
    private List<EsShopOrderGoods> gift1 = new ArrayList<>();
    private List<EsShopOrderGoods> gift2 = new ArrayList<>();
    private List<EsShopOrderGoods> gift3 = new ArrayList<>();
    private List<EsShopOrderGoods> gift4 = new ArrayList<>();
    private List<EsShopOrderGoods> gift5 = new ArrayList<>();
    private List<EsShopOrderGoods> gift6 = new ArrayList<>();
}
