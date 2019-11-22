package com.mei.zhuang.service.marking.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.marking.EsShopFriendGiftCardMapper;
import com.mei.zhuang.dao.marking.EsShopFriendGiftMapper;
import com.mei.zhuang.entity.marking.EsShopFriendGift;
import com.mei.zhuang.entity.marking.EsShopFriendGiftCard;
import com.mei.zhuang.service.marking.EsShopFriendGiftService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EsShopFriendGiftServiceImpl extends ServiceImpl<EsShopFriendGiftMapper, EsShopFriendGift> implements EsShopFriendGiftService {

   @Resource
   private EsShopFriendGiftMapper friendGiftMapper;
   @Resource
   private EsShopFriendGiftCardMapper friendGiftCardMapper;


    @Override
    public boolean save(EsShopFriendGift friendGift) {
        friendGiftMapper.insert(friendGift);
        giftcard(friendGift);
        return true;
    }

    public void giftcard(EsShopFriendGift friendGift){
        if(friendGift.getFriendgifcard()!=null&&friendGift.getFriendgifcard().size()>0){
            for(EsShopFriendGiftCard card:friendGift.getFriendgifcard()){
                EsShopFriendGiftCard giftcard=new EsShopFriendGiftCard();
                giftcard.setGiftId(friendGift.getId());
                giftcard.setTitle(card.getTitle());
                giftcard.setGiftPicture(card.getGiftPicture());
                friendGiftCardMapper.insert(giftcard);
            }
        }
    }

    @Override
    public Integer update(EsShopFriendGift friendGift) {
        friendGiftMapper.updateById(friendGift);
        friendGiftCardMapper.updatecard(friendGift.getId());
        giftcard(friendGift);
        return 1;
    }

    @Override
    public EsShopFriendGift friend() {
        EsShopFriendGift friend = friendGiftMapper.friend();
        if(friend!=null) {
            List<EsShopFriendGiftCard> gift_id = friendGiftCardMapper.selectList(new QueryWrapper<EsShopFriendGiftCard>().eq("gift_id", friend.getId()).eq("according",1));
            friend.setFriendgifcard(gift_id);
        }
        return friend;
    }


    @Override
    public EsShopFriendGift detail(long id) {
        return friendGiftMapper.selectById(id);
    }

    @Override
    public EsShopFriendGiftCard giftcard(long id) {
        return friendGiftCardMapper.selectById(id);
    }
}
