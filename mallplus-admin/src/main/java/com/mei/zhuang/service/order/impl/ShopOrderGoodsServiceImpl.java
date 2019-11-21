package com.mei.zhuang.service.order.impl;

import com.arvato.service.order.api.feigin.MarkingFegin;
import com.arvato.service.order.api.orm.dao.EsShopOrderGoodsMapper;
import com.arvato.service.order.api.orm.dao.EsShopOrderMapper;
import com.arvato.service.order.api.service.ShopOrderGoodsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.marking.EsShopFriendGift;
import com.mei.zhuang.entity.marking.EsShopFriendGiftCard;
import com.mei.zhuang.entity.order.EsShopOrder;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: Tiger
 * @Date: 2019-05-10 16:36
 * @Description:
 */
@Service
public class ShopOrderGoodsServiceImpl extends ServiceImpl<EsShopOrderGoodsMapper, EsShopOrderGoods> implements ShopOrderGoodsService {

    @Resource
    private EsShopOrderGoodsMapper orderGoodsMapper;
    @Resource
    private MarkingFegin markingFegin;
    @Resource
    private EsShopOrderMapper orderMapper;
    @Override
    public EsShopOrderGoods orderlist(long orderId) {
        EsShopOrder Order = orderMapper.selectById(orderId);
        EsShopOrderGoods OrderGoods = orderGoodsMapper.orderid(orderId);
        OrderGoods.setOrder(Order);
        EsShopFriendGiftCard GiftCard = markingFegin.GiftCard(Order.getGiftId());
        OrderGoods.setGiftCardList(GiftCard);
        EsShopFriendGift list = markingFegin.list();
        OrderGoods.setFriendGift(list);
        return OrderGoods;
    }
}
