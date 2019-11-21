package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author arvato team
 * @since 2019-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_order_comment")
public class EsShopOrderComment extends Model<EsShopOrderComment> {

    private static final long serialVersionUID = 1L;

    /**
     * 评论内容
     */
    private String content;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 选项id
     */
    @TableField("option_id")
    private Long optionId;
    /**
     * 回复图像
     */
    @TableField("reply_images")
    private String replyImages;
    /**
     * 检查
     */
    private Integer checked;
    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 回复时间
     */
    @TableField("reply_time")
    private Date replyTime;
    /**
     * 订单商品id
     */
    @TableField("order_goods_id")
    private Long orderGoodsId;
    /**
     * 附加回复时间
     */
    @TableField("append_reply_time")
    private Date appendReplyTime;
    /**
     * 订单类型
     */
    @TableField("order_type")
    private Integer orderType;
    /**
     * 附加回复图片
     */
    @TableField("append_reply_images")
    private String appendReplyImages;
    /**
     * 附加时间
     */
    @TableField("append_time")
    private Date appendTime;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 附加回复内容
     */
    @TableField("append_reply_content")
    private String appendReplyContent;
    /**
     * 附加图片
     */
    @TableField("append_images")
    private String appendImages;
    /**
     * 商品标题
     */
    @TableField("goods_title")
    private String goodsTitle;
    /**
     * 是否置顶
     */
    @TableField("is_top")
    private Long isTop;
    /**
     * 会员id
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 附加内容
     */
    @TableField("append_content")
    private String appendContent;
    /**
     * 商品图片
     */
    @TableField("goods_img")
    private String goodsImg;
    /**
     * 选项标题
     */
    @TableField("option_title")
    private String optionTitle;
    /**
     * 附加检查
     */
    @TableField("append_checked")
    private Integer appendChecked;
    private Long id;
    /**
     * 图片
     */
    private String images;
    /**
     * 回复内容
     */
    @TableField("reply_content")
    private String replyContent;
    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
