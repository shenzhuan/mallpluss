package com.mei.zhuang.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsShopOrderGoods;

/**
 * @Auther: Tiger
 * @Date: 2019-05-10 16:34
 * @Description:
 */
public interface ShopOrderGoodsService extends IService<EsShopOrderGoods> {

    //拆礼物
    EsShopOrderGoods orderlist(long orderId);
}
