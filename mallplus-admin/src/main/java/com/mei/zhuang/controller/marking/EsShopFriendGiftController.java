package com.mei.zhuang.controller.marking;

import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.service.marking.EsShopFriendGiftService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.marking.EsShopFriendGift;
import com.mei.zhuang.entity.marking.EsShopFriendGiftCard;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(value = "好友赠礼", description = "", tags = {"好友赠礼"})
@RestController
@RequestMapping("/api/friendgift")
public class EsShopFriendGiftController {
    @Resource
    private EsShopFriendGiftService giftService;


    @SysLog(MODULE = "好友赠礼保存", REMARK = "好友赠礼保存")
    @ApiOperation("好友赠礼保存")
    @PostMapping("/save")
    public Object save(EsShopFriendGift entity) {

        try {
            List<EsShopFriendGiftCard> GiftCards = JSONObject.parseArray(entity.getGiftCardList(), EsShopFriendGiftCard.class);
             entity.setFriendgifcard(GiftCards);
            return new CommonResult().success(giftService.save(entity));

        } catch (Exception e) {
            log.error("好友赠礼保存失败：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "好友赠礼修改", REMARK = "好友赠礼修改")
    @ApiOperation("好友赠礼修改")
    @PostMapping("/update")
    public Object update(EsShopFriendGift entity) {
        try {
            List<EsShopFriendGiftCard> GiftCards = JSONObject.parseArray(entity.getGiftCardList(), EsShopFriendGiftCard.class);
            entity.setFriendgifcard(GiftCards);
            return new CommonResult().success(giftService.update(entity));

        } catch (Exception e) {
            log.error("好友赠礼修改：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "好友赠礼查询", REMARK = "好友赠礼查询")
    @ApiOperation("好友赠礼查询")
    @PostMapping("/list")
    public Object list() {
        try {
            EsShopFriendGift friend = giftService.friend();
            if(friend!=null){
                return new CommonResult().success(friend);
            }else{
                return new CommonResult().failed("未开启好友赠礼");
            }
        } catch (Exception e) {
            log.error("好友赠礼查询失败：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }




}
