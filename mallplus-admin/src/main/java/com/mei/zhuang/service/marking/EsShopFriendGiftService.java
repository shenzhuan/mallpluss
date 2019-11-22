package com.mei.zhuang.service.marking;

import com.mei.zhuang.entity.marking.EsShopFriendGift;
import com.mei.zhuang.entity.marking.EsShopFriendGiftCard;

public interface EsShopFriendGiftService {

    boolean save(EsShopFriendGift friendGift);

    Integer update(EsShopFriendGift friendGift);

    EsShopFriendGift friend();

    EsShopFriendGift detail(long id);

    EsShopFriendGiftCard giftcard(long id);


}
