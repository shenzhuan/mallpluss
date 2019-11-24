package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 好友赠礼
 * </p>
 *
 * @author meizhuang team
 * @since 2019-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_friend_gift")
public class EsShopFriendGift extends Model<EsShopFriendGift> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)     private Long id;

    /**
     * 送礼规则标题
     */
    @TableField("gift_title")
    private String giftTitle;
    /**
     * 规则跳转
     */
    @TableField("rules_link")
    private String rulesLink;
    /**
     * 文字提示
     */
    private String tooltip;
    /**
     * 分享图片
     */
    @TableField("share_photos")
    private String sharePhotos;
    /**
     * 分享标题
     */
    @TableField("share_title")
    private String shareTitle;
    /**
     * 收礼卡图
     */
    @TableField("accept_card")
    private String acceptCard;
    /**
     * 已领取卡图
     */
    @TableField("receive_card")
    private String receiveCard;
    /**
     * 收礼提示语
     */
    @TableField("receiving_gifts")
    private String receivingGifts;
    //状态  1.开启 2.禁用
    private String status;

    @TableField(exist = false)
    private String giftCardList;

    @TableField(exist = false)
    private List<EsShopFriendGiftCard> friendgifcard;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
