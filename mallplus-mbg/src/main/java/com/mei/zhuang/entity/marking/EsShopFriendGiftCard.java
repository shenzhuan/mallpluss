package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_friend_gift_card")
public class EsShopFriendGiftCard {

    private long id;
    //送礼id
    @TableField("gift_id")
    private long giftId;
    //卡片图
    @TableField("gift_picture")
    private String giftPicture;
    // 卡片名称
    private String title;
    //1 显示  2.隐藏
    private Integer according;
}
